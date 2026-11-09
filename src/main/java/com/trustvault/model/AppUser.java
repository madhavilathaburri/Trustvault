package com.trustvault.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="app_users")
public class AppUser {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=120) private String email;
 @Column(nullable=false,length=100) private String fullName;
 @Column(nullable=false) private String password;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private Role role=Role.EMPLOYEE;
 private LocalDateTime createdAt=LocalDateTime.now();
 public Long getId(){return id;} public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
 public String getPassword(){return password;} public void setPassword(String v){password=v;}
 public Role getRole(){return role;} public void setRole(Role v){role=v;}
}
