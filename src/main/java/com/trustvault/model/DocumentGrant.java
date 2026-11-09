package com.trustvault.model;
import jakarta.persistence.*;
@Entity @Table(name="document_grants",uniqueConstraints=@UniqueConstraint(columnNames={"document_id","user_id"}))
public class DocumentGrant {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="document_id") private StoredDocument document;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="user_id") private AppUser user;
 public Long getId(){return id;} public StoredDocument getDocument(){return document;} public void setDocument(StoredDocument v){document=v;}
 public AppUser getUser(){return user;} public void setUser(AppUser v){user=v;}
}
