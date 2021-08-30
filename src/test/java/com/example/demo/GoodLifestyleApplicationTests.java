package com.example.demo;

import com.example.demo.entity.model.UserLoginModel;
import com.example.demo.entity.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"h2db", "debug"})
class GoodLifestyleApplicationTests {

	private String token;

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
	void testRegistrationUser() {
		String login = "username";
		String password = "password";

		UserLoginModel userLoginModel = new UserLoginModel();
		userLoginModel.setUsername(login);
		userLoginModel.setPassword(password);

		Role role = new Role();
		role.setName("ROLE_ADMIN");
		List<Role> roles = new ArrayList<>();
		roles.add(role);
		userLoginModel.setRoles(roles);

		ResponseEntity<Map> responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/auth/registration", userLoginModel, Map.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
		//check login and token
		Map<String, String> body = responseEntity.getBody();
		for (Map.Entry<String, String> entry : body.entrySet()) {
			if (entry.getKey().equals("username"))
				assertEquals(login, entry.getValue());
			if (entry.getKey().equals("token"))
				token = entry.getValue();
		}
	}

//	@Test
	void testLoginUser() {
		String login = "username";
		String password = "password";

		UserLoginModel userLoginModel = new UserLoginModel();
		userLoginModel.setUsername(login);
		userLoginModel.setPassword(password);

		Role role = new Role();
		role.setName("ROLE_ADMIN");
		List<Role> roles = new ArrayList<>();
		roles.add(role);
		userLoginModel.setRoles(roles);

		ResponseEntity<Map> responseEntity = testRestTemplate.withBasicAuth("Authorization", token).postForEntity("http://localhost:" + port + "/auth/login", userLoginModel, Map.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
		//check login and token
		Map<String, String> body = responseEntity.getBody();
		for (Map.Entry<String, String> entry : body.entrySet()) {
			if (entry.getKey().equals("username"))
				assertEquals(login, entry.getValue());
		}
	}
}
