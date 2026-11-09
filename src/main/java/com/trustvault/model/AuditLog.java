package com.trustvault.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="audit_logs")
public class AuditLog {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=120) private String actorEmail;
 @Column(nullable=false,length=80) private String action;
 @Column(length=255) private String documentName;
 @Column(nullable=false,length=20) private String outcome;
 @Column(length=500) private String details;
 @Column(nullable=false) private LocalDateTime eventTime=LocalDateTime.now();
 public Long getId(){return id;} public String getActorEmail(){return actorEmail;} public void setActorEmail(String v){actorEmail=v;}
 public String getAction(){return action;} public void setAction(String v){action=v;}
 public String getDocumentName(){return documentName;} public void setDocumentName(String v){documentName=v;}
 public String getOutcome(){return outcome;} public void setOutcome(String v){outcome=v;}
 public String getDetails(){return details;} public void setDetails(String v){details=v;}
 public LocalDateTime getEventTime(){return eventTime;}
}
