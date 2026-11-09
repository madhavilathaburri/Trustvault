package com.trustvault.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="stored_documents")
public class StoredDocument {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=255) private String originalFileName;
 @Column(nullable=false,unique=true,length=80) private String storageName;
 @Column(nullable=false,length=120) private String contentType;
 @Column(nullable=false) private long fileSize;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="owner_id") private AppUser owner;
 private LocalDateTime uploadedAt=LocalDateTime.now();
 public Long getId(){return id;} public String getOriginalFileName(){return originalFileName;} public void setOriginalFileName(String v){originalFileName=v;}
 public String getStorageName(){return storageName;} public void setStorageName(String v){storageName=v;}
 public String getContentType(){return contentType;} public void setContentType(String v){contentType=v;}
 public long getFileSize(){return fileSize;} public void setFileSize(long v){fileSize=v;}
 public AppUser getOwner(){return owner;} public void setOwner(AppUser v){owner=v;}
 public LocalDateTime getUploadedAt(){return uploadedAt;}
}
