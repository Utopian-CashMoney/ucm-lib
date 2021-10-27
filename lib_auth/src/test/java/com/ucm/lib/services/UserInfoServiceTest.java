package com.ucm.lib.services;

import com.ucm.lib.dao.UserDAO;
import com.ucm.lib.entities.User;
import com.ucm.lib.entities.UserRole;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class UserInfoServiceTest {

	@InjectMocks
	@Autowired
	UserInfoService userService;

	@MockBean
	UserDAO userRepo;

	@Test
	@AutoConfigureTestDatabase
	void testLoadUserByUsername() {
		User u1 = new User();
		u1.setUsername("name");
		u1.setEmail("name1@gmail.com");
		u1.setPassword("Name1");
		u1.setPhNum("111-111-1111");
		u1.setFirstName("name1");
		u1.setLastName("one");
		u1.setLastName("one");
		u1.setActive(true);

		UserRole userRole = new UserRole();
		userRole.setId(1);
		userRole.setName("Role");
		u1.setRole(userRole);

		when(userRepo.findByUsername("name")).thenReturn(u1);
		UserDetails userDetail = userService.loadUserByUsername("name");

		assertEquals("name", userDetail.getUsername());

		// Test with different userName
		u1.setUsername("nameTwo");

		when(userRepo.findByUsername("nameTwo")).thenReturn(u1);
		userDetail = userService.loadUserByUsername("nameTwo");
		assertEquals("nameTwo", userDetail.getUsername());

		// Test with different userName
		u1.setUsername("nameThree");
		when(userRepo.findByUsername("nameThree")).thenReturn(u1);
		userDetail = userService.loadUserByUsername("nameThree");
		assertEquals("nameThree", userDetail.getUsername());

	}

}
