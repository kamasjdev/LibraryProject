package exceptions;

import interfaces.ExceptionToResponseMapper;

public class ExceptionToResponseMapperImpl implements ExceptionToResponseMapper {

	@Override
	public ExceptionResponse map(Exception exception) {
		ExceptionResponse response = null;
		
		if(ServiceException.class.isAssignableFrom(exception.getClass())) {
			String message = ((ServiceException) exception).getMessage();
			String code = ((ServiceException) exception).getCode();
			String clazzThrownName = ((ServiceException) exception).classNameThrown();
			response = new ExceptionResponse(message, code, clazzThrownName);
		} else if(java.util.InputMismatchException.class.isAssignableFrom(exception.getClass())) {
			String message = new StringBuilder("Ivalid value entered. ").append(exception.getMessage()).toString();
			String code = "invalid_value_entered";
			String clazzThrownName = "java.util.InputMismatchException";
			response = new ExceptionResponse(message, code, clazzThrownName);
		} else if(java.sql.SQLException.class.isAssignableFrom(exception.getClass())) {
			String message = ((java.sql.SQLException) exception).getMessage();
			String code = "sql_exception";
			String clazzThrownName = "com.mysql.cj.jdbc";
			response = new ExceptionResponse(message, code, clazzThrownName);
		} else {
			String message = "Something bad happen";
			String code = "something_bad_happen";
			String clazzThrownName = "";
			response = new ExceptionResponse(message, code, clazzThrownName);
		}
		
		return response;
	}

}
