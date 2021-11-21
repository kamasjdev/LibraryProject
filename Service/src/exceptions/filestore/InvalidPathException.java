package exceptions.filestore;

import exceptions.ServiceException;

public class InvalidPathException extends ServiceException {
	private static final long serialVersionUID = 4711391482230051429L;

	public InvalidPathException() {
		super("Invalid path");
	}
	
	@Override
	public String getCode() {
		String code = "invalid_path";
		return code;
	}

	@Override
	public String classNameThrown() {
		String clazz = "FileStore";
		return clazz;
	}
}
