package com.sahachko.servletsProject.service.implementations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.endsWith;







import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sahachko.servletsProject.exceptions.BannedFileException;
import com.sahachko.servletsProject.exceptions.ResourceNotFoundException;
import com.sahachko.servletsProject.model.EventAction;
import com.sahachko.servletsProject.model.FileStatus;
import com.sahachko.servletsProject.model.UserFile;
import com.sahachko.servletsProject.repository.EventRepository;
import com.sahachko.servletsProject.repository.UserFileRepository;
import com.sahachko.servletsProject.repository.UserRepository;
import com.sahachko.servletsProject.service.FilesIOService;

@ExtendWith(MockitoExtension.class)
class UserFileServiceImplementationTest {
	
	@Mock
	private UserFileRepository fileRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private EventRepository eventRepository;
	
	@Mock
	private FilesIOService ioService;
	
	@InjectMocks
	private DummyUserFileServiceImplementation fileService;
	
	@Test
	public void testGetAllUsersFiles_shouldCallRepositoryGetAllMethod() {
		fileService.getAllUsersFiles();
		verify(fileRepository).getAll();
	}
	
	@Test
	public void testGetAllUsersFiles_shouldReturnOnlyActiveFiles() {
		UserFile file1 = new UserFile();
		file1.setStatus(FileStatus.ACTIVE);
		UserFile file2 = new UserFile();
		file2.setStatus(FileStatus.ACTIVE);
		UserFile file3 = new UserFile();
		file3.setStatus(FileStatus.BANNED);
		UserFile file4 = new UserFile();
		file4.setStatus(FileStatus.DELETED);
		List<UserFile> files = Arrays.asList(file1, file2, file3, file4);
		when(fileRepository.getAll()).thenReturn(files);
		List<UserFile> fetchedFiles = fileService.getAllUsersFiles();
		assertEquals(2, fetchedFiles.size());
		assertEquals(FileStatus.ACTIVE, fetchedFiles.get(0).getStatus());
		assertEquals(FileStatus.ACTIVE, fetchedFiles.get(1).getStatus());
	}
	
	@Test
	public void testGetUserFileById_shouldCallRepositoryGetByIdMethod() {
		UserFile file = mock(UserFile.class);
		when(fileRepository.getById(1)).thenReturn(file);
		fileService.getUserFileById(1);
		verify(fileRepository).getById(1);
	}
	
	@Test
	public void testGetUserFileById_shouldReturnTheSameFileAsRepositoryReturns() {
		UserFile file = mock(UserFile.class);
		when(fileRepository.getById(1)).thenReturn(file);
		assertEquals(file, fileService.getUserFileById(1));
	}
	
	@Test
	public void testGetUserFileById_shouldThrowException_whenFileIsBanned() {
		UserFile file = new UserFile();
		file.setStatus(FileStatus.BANNED);
		when(fileRepository.getById(1)).thenReturn(file);
		Exception exception = assertThrows(BannedFileException.class, () -> fileService.getUserFileById(1));
		assertEquals("This file is banned", exception.getMessage());
	}
	
	@Test
	public void testGetUserFileById_shouldThrowException_whenFileIsDeleted() {
		UserFile file = new UserFile();
		file.setStatus(FileStatus.DELETED);
		when(fileRepository.getById(1)).thenReturn(file);
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> fileService.getUserFileById(1));
		assertEquals("This file has been removed", exception.getMessage());
	}
	
	@Test
	public void testGetUserFileById_shouldThrowException_whenThereIsNoFileWithSuchId() {
		when(fileRepository.getById(0)).thenThrow(new ResourceNotFoundException("There is no file with such id"));
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> fileService.getUserFileById(0));
		assertEquals("There is no file with such id", exception.getMessage());
	}
	
	@Test
	public void testSaveUserFile_shouldCallUserRepositoryGetByIdAndSaveMethods() {
		UserFile file = new UserFile();
		file.setUserId(101);
		when(fileRepository.save(file)).thenReturn(file);
		fileService.saveUserFile(file, new byte[] {1,  2, 3}, "testFile.pdf");
		verify(userRepository).getById(101);
		verify(fileRepository).save(file);
	}
	
	@Test
	public void testSaveUserFile_shouldCallFilesIOServiceWriteUserFileMethod() {
		UserFile file = new UserFile();
		file.setUserId(101);
		byte[] bytes = {1, 2, 3};
		when(fileRepository.save(file)).thenReturn(file);
		fileService.saveUserFile(file, bytes, "testFile.pdf");
		verify(ioService).writeUserFile(eq(bytes), eq(101), endsWith("testFile.pdf"));
	}
	
	@Test
	public void testSaveUserFile_shouldReturnTheSameFileAsUserFileRepositoryReturns() {
		UserFile file = new UserFile();
		file.setUserId(101);
		when(fileRepository.save(file)).thenReturn(file);
		assertEquals(file, fileService.saveUserFile(file, new byte[] {1,  2, 3}, "testFile.pdf"));
	}
	
	@Test
	public void testSaveUserFile_shouldAssignFileObjectFields() {
		UserFile file = new UserFile();
		file.setUserId(101);
		when(fileRepository.save(file)).thenReturn(file);
		file = fileService.saveUserFile(file, new byte[] {1, 2, 3}, "testFile.pdf");
		assertEquals(FileStatus.ACTIVE, file.getStatus());
		assertTrue(file.getName().endsWith("testFile.pdf"));
	}
	
	@Test
	public void testSaveUserFile_shouldThrowException_whenThereIsNoUserWithSuchId() {
		UserFile file = new UserFile();
		file.setUserId(101);
		when(userRepository.getById(101)).thenThrow(new ResourceNotFoundException("There is no user with such id"));
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> fileService.saveUserFile(file, new byte[] {1,  2, 3}, "testFile.pdf"));
		assertEquals("There is no user with such id", exception.getMessage());
	}
	
	@Test
	public void testUpdateUserFile_shouldCallRepositoryUpdateAndGetByIdMethods() {
		UserFile file = mock(UserFile.class);
		when(file.getId()).thenReturn(1);
		when(fileRepository.update(file)).thenReturn(file);
		when(fileRepository.getById(1)).thenReturn(file);
		fileService.updateUserFile(file);
		verify(fileRepository).update(file);
		verify(fileRepository).getById(1);
	}
	
	@Test
	public void testUpdateFile_shouldReturnTheSameUserFileAsRepositoryReturns() {
		UserFile file = mock(UserFile.class);
		when(file.getId()).thenReturn(1);
		when(fileRepository.update(file)).thenReturn(file);
		when(fileRepository.getById(1)).thenReturn(file);
		assertEquals(file, fileService.updateUserFile(file));
	}
	
	@Test
	public void testUpdateFile_shouldThrowException_whenThereIsNoUserFileWithSuchId() {
		UserFile file = mock(UserFile.class);
		when(fileRepository.update(file)).thenThrow(new ResourceNotFoundException("There is no file with such id"));
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> fileService.updateUserFile(file));
		assertEquals("There is no file with such id", exception.getMessage());
	}
	
	@Test
	public void testDeleteUserFileById_shouldCallRepositoryUpdateMethod() {
		UserFile file = new UserFile("testFile.pdf", 101, FileStatus.ACTIVE);
		file.setId(1);
		when(fileRepository.getById(1)).thenReturn(file);
		fileService.deleteUserFileById(1);
		verify(fileRepository).update(file);
	}
	
	@Test
	public void testDeleteUserFileById_shouldCallFilesIOServiceDeleteUserFileMethod() {
		UserFile file = new UserFile("testFile.pdf", 101, FileStatus.ACTIVE);
		file.setId(1);
		when(fileRepository.getById(1)).thenReturn(file);
		fileService.deleteUserFileById(1);
		verify(ioService).deleteUserFile(101, "testFile.pdf");
	}
	
	@Test
	public void testDeleteUserFile_shouldSetDeletedStatusToFile() {
		UserFile file = new UserFile("testFile.pdf", 101, FileStatus.ACTIVE);
		file.setId(1);
		when(fileRepository.getById(1)).thenReturn(file);
		ArgumentCaptor<UserFile> captor = ArgumentCaptor.forClass(UserFile.class);
		fileService.deleteUserFileById(1);
		verify(fileRepository).update(captor.capture());
		UserFile capturedFile = captor.getValue();
		assertEquals(FileStatus.DELETED, capturedFile.getStatus());
	}
	
	private static class DummyUserFileServiceImplementation extends UserFileServiceImplementation {
	
		public DummyUserFileServiceImplementation (UserFileRepository userFileRepository, UserRepository userRepository, EventRepository eventRepository, FilesIOService ioService) {
			super(userFileRepository,  userRepository, eventRepository,  ioService);
		}
		
		@Override
		void passInformationAboutOperationWithFileToUser(UserFile file, EventAction action) {
			//doNothing
		}
	}
}
