package com.trustvault.controller;
import com.trustvault.model.*;
import com.trustvault.repository.*;
import com.trustvault.service.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
@Controller
public class DocumentController {
 private static final long MAX_SIZE=10L*1024*1024;
 private final Path folder=Paths.get("uploads").toAbsolutePath().normalize();
 private final UserRepository users; private final DocumentRepository docs; private final DocumentGrantRepository grants;
 private final AuditService audit; private final DocumentAccessService access;
 public DocumentController(UserRepository u,DocumentRepository d,DocumentGrantRepository g,AuditService a,DocumentAccessService s)throws IOException{
  users=u;docs=d;grants=g;audit=a;access=s;Files.createDirectories(folder);
 }
 @GetMapping("/documents") public String list(Authentication auth,Model m){
  AppUser user=current(auth); List<StoredDocument> visible=new ArrayList<>();
  if(user.getRole()==Role.ADMIN) visible=docs.findAllByOrderByUploadedAtDesc();
  else {
   visible.addAll(docs.findByOwnerEmailOrderByUploadedAtDesc(user.getEmail()));
   for(DocumentGrant grant:grants.findByUserEmail(user.getEmail())) if(visible.stream().noneMatch(x->x.getId().equals(grant.getDocument().getId()))) visible.add(grant.getDocument());
  }
  m.addAttribute("documents",visible);m.addAttribute("currentUser",user);return "documents";
 }
 @PostMapping("/documents/upload") public String upload(@RequestParam("file") MultipartFile file,Authentication auth)throws IOException{
  AppUser owner=current(auth); if(file.isEmpty())return "redirect:/documents?error=empty";
  if(file.getSize()>MAX_SIZE)return "redirect:/documents?error=size";
  String original=Paths.get(Objects.requireNonNullElse(file.getOriginalFilename(),"file")).getFileName().toString();
  String key=UUID.randomUUID().toString();Path target=folder.resolve(key).normalize();
  if(!target.startsWith(folder))throw new IOException("Invalid file path");
  Files.copy(file.getInputStream(),target,StandardCopyOption.REPLACE_EXISTING);
  StoredDocument d=new StoredDocument();d.setOriginalFileName(original);d.setStorageName(key);
  d.setContentType(Objects.requireNonNullElse(file.getContentType(),MediaType.APPLICATION_OCTET_STREAM_VALUE));
  d.setFileSize(file.getSize());d.setOwner(owner);docs.save(d);
  audit.record(owner.getEmail(),"UPLOAD",original,"ALLOWED","File uploaded");return "redirect:/documents?uploaded";
 }
 @GetMapping("/documents/{id}/download") public ResponseEntity<?> download(@PathVariable Long id,Authentication auth)throws IOException{
  AppUser user=current(auth);StoredDocument d=docs.findById(id).orElse(null);
  if(d==null){audit.record(user.getEmail(),"DOWNLOAD","id:"+id,"DENIED","Document not found");return ResponseEntity.notFound().build();}
  if(!access.canRead(user,d)){audit.record(user.getEmail(),"DOWNLOAD",d.getOriginalFileName(),"DENIED","No owner or sharing permission");return ResponseEntity.status(403).body("Access denied");}
  Path path=folder.resolve(d.getStorageName()).normalize();
  if(!path.startsWith(folder)||!Files.exists(path)){audit.record(user.getEmail(),"DOWNLOAD",d.getOriginalFileName(),"ERROR","Stored file missing");return ResponseEntity.notFound().build();}
  audit.record(user.getEmail(),"DOWNLOAD",d.getOriginalFileName(),"ALLOWED","File downloaded");
  String safe=d.getOriginalFileName().replace("\\","_").replace("\"","_");
  return ResponseEntity.ok().contentType(MediaType.parseMediaType(d.getContentType()))
   .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+safe+"\"")
   .contentLength(d.getFileSize()).body(new FileSystemResource(path));
 }
 @PostMapping("/admin/documents/{id}/grant") public String grant(@PathVariable Long id,@RequestParam String email,Authentication auth){
  AppUser actor=current(auth);StoredDocument d=docs.findById(id).orElse(null);AppUser recipient=users.findByEmailIgnoreCase(email.trim()).orElse(null);
  if(d==null||recipient==null){audit.record(actor.getEmail(),"GRANT_ACCESS","id:"+id,"DENIED","Document or user missing");return "redirect:/documents?grantError";}
  if(!grants.existsByDocumentIdAndUserId(id,recipient.getId())){DocumentGrant g=new DocumentGrant();g.setDocument(d);g.setUser(recipient);grants.save(g);}
  audit.record(actor.getEmail(),"GRANT_ACCESS",d.getOriginalFileName(),"ALLOWED","Granted to "+recipient.getEmail());return "redirect:/documents?granted";
 }
 private AppUser current(Authentication a){return users.findByEmailIgnoreCase(a.getName()).orElseThrow();}
}
