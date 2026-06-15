package com.cyberSAKura.service.user.exception;

import com.cyberSAKura.service.user.exception.model.DescribedRuntimeException;

public class UsernameNotFoundException extends DescribedRuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6902450692785024564L;
	public UsernameNotFoundException() {
		super("USERNAME ENTRY WAS NOT FOUND IN THE DATABASE");
	}
}
