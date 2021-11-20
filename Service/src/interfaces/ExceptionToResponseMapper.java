package interfaces;

import exceptions.ExceptionResponse;

public interface ExceptionToResponseMapper {
	ExceptionResponse Map(Exception exception);
}
