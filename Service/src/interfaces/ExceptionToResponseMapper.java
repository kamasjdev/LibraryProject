package interfaces;

import exceptions.ExceptionResponse;

public interface ExceptionToResponseMapper {
	ExceptionResponse map(Exception exception);
}
