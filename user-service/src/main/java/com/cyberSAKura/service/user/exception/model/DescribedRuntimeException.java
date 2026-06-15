package com.cyberSAKura.service.user.exception.model;

import lombok.Getter;

public class DescribedRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Getter private final String description;
	public DescribedRuntimeException(String description) {
		super();
		this.description = description;
	}
}
