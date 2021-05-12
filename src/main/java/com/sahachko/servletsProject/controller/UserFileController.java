package com.sahachko.servletsProject.controller;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sahachko.servletsProject.exceptions.*;
import com.sahachko.servletsProject.model.*;
import com.sahachko.servletsProject.repository.hibernate.*;
import com.sahachko.servletsProject.service.*;
import com.sahachko.servletsProject.service.implementations.*;

@SuppressWarnings("serial")
public class UserFileController extends HttpServlet {
	Gson json;
	UserFileService userFileService;
	
	public UserFileController() {
		this.json = new GsonBuilder().setDateFormat("yyyy-MM-dd 'at' HH:mm:ss").create();
		this.userFileService = new UserFileServiceImplementation(new HibernateUserFileRepository(), new HibernateUserRepository(), new HibernateEventRepository(), new FilesIOServiceImplementation());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		if(uri.equals("/servletsProject/files")) {
			getAll(request, response);
		} else {
			getById(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userIdHeader = request.getHeader("Id");
		String fileName = request.getHeader("Filename");
		if (fileName == null || userIdHeader == null) {
			throw new IncorrectHeaderException("Required \"Filename\" or \"Id\" header wasn't sent along with request");
		}
		int userId = Integer.parseInt(userIdHeader);
		UserFile file = new UserFile();
		file.setUserId(userId);
		int contentLength = request.getContentLength();
		byte[] bytes = new byte[contentLength];
		int currentPosition = 0;
		int bytesRead = 0;
		InputStream is = request.getInputStream();
		while ((bytesRead = is.read()) != -1) {
			bytes[currentPosition] = (byte) bytesRead;
			currentPosition++;
		}
		file = userFileService.saveUserFile(file, bytes, fileName);
		response.setStatus(201);
		response.setContentType("application/json");
		response.getWriter().print(json.toJson(file));
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder requestBody = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line; 
		while ((line = reader.readLine()) != null) {
			requestBody.append(line);
			requestBody.append(System.lineSeparator());
		}
		UserFile file = json.fromJson(requestBody.toString(), UserFile.class);
		if (file.getId() == null || file.getName() == null || file.getUserId() == null ||file.getCreated() == null || file.getStatus() == null) {  
			throw new NullFieldException("Field in an UserFile object which must be assigned has null value");
		}
		file = userFileService.updateUserFile(file);
		response.setStatus(200);
		response.setContentType("application/json");
		response.getWriter().print(json.toJson(file));
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int fileId = Integer.parseInt(request.getHeader("Id"));
		userFileService.deleteUserFileById(fileId);
		response.setStatus(204);
		
	}
	
	private void getAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<UserFile> files = userFileService.getAllUsersFiles();
		String filesInJsonFormat = json.toJson(files);
		response.setStatus(200);
		response.setContentType("application/json");
		response.getWriter().print(filesInJsonFormat);
	}
	
	private void getById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userFileId = Integer.parseInt(request.getHeader("Id"));
		UserFile file = userFileService.getUserFileById(userFileId);
		response.setStatus(200);
		response.setContentType("application/json");
		response.getWriter().print(json.toJson(file));
	}
}
