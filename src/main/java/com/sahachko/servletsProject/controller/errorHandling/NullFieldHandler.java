package com.sahachko.servletsProject.controller.errorHandling;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NullFieldHandler extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = (String) request.getAttribute("javax.servlet.error.message");
		String type =  request.getAttribute("javax.servlet.error.exception_type").toString().substring(6);
		response.setContentType("application/json");
		response.setStatus(422);
		PrintWriter out = response.getWriter();
		out.println("{");
		out.println("\"type\" : \"" + type + "\",");
		out.println("\"title\" : \"One of the object's filed which is supposed to be not null has null value\",");
		out.println("\"status\" : \"422\",");
		out.println("\"detail\" : \"" + message + "\",");
		out.println("\"help\" : \"Provide not null value for not nullable field in the next request\"");
		out.println("}");
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
