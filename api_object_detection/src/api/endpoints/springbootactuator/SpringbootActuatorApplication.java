package api.endpoints.springbootactuator;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class SpringbootActuatorApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SpringbootActuatorApplication.class);
		app.setDefaultProperties(Collections
				.<String, Object>singletonMap("server.port", "9080"));
		app.run(args);
	}

}
