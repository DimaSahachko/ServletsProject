package com.sahachko.servletsProject.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sahachko.servletsProject.exceptions.NullFieldException;
import com.sahachko.servletsProject.model.User;
import com.sahachko.servletsProject.repository.hibernate.HibernateEventRepository;
import com.sahachko.servletsProject.repository.hibernate.HibernateUserFileRepository;
import com.sahachko.servletsProject.repository.hibernate.HibernateUserRepository;
import com.sahachko.servletsProject.service.UserService;
import com.sahachko.servletsProject.service.implementations.FilesIOServiceImplementation;
import com.sahachko.servletsProject.service.implementations.UserFileServiceImplementation;
import com.sahachko.servletsProject.service.implementations.UserServiceImplementation;

@SuppressWarnings("serial")
public class UserController extends HttpServlet {
	private UserService service;
	private Gson json;

	public UserController() {
		this.service = new UserServiceImplementation(new HibernateUserRepository(), new UserFileServiceImplementation(new HibernateUserFileRepository(), new HibernateUserRepository(), new HibernateEventRepository(), new FilesIOServiceImplementation()));
		this.json = new GsonBuilder().setDateFormat("yyyy-MM-dd 'at' HH:mm:ss").create();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		if(uri.equals("/servletsProject/users")) {
			getAll(request, response);
		} else {
			getById(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder requestBody = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			requestBody.append(line);
			requestBody.append(System.lineSeparator());
		}
		User user = json.fromJson(requestBody.toString(), User.class);
		if (user.getLogin() == null || user.getAccount() == null || (user.getFiles() != null && user.getFiles().size() > 0) || (user.getEvents() != null && user.getEvents().size() > 0)) {
			throw new NullFieldException("Field in an user object which must be assigned has null value or vice versa");
		}
		user = service.saveUser(user);
		response.setStatus(201);
		response.setContentType("application/json");
		response.getWriter().print(json.toJson(user));
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder requestBody = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line; 
		while ((line = reader.readLine()) != null) {
			requestBody.append(line);
			requestBody.append(System.lineSeparator());
		}
		User user = json.fromJson(requestBody.toString(), User.class);
		if(user.getId() == null || user.getLogin() == null || user.getAccount() == null) {
			throw new NullFieldException("Field in an user object which must be assigned has null value");
		}
		service.updateUser(user);
		user = service.getUserById(user.getId());
		response.setStatus(200);
		response.setContentType("application/json");
		response.getWriter().print(json.toJson(user));
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = Integer.parseInt(request.getHeader("Id"));
		service.deleteUserById(userId);
		response.setStatus(204);
	}
	
	private void getAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<User> users = service.getAllUsers();
		String usersInJsonFormat = json.toJson(users);
		response.setStatus(200);
		response.setContentType("application/json");
		response.getWriter().print(usersInJsonFormat);
	}
	
	private void getById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = Integer.parseInt(request.getHeader("Id"));
		User user = service.getUserById(userId);
		response.setStatus(200);
		response.setContentType("application/json");
		response.getWriter().print(json.toJson(user));
	}
	

}
