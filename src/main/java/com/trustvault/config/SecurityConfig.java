package com.trustvault.config;
import com.trustvault.service.CustomUserDetailsService;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService service,PasswordEncoder encoder){
  DaoAuthenticationProvider p=new DaoAuthenticationProvider(service); p.setPasswordEncoder(encoder); return p;
 }
 @Bean SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
  http.authorizeHttpRequests(a->a.requestMatchers("/","/register","/login","/css/**","/error").permitAll()
   .requestMatchers("/audit","/admin/**").hasRole("ADMIN").anyRequest().authenticated())
   .formLogin(f->f.loginPage("/login").defaultSuccessUrl("/dashboard",true).permitAll())
   .logout(l->l.logoutSuccessUrl("/login?logout").permitAll());
  return http.build();
 }
}
