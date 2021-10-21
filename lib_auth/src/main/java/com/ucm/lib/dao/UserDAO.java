package com.ucm.lib.dao;

import com.ucm.lib.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    
    public User getEmailById(int id);
	
	public User findByUsername(String username);
	
	public User findByEmailIgnoreCase(String email);
	
	public Boolean existsByUsername(String username);

	public Boolean existsByEmail(String email);
	
	public User findById(int id);
	
	public User getUserById(int id);
}
