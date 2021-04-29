package com.sahachko.servletsProject.service.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sahachko.servletsProject.exceptions.BannedFileException;
import com.sahachko.servletsProject.exceptions.ResourceNotFoundException;
import com.sahachko.servletsProject.model.Event;
import com.sahachko.servletsProject.model.EventAction;
import com.sahachko.servletsProject.model.FileStatus;
import com.sahachko.servletsProject.model.User;
import com.sahachko.servletsProject.model.UserFile;
import com.sahachko.servletsProject.repository.EventRepository;
import com.sahachko.servletsProject.repository.UserFileRepository;
import com.sahachko.servletsProject.repository.UserRepository;
import com.sahachko.servletsProject.service.UserFileService;

public class UserFileServiceImplementation implements UserFileService {
	UserFileRepository userFileRepository;
	UserRepository userRepository;
	EventRepository eventRepository;
	
	public UserFileServiceImplementation(UserFileRepository userFileRepository, UserRepository userRepository, EventRepository eventRepository) {
		super();
		this.userFileRepository = userFileRepository;
		this.userRepository = userRepository;
		this.eventRepository = eventRepository;
	}

	@Override
	public UserFile saveUserFile(UserFile file) {
		User user = userRepository.getById(file.getUserId());
		if (user == null) {
			throw new ResourceNotFoundException("There is no user with such id");
		}
		file = userFileRepository.save(file);
		
		Event event = new Event(file.getId(), file.getName(), EventAction.UPLOADING);
		event = eventRepository.save(event);
		user.addFile(file);
		user.addEvent(event);
		userRepository.update(user);
		return file;
	}

	@Override
	public UserFile updateUserFile(UserFile file) {
		file = userFileRepository.update(file);
		if (file == null) {
			throw new ResourceNotFoundException("There is no file with such id");
		}
		file = userFileRepository.getById(file.getId());
		Event event = new Event(file.getId(), file.getName(), EventAction.UPDATING);
		event = eventRepository.save(event);
		User user = userRepository.getById(file.getUserId());
		user.addEvent(event);
		userRepository.update(user);
		return file;
	}

	@Override
	public List<UserFile> getAllUsersFiles() {	
		List<UserFile> allFiles = userFileRepository.getAll();
		List<UserFile> activeFiles = allFiles.stream().filter(file -> file.getStatus().equals(FileStatus.ACTIVE)).collect(Collectors.toCollection(ArrayList::new));
		return activeFiles;
	}

	@Override
	public UserFile getUserFileById(int id) {
		UserFile file =  userFileRepository.getById(id);
		if(file == null) {
			throw new ResourceNotFoundException("There is no file with such id");
		}
		if(file.getStatus().equals(FileStatus.DELETED)) {
			throw new ResourceNotFoundException("File has been removed");
		}
		if(file.getStatus().equals(FileStatus.BANNED)) {
			throw new BannedFileException("This file is banned"); 
		}
		return file;
	}

	@Override
	public boolean deleteUserFileById(int id) {
		UserFile file = getUserFileById(id);
		file.setStatus(FileStatus.DELETED);
		userFileRepository.update(file);
		
		User user = userRepository.getById(file.getUserId());
		Event event = new Event(file.getId(), file.getName(), EventAction.DELETION);
		eventRepository.save(event);
		user.addEvent(event);
		userRepository.update(user);
		return true;
	}
	
}
