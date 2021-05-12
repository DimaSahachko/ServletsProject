package com.sahachko.servletsProject.service.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.sahachko.servletsProject.service.FilesIOService;

public class FilesIOServiceImplementation implements FilesIOService {
	
	public void writeUserFile(byte [] bytes, int userId, String fileName) {
		String root = getRoot();
		File userDirectory = new File(root + userId);
		if(!userDirectory.exists()) {
			userDirectory.mkdir();
		}
		File uploadFile = new File(userDirectory + "\\" + fileName);
		try (FileOutputStream fos = new FileOutputStream(uploadFile)) {
			fos.write(bytes);
		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}
	
	public void deleteUserFile(int userId, String fileName) {
		String root = getRoot();
		File fileOnDisk = new File(root + userId + "\\" + fileName);
		if(fileOnDisk.exists()) {
			fileOnDisk.delete();
		}		
	}
	
	private String getRoot() {
		Properties prop = new Properties();
		String root = null;
		try (FileInputStream fis = new FileInputStream("src\\main\\resources\\application.properties")) {
			prop.load(fis);
			root = prop.getProperty("root");
		} catch (IOException exc) {
			exc.printStackTrace();
		}
		return root;
	}
}
