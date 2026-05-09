package edu.kennesaw.smarthome;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "CORS_ALLOWED_ORIGINS=http://localhost:5173")
class SmartHomeApplicationTests {

	@Test
	void contextLoads() {
	}

}
