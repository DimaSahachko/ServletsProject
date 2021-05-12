package com.sahachko.servletsProject.exceptions;

@SuppressWarnings("serial")
public class BannedFileException extends RuntimeException {
	public BannedFileException(String message) {
		super(message);
	}
}
