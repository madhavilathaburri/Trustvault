package com.trustvault.service;
import com.trustvault.model.AuditLog;
import com.trustvault.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
@Service
public class AuditService {
 private final AuditLogRepository logs;
 public AuditService(AuditLogRepository logs){this.logs=logs;}
 public void record(String actor,String action,String doc,String outcome,String details){
  AuditLog l=new AuditLog(); l.setActorEmail(actor==null?"ANONYMOUS":actor);
  l.setAction(action); l.setDocumentName(doc); l.setOutcome(outcome); l.setDetails(details); logs.save(l);
 }
}
