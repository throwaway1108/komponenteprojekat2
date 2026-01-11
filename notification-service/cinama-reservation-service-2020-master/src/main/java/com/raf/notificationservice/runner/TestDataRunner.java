package com.raf.notificationservice.runner;

import com.raf.notificationservice.domain.NotificationType;
import com.raf.notificationservice.repository.NotificationTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {

    private final NotificationTypeRepository notificationTypeRepository;

    public TestDataRunner(NotificationTypeRepository notificationTypeRepository) {
        this.notificationTypeRepository = notificationTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (notificationTypeRepository.count() == 0) {
            notificationTypeRepository.save(new NotificationType("ACTIVATION_EMAIL", "Aktivacija", "Vaš kod je: %s"));
            notificationTypeRepository.save(new NotificationType("TITLE_UPDATE", "Nova titula", "Čestitamo na tituli: %s"));
            notificationTypeRepository.save(new NotificationType("SESSION_CANCELLED", "Otkazana sesija", "Sesija %s je otkazana od strane korisnika %s."));
            notificationTypeRepository.save(new NotificationType("JOIN_CONFIRMATION", "Potvrda pridruživanja", "Uspešno ste se pridružili sesiji: %s"));
            notificationTypeRepository.save(new NotificationType("SESSION_INVITATION", "Pozivnica za sesiju", "Pozvani ste na sesiju. Vaš token je: %s"));
        }
    }
}