package com.sahachko.servletsProject.controller.errorHandling;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BadRequestHandler extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = (String) request.getAttribute("javax.servlet.error.message");
		String type =  request.getAttribute("javax.servlet.error.exception_type").toString().substring(6);
		String path = request.getServletPath();
		response.setContentType("application/json");
		response.setStatus(400);
		PrintWriter out = response.getWriter();
		out.println("{");
		out.println("\"type\" : \"" + type + "\",");
		out.println("\"title\" : \"" + path + "\",");
		out.println("\"status\" : \"400\",");
		out.println("\"detail\" : \"" + message + "\",");
		out.println("\"help\" : \"Make sure to provide correct data in your next request\"");
		out.println("}");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
