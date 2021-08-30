package com.example.demo;

import com.example.demo.entity.model.UserLoginModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"h2db", "debug"})
class GoodLifestyleApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void testContextLoads() {
		assertNotNull(testRestTemplate);
		assertNotEquals(0, port);
	}

	@Test
	void testCreateUserList() {
		UserLoginModel userLoginModel = new UserLoginModel();
		userLoginModel.setUsername("username");
		userLoginModel.setPassword("password");

		ResponseEntity<Map> responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/auth/registration", userLoginModel, Map.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
		//check login and token
	}
}
