package ensah.absencemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class AbsenceManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbsenceManagementApplication.class, args);
	}

}
