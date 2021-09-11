package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
	void testRegistrationLoginUser() {
//		String login = "username";
//		String password = "testtest";
//
//		UserLoginModel userLoginModel = new UserLoginModel();
//		userLoginModel.setUsername(login);
//		userLoginModel.setPassword(password);
//
//		ResponseEntity<Map> entityBody = testRestTemplate
//				.postForEntity("http://localhost:" + port + "/auth/registration", userLoginModel, Map.class);
//		assertEquals(HttpStatus.OK, entityBody.getStatusCode());
//		assertEquals(MediaType.APPLICATION_JSON, entityBody.getHeaders().getContentType());
//		//check login and token
//		Map<String, String> responseEntityBody = entityBody.getBody();
//		for (Map.Entry<String, String> entry : responseEntityBody.entrySet()) {
//			if (entry.getKey().equals("username"))
//				assertEquals(login, entry.getValue());
//		}
//
//		ResponseEntity<Map> responseEntity = testRestTemplate
//				.postForEntity("http://localhost:" + port + "/auth/login", userLoginModel, Map.class);
//
//		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//		assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
//		//check login and token
//		Map<String, String> body = responseEntity.getBody();
//		for (Map.Entry<String, String> entry : body.entrySet()) {
//			if (entry.getKey().equals("username"))
//				assertEquals(login, entry.getValue());
//		}

	}
}
