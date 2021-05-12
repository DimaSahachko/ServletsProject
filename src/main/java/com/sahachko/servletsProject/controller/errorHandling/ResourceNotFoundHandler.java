package com.sahachko.servletsProject.controller.errorHandling;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ResourceNotFoundHandler extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = (String) request.getAttribute("javax.servlet.error.message");
		String type =  request.getAttribute("javax.servlet.error.exception_type").toString().substring(6);
		response.setContentType("application/json");
		response.setStatus(404);
		PrintWriter out = response.getWriter();
		out.println("{");
		out.println("\"type\" : \"" + type + "\",");
		out.println("\"title\" : \"Resource can not be found\",");
		out.println("\"status\" : \"404\",");
		out.println("\"detail\" : \"" + message + "\",");
		out.println("\"help\" : \"Make sure to provide correct id via Id header\"");
		out.println("}");
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = (String) request.getAttribute("javax.servlet.error.message");
		String type =  request.getAttribute("javax.servlet.error.exception_type").toString().substring(6);
		response.setContentType("application/json");
		response.setStatus(404);
		PrintWriter out = response.getWriter();
		out.println("{");
		out.println("\"type\" : \"" + type + "\",");
		out.println("\"title\" : \"Resource can not be found\",");
		out.println("\"status\" : \"404\",");
		out.println("\"detail\" : \"" + message + "\",");
		out.println("\"help\" : \"Make sure to provide correct user id inside json data\"");
		out.println("}");	
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
