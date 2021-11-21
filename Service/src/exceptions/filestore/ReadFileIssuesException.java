package exceptions.filestore;

import exceptions.ServiceException;

public class ReadFileIssuesException extends ServiceException {
	private static final long serialVersionUID = 8343646140443662813L;

	public String path;
	public String fileName;
	
	public ReadFileIssuesException(String path, String fileName, Throwable throwable) {
		super(String.format("There was some issues loading file %1$s%2$s", path, fileName), throwable);
		this.path = path;
		this.fileName = fileName;
	}
	
	@Override
	public String getCode() {
		String code = "read_file_issues";
		return code;
	}

	@Override
	public String classNameThrown() {
		String clazz = "FileStore";
		return clazz;
	}

}
