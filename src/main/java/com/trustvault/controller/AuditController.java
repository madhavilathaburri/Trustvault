package com.trustvault.controller;
import com.trustvault.repository.AuditLogRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class AuditController {
 private final AuditLogRepository logs;
 public AuditController(AuditLogRepository logs){this.logs=logs;}
 @GetMapping("/audit") public String audit(Model m){m.addAttribute("logs",logs.findTop200ByOrderByEventTimeDesc());return "audit";}
}
