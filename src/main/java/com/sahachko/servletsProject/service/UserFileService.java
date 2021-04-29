package com.sahachko.servletsProject.service;

import java.util.List;

import com.sahachko.servletsProject.model.UserFile;

public interface UserFileService {
	
	UserFile saveUserFile(UserFile file);
	
	UserFile updateUserFile(UserFile file);

	List<UserFile> getAllUsersFiles();
	
	UserFile getUserFileById(int id);
	
	boolean deleteUserFileById(int id);
}
