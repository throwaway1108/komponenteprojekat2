package com.raf.gaminguserservice.runner;

import com.raf.gaminguserservice.domain.Role;
import com.raf.gaminguserservice.domain.User;
import com.raf.gaminguserservice.repository.RoleRepository;
import com.raf.gaminguserservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public TestDataRunner(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Role rolePlayer = new Role("ROLE_PLAYER", "Standard player role");
        Role roleAdmin = new Role("ROLE_ADMIN", "Administrator role");

        if (roleRepository.count() == 0) {
            roleRepository.save(rolePlayer);
            roleRepository.save(roleAdmin);
        } else {
            rolePlayer = roleRepository.findRoleByName("ROLE_PLAYER").get();
            roleAdmin = roleRepository.findRoleByName("ROLE_ADMIN").get();
        }

        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("Adminovic");
            admin.setEmail("admin@gmail.com");
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setRole(roleAdmin);
            admin.setActive(true);
            admin.setBlocked(false);

            admin.setAttendancePercentage(100.0);
            admin.setSuccessfullyOrganizedSessions(0);
            admin.setOrganizerTitle("Veteran");

            userRepository.save(admin);

            User player = new User();
            player.setFirstName("Igrac");
            player.setLastName("Igracovic");
            player.setEmail("player@gmail.com");
            player.setUsername("player1");
            player.setPassword("player1");
            player.setRole(rolePlayer);
            player.setActive(true);
            player.setBlocked(false);
            player.setAttendancePercentage(100.0);
            player.setSuccessfullyOrganizedSessions(0);
            player.setOrganizerTitle("Beginner");

            userRepository.save(player);

            System.out.println("GamingUser Service: Baza je uspesno popunjena (Admin i Player kreirani).");
        }
    }
}