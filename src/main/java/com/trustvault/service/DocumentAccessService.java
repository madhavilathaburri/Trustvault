package com.trustvault.service;
import com.trustvault.model.*;
import com.trustvault.repository.DocumentGrantRepository;
import org.springframework.stereotype.Service;
@Service
public class DocumentAccessService {
 private final DocumentGrantRepository grants;
 public DocumentAccessService(DocumentGrantRepository grants){this.grants=grants;}
 public boolean canRead(AppUser user,StoredDocument doc){
  if(user.getRole()==Role.ADMIN) return true;
  if(doc.getOwner().getId().equals(user.getId())) return true;
  return grants.existsByDocumentIdAndUserId(doc.getId(),user.getId());
 }
}
