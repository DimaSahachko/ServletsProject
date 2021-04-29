package com.sahachko.servletsProject.service;

import java.util.List;

import com.sahachko.servletsProject.model.User;


public interface UserService {
	
	User saveUser(User user);
	
	User updateUser(User user);

	List<User> getAllUsers();
	
	User getUserById(int id);
	
	boolean deleteUserById(int id);
}
