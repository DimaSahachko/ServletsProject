package com.sahachko.servletsProject.service.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sahachko.servletsProject.exceptions.ResourceNotFoundException;
import com.sahachko.servletsProject.model.FileStatus;
import com.sahachko.servletsProject.model.User;
import com.sahachko.servletsProject.model.UserFile;
import com.sahachko.servletsProject.repository.UserRepository;
import com.sahachko.servletsProject.service.UserService;

public class UserServiceImplementation implements UserService {
	
	UserRepository repository;

	public UserServiceImplementation(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public User saveUser(User user) {
		return repository.save(user);
	}

	@Override
	public User updateUser(User user) {
		return repository.update(user);
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users =  repository.getAll();
		for (User user : users) {
			List<UserFile> files = user.getFiles().stream().filter(file -> file.getStatus().equals(FileStatus.ACTIVE)).collect(Collectors.toCollection(ArrayList::new));
			user.setFiles(files);
		}
		return users;
	}

	@Override
	public User getUserById(int id) {
		User user = repository.getById(id);
		if(user == null) {
			throw new ResourceNotFoundException("There is no user with such id");
		}
		List<UserFile> files = user.getFiles().stream().filter(file -> file.getStatus().equals(FileStatus.ACTIVE)).collect(Collectors.toCollection(ArrayList::new));
		user.setFiles(files);
		return user;
	}

	@Override
	public boolean deleteUserById(int id) {
		return repository.deleteById(id);
	}
	
}
