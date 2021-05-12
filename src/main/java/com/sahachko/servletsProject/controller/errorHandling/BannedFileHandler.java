package com.sahachko.servletsProject.controller.errorHandling;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class BannedFileHandler extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = (String) request.getAttribute("javax.servlet.error.message");
		String type =  request.getAttribute("javax.servlet.error.exception_type").toString().substring(6);
		String path = request.getServletPath();
		response.setContentType("application/json");
		response.setStatus(403);
		PrintWriter out = response.getWriter();
		out.println("{");
		out.println("\"type\" : \"" + type + "\",");
		out.println("\"title\" : \"" + path + "\",");
		out.println("\"status\" : \"403\",");
		out.println("\"detail\" : \"" + message + "\",");
		out.println("\"help\" : \"Unfortunately you can not access banned files\"");
		out.println("}");
	}

}
