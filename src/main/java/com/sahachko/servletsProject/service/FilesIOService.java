package com.sahachko.servletsProject.service;

public interface FilesIOService {

	void writeUserFile(byte[] bytes, int userId, String fileName);

	void deleteUserFile(int userId, String fileName);
}
