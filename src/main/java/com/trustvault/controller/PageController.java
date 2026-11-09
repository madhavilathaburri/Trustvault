
package com.trustvault.controller;

import com.trustvault.model.AppUser;
import com.trustvault.repository.UserRepository;
import jakarta.validation.constraints.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public PageController(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model m) {
        m.addAttribute("form", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String create(
            @ModelAttribute("form") RegisterForm f,
            BindingResult r) {

        if (!java.util.Objects.equals(
                f.getPassword(), f.getConfirmPassword())) {
            r.rejectValue("confirmPassword", "mismatch",
                    "Passwords do not match");
        }

        if (f.getEmail() != null &&
                users.existsByEmailIgnoreCase(f.getEmail().trim())) {
            r.rejectValue("email", "duplicate",
                    "Email already registered");
        }

        if (r.hasErrors()) {
            return "register";
        }

        AppUser u = new AppUser();
        u.setFullName(f.getFullName().trim());
        u.setEmail(f.getEmail().trim().toLowerCase());
        u.setPassword(encoder.encode(f.getPassword()));

        users.save(u);

        return "redirect:/login?registered";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication a, Model m) {
        m.addAttribute(
                "currentUser",
                users.findByEmailIgnoreCase(a.getName()).orElseThrow()
        );

        return "dashboard";
    }

    public static class RegisterForm {

        @NotBlank
        @Size(max = 100)
        private String fullName;

        @NotBlank
        @Email
        @Size(max = 120)
        private String email;

        @NotBlank
        @Size(min = 8, max = 72)
        private String password;

        @NotBlank
        private String confirmPassword;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }
    }
}
