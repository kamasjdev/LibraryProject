package exceptions.filestore;

import exceptions.ServiceException;

public class FileNotFoundException extends ServiceException {
	private static final long serialVersionUID = -319342670418698561L;

	public String path;
	public String fileName;
	
	public FileNotFoundException(String path, String fileName, Throwable throwable) {
		super(String.format("File %1$s%2$s not found", path, fileName), throwable);
		this.path = path;
		this.fileName = fileName;
	}
	
	@Override
	public String getCode() {
		String code = "file_not_found";
		return code;
	}

	@Override
	public String classNameThrown() {
		String clazz = "FileStore";
		return clazz;
	}

	
}
