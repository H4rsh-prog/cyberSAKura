package com.cyberSAKura.service.user.control;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cyberSAKura.service.user.exception.UsernameNotFoundException;
import com.cyberSAKura.service.user.exception.model.DescribedRuntimeException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
public class ExceptionController {
	private ObjectMapper mapper = new ObjectMapper();
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	public void logErr(DescribedRuntimeException ex) {
		log.severe("RUNTIME EXCEPTION : "+ex.getClass().getName()+" : "+ex.getDescription());
	}
	
	@ExceptionHandler(exception = UsernameNotFoundException.class)
	ResponseEntity<?> usernameNotFoundException(UsernameNotFoundException ex) {
		logErr(ex);
		record StackTraceRecord(String className, String fileName, long lineNumber, String methodName) {}
		ArrayList<StackTraceRecord> stackTrace = new ArrayList<>();
		for(StackTraceElement element : ex.getStackTrace()) {
			stackTrace.add(new StackTraceRecord(element.getClassName(), element.getFileName(), element.getLineNumber(), element.getMethodName()));
		}
		record response(String status, String message, ArrayList<StackTraceRecord> stackTrace) {}
		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(new response("failed", "No database entries found associated with the queried username", stackTrace));
	}
}
