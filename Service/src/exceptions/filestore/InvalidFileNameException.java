package exceptions.filestore;

import exceptions.ServiceException;

public class InvalidFileNameException extends ServiceException {
	private static final long serialVersionUID = -2231596269972127351L;

	public InvalidFileNameException() {
		super("Invalid file Name");
	}
	
	@Override
	public String getCode() {
		String code = "invalid_file_name";
		return code;
	}

	@Override
	public String classNameThrown() {
		String clazz = "FileStore";
		return clazz;
	}
}
