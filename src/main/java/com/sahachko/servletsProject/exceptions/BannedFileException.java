package com.sahachko.servletsProject.exceptions;

public class BannedFileException extends RuntimeException {
	public BannedFileException(String message) {
		super(message);
	}
}
