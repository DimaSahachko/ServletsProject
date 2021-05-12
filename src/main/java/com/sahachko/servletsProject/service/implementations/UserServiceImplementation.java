package com.sahachko.servletsProject.service.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sahachko.servletsProject.model.FileStatus;
import com.sahachko.servletsProject.model.User;
import com.sahachko.servletsProject.model.UserFile;
import com.sahachko.servletsProject.repository.UserRepository;
import com.sahachko.servletsProject.service.UserFileService;
import com.sahachko.servletsProject.service.UserService;

public class UserServiceImplementation implements UserService {
	
	UserRepository repository;
	UserFileService userFileService;

	public UserServiceImplementation(UserRepository repository, UserFileService userFileService) {
		this.repository = repository;
		this.userFileService = userFileService;
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
			if(user.getFiles() != null) {
				List<UserFile> files = user.getFiles().stream().filter(file -> file.getStatus().equals(FileStatus.ACTIVE)).collect(Collectors.toCollection(ArrayList::new));
				user.setFiles(files);
			}
		}
		return users;
	}

	@Override
	public User getUserById(int id) {
		User user = repository.getById(id);
		if(user.getFiles() != null) {
			List<UserFile> files = user.getFiles().stream().filter(file -> file.getStatus().equals(FileStatus.ACTIVE)).collect(Collectors.toCollection(ArrayList::new));
			user.setFiles(files);
		}
		return user;
	}

	@Override
	public void deleteUserById(int id) {
		User user = getUserById(id);
		if(user.getFiles() != null) {
			user.getFiles().stream().forEach(userFile -> userFileService.deleteUserFileById(userFile.getId()));
		}
		repository.deleteById(id);
		
	}
	
}
