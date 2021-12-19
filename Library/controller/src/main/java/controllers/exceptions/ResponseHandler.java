package controllers.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import exceptions.ExceptionResponse;
import exceptions.ServiceException;
import interfaces.ExceptionToResponseMapper;

@ControllerAdvice
public class ResponseHandler {
	private final ExceptionToResponseMapper exceptionToResponseMapper;
	private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);
	
	@Autowired
	public ResponseHandler(ExceptionToResponseMapper exceptionToResponseMapper) {
		this.exceptionToResponseMapper = exceptionToResponseMapper;
	}
	
	@ExceptionHandler(value = ServiceException.class)
    public ResponseEntity<Object> exception(ServiceException exception) {
		ExceptionResponse exceptionResponse = exceptionToResponseMapper.map(exception);
        return new ResponseEntity<>(exceptionResponse.toString(), HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> genericException(Exception exception) {		
		ExceptionResponse exceptionResponse = exceptionToResponseMapper.map(exception);
        return new ResponseEntity<>(exceptionResponse.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}