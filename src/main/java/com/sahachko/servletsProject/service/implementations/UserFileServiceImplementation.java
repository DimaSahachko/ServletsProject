package com.sahachko.servletsProject.service.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
import com.sahachko.servletsProject.service.FilesIOService;
import com.sahachko.servletsProject.service.UserFileService;

public class UserFileServiceImplementation implements UserFileService {
	UserFileRepository userFileRepository;
	UserRepository userRepository;
	EventRepository eventRepository;
	FilesIOService ioService;

	public UserFileServiceImplementation(UserFileRepository userFileRepository, UserRepository userRepository,
			EventRepository eventRepository, FilesIOService ioService) {
		this.userFileRepository = userFileRepository;
		this.userRepository = userRepository;
		this.eventRepository = eventRepository;
		this.ioService = ioService;
	}

	@Override
	public UserFile saveUserFile(UserFile file, byte[] bytes, String fileName) {
		userRepository.getById(file.getUserId());
		file.setStatus(FileStatus.ACTIVE);
		UUID uuid = UUID.randomUUID();
		String storedName = uuid + "__" + fileName;
		file.setName(storedName);
		file = userFileRepository.save(file);
		ioService.writeUserFile(bytes, file.getUserId(), storedName);
		passInformationAboutOperationWithFileToUser(file, EventAction.UPLOADING);
		return file;
	}

	@Override
	public UserFile updateUserFile(UserFile file) {
		file = userFileRepository.update(file);
		file = userFileRepository.getById(file.getId());
		passInformationAboutOperationWithFileToUser(file, EventAction.UPDATING);
		return file;
	}

	@Override
	public List<UserFile> getAllUsersFiles() {
		List<UserFile> allFiles = userFileRepository.getAll();
		List<UserFile> activeFiles = allFiles.stream().filter(file -> file.getStatus().equals(FileStatus.ACTIVE))
				.collect(Collectors.toCollection(ArrayList::new));
		return activeFiles;
	}

	@Override
	public UserFile getUserFileById(int id) {
		UserFile file = userFileRepository.getById(id);
		if (FileStatus.DELETED.equals(file.getStatus())) {
			throw new ResourceNotFoundException("This file has been removed");
		}
		if (FileStatus.BANNED.equals(file.getStatus())) {
			throw new BannedFileException("This file is banned");
		}
		return file;
	}

	@Override
	public void deleteUserFileById(int id) {
		UserFile file = getUserFileById(id);
		file.setStatus(FileStatus.DELETED);
		userFileRepository.update(file);
		ioService.deleteUserFile(file.getUserId(), file.getName());
		passInformationAboutOperationWithFileToUser(file, EventAction.DELETION);
	}

	void passInformationAboutOperationWithFileToUser(UserFile file, EventAction action) {
		User user = userRepository.getById(file.getUserId());
		Event event = new Event(file.getId(), file.getName(), action);
		event = eventRepository.save(event);
		if(event.getEventAction().equals(EventAction.UPLOADING)) {
			user.addFile(file);
		}
		user.addEvent(event);
		userRepository.update(user);
	}
}
