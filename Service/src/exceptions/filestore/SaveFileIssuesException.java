package exceptions.filestore;

import exceptions.ServiceException;

public class SaveFileIssuesException extends ServiceException {
	private static final long serialVersionUID = -4412500620804369526L;

	public String path;
	public String fileName;
	
	public SaveFileIssuesException(String path, String fileName) {
		super(String.format("There was some issues saving file %1$s%2$s", path, fileName));
		this.path = path;
		this.fileName = fileName;
	}
	
	public SaveFileIssuesException(String path, String fileName, Throwable throwable) {
		super(String.format("There was some issues saving file %1$s%2$s", path, fileName), throwable);
		this.path = path;
		this.fileName = fileName;
	}
	
	@Override
	public String getCode() {
		String code = "save_file_issues";
		return code;
	}

	@Override
	public String classNameThrown() {
		String clazz = "FileStore";
		return clazz;
	}

}
