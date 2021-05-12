package com.sahachko.servletsProject.service.implementations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyInt;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.sahachko.servletsProject.exceptions.ResourceNotFoundException;
import com.sahachko.servletsProject.model.FileStatus;
import com.sahachko.servletsProject.model.User;
import com.sahachko.servletsProject.model.UserFile;
import com.sahachko.servletsProject.repository.UserRepository;
import com.sahachko.servletsProject.service.UserFileService;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {
	
	@Mock
	private UserFileService userFileService;
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private UserServiceImplementation userService;
	
	@Test
	public void testSaveUser_shouldCallRepositorySaveMethod() {
		User user = new User();
		userService.saveUser(user);
		verify(userRepository).save(user);
	}
	
	@Test
	public void testSaveUser_shouldReturnTheSameObject() {
		User user = new User();
		when(userRepository.save(user)).thenReturn(user);
		assertEquals(user, userService.saveUser(user));
	}
	
	@Test
	public void testUpdateUser_shouldCallRepositoryUpdateMethod() {
		User user = new User();
		userService.updateUser(user);
		verify(userRepository).update(user);
	}
	
	@Test
	public void testUpdateUser_shouldReturnTheSameObject() {
		User user = new User();
		when(userRepository.update(user)).thenReturn(user);
		assertEquals(user, userService.updateUser(user));
	}
	
	@Test
	public void testGetAllUsers_shouldCallRepositoryGetAllMethod() {
		userService.getAllUsers();
		verify(userRepository).getAll();
	}
	
	@Test
	public void testGetAllUsers_shouldReturnTheSameAmountOfUsersAsRepositoryReturns() {
		User user1 = mock(User.class);
		User user2 = mock(User.class);
		User user3 = mock(User.class);
		List<User> allUsers = Arrays.asList(user1, user2, user3);
		when(user1.getFiles()).thenReturn(new ArrayList<UserFile>());
		when(user2.getFiles()).thenReturn(new ArrayList<UserFile>());
		when(user3.getFiles()).thenReturn(new ArrayList<UserFile>());
		when(userRepository.getAll()).thenReturn(allUsers);
		assertEquals(3, userService.getAllUsers().size());
	}
	
	@Test
	public void testGetAllUsers_shouldReturnOnlyUsersActiveFiles() {
		User user1 = new User();
		User user2 = new User();
		UserFile user1ActiveFile = new UserFile();
		user1ActiveFile.setStatus(FileStatus.ACTIVE);
		UserFile user1BannedFile = new UserFile();
		user1BannedFile.setStatus(FileStatus.BANNED);
		UserFile user2DeletedFile = new UserFile();
		user2DeletedFile.setStatus(FileStatus.DELETED);
		UserFile user2BannedFile = new UserFile();
		user2BannedFile.setStatus(FileStatus.BANNED);
		List<UserFile> user1Files = Arrays.asList(user1ActiveFile, user1BannedFile);
		List<UserFile> user2Files = Arrays.asList(user2BannedFile, user2DeletedFile);
		user1.setFiles(user1Files);
		user2.setFiles(user2Files);
		List<User> allUsers = Arrays.asList(user1, user2);
		when(userRepository.getAll()).thenReturn(allUsers);
		int numberOfUser1Files = userService.getAllUsers().get(0).getFiles().size();
		int numberOfUser2Files = userService.getAllUsers().get(1).getFiles().size();
		assertEquals(1, numberOfUser1Files);
		assertEquals(0, numberOfUser2Files);
		assertEquals(FileStatus.ACTIVE, userService.getAllUsers().get(0).getFiles().get(0).getStatus());
	}
	
	@Test
	public void testGetAllUsers_shouldThrowExecption_whenThereAreNoUsersYet() {
		when(userRepository.getAll()).thenThrow(new ResourceNotFoundException("There are no users yet"));
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> userService.getAllUsers());
		assertEquals("There are no users yet", exception.getMessage());
	}
	
	@Test
	public void testGetUserById_shouldCallRepositoryGetByIdMethod() {
		User user = mock(User.class);
		when(userRepository.getById(1)).thenReturn(user);
		when(user.getFiles()).thenReturn(new ArrayList<UserFile>());
		userService.getUserById(1);
		verify(userRepository).getById(1);
	}
	
	@Test
	public void testGetUserById_shouldReturnTheSameUserAsRepositoryReturns() {
		User user = mock(User.class);
		when(userRepository.getById(1)).thenReturn(user);
		when(user.getFiles()).thenReturn(new ArrayList<UserFile>());
		assertEquals(user, userService.getUserById(1));
	}
	
	@Test
	public void testGetUserById_shouldReturnOnlyUsersActiveFiles() {
		User user = new User();
		UserFile activeFile = new UserFile();
		activeFile.setStatus(FileStatus.ACTIVE);
		UserFile bannedFile = new UserFile();
		bannedFile.setStatus(FileStatus.BANNED);
		UserFile deletedFile = new UserFile();
		deletedFile.setStatus(FileStatus.DELETED);
		List<UserFile> files = Arrays.asList(activeFile, bannedFile, deletedFile);
		user.setFiles(files);
		when(userRepository.getById(1)).thenReturn(user);
		int numberOfUsersFiles = userService.getUserById(1).getFiles().size();
		assertEquals(1, numberOfUsersFiles);
		assertEquals(FileStatus.ACTIVE, userService.getUserById(1).getFiles().get(0).getStatus());
	}
	
	@Test
	public void testGetUserById_shouldThrowException_whenThereIsNoUserWithSuchId() {
		when(userRepository.getById(0)).thenThrow(new ResourceNotFoundException("There is no user with such id"));
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(0));
		assertEquals("There is no user with such id", exception.getMessage());
	}
	
	@Test
	public void testDeleteUserById_shouldCallRepositoryDeleteByIdMethod() {
		User user = mock(User.class);
		when(userRepository.getById(1)).thenReturn(user);
		when(user.getFiles()).thenReturn(new ArrayList<UserFile>());
		userService.deleteUserById(1);
		verify(userRepository).deleteById(1);
	}
	
	@Test
	public void testDeleteUserById_shouldCallUserFileServiceDeleteUserFileByIdMethodAsManyTimesAsUserHasFiles() {
		User user = new User();
		UserFile file1 = new UserFile();
		file1.setStatus(FileStatus.ACTIVE);
		file1.setId(1);
		UserFile file2 = new UserFile();
		file2.setStatus(FileStatus.ACTIVE);
		file2.setId(2);
		UserFile file3 = new UserFile();
		file3.setStatus(FileStatus.ACTIVE);
		file3.setId(3);
		List<UserFile> files = Arrays.asList(file1, file2, file3);
		user.setFiles(files);
		when(userRepository.getById(1)).thenReturn(user);
		userService.deleteUserById(1);
		verify(userFileService, times(3)).deleteUserFileById(anyInt()); 
	}
	
	@Test
	public void testDeleteUserById_shouldThrowException_whenThereIsNoUserWithSuchId() {
		when(userRepository.getById(0)).thenThrow(new ResourceNotFoundException("There is no user with such id"));
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserById(0));
		assertEquals("There is no user with such id", exception.getMessage());
	}
}
