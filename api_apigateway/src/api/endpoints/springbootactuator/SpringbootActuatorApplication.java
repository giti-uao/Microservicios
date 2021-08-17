package api.endpoints.springbootactuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootActuatorApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SpringbootActuatorApplication.class);
        app.run(args);
	}

}
