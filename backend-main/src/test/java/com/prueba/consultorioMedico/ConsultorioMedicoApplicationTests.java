package com.prueba.consultorioMedico;

import com.prueba.consultorioMedico.enums.RoleEnum;
import com.prueba.consultorioMedico.model.Admin;
import com.prueba.consultorioMedico.model.AdminUser;
import com.prueba.consultorioMedico.model.User;
import com.prueba.consultorioMedico.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
class ConsultorioMedicoApplicationTests {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        System.out.println("========== RESETTING ALL USER PASSWORDS IN DATABASE ==========");
        List<User> allUsers = userRepository.findAll();
        for (User u : allUsers) {
            String newPassword = u.getUsername() + "123";
            System.out.println("User: " + u.getUsername() + " | DNI: " + u.getDni() + " | Role: " + u.getRole() + " -> Setting password to: " + newPassword);
            
            u.setPassword(passwordEncoder.encode(newPassword));
            u.setNewAccount(false); // Make sure newAccount is false so they don't get forced password change dialogs immediately
            
            if (u.getRole() == RoleEnum.ADMIN && u instanceof Admin) {
                Admin admin = (Admin) u;
                // Clear existing sub-users or reset their passwords
                for (AdminUser au : admin.getUsers()) {
                    System.out.println("  Admin sub-user: " + au.getUsername() + " -> Setting password to: " + newPassword);
                    au.setPassword(passwordEncoder.encode(newPassword));
                }
            }
            
            userRepository.save(u);
        }
        System.out.println("=============================================================");
    }
}
