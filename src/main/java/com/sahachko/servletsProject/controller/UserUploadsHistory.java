package com.sahachko.servletsProject.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sahachko.servletsProject.exceptions.ResourceNotFoundException;
import com.sahachko.servletsProject.model.User;
import com.sahachko.servletsProject.model.UserFile;
import com.sahachko.servletsProject.repository.hibernate.HibernateUserRepository;
import com.sahachko.servletsProject.service.UserService;
import com.sahachko.servletsProject.service.implementations.UserServiceImplementation;

public class UserUploadsHistory extends HttpServlet {
	private UserService service;
	private Gson json;
	
	public UserUploadsHistory() {
		this.service = new UserServiceImplementation(new HibernateUserRepository());
		this.json = new GsonBuilder().setDateFormat("yyyy-MM-dd 'at' HH:mm:ss").create();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = Integer.parseInt(request.getHeader("Id"));
		User user = service.getUserById(userId);
		if (user == null) {
			throw new ResourceNotFoundException("There is no user with such id");
		}
		List<UserFile> userFiles = user.getFiles();
		response.setStatus(200);
		response.setContentType("application/json");
		response.getWriter().print(json.toJson(userFiles));
	}

}
