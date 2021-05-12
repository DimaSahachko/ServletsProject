package com.sahachko.servletsProject.exceptions;

@SuppressWarnings("serial")
public class IncorrectHeaderException extends RuntimeException {
	public IncorrectHeaderException(String message) {
		super(message);
	}
}
