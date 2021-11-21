package services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import exceptions.filestore.InvalidFileNameException;
import exceptions.filestore.InvalidPathException;
import exceptions.filestore.ReadFileIssuesException;
import exceptions.filestore.SaveFileIssuesException;
import interfaces.FileStore;

public class FileStoreImpl implements FileStore {

	@Override
	public void saveFile(String path, String fileName, String content) {
		if(content == null) {
			throw new SaveFileIssuesException(path, fileName);
		}
		
		if(content.isEmpty() || content.equals("[]")) {
			return;
		}
		
		validParameters(path, fileName);
		
		File file = new File(path + fileName);
		
		try(FileWriter fileWriter = new FileWriter(file)) {
			fileWriter.write(content);
			fileWriter.close();
		} catch (IOException e) {
			throw new SaveFileIssuesException(path, fileName, e);
		}
	}

	@Override
	public String loadFile(String path, String fileName) {
		validParameters(path, fileName);
		
		File file = new File(path + fileName);
		String content;
		
		try(FileReader fileReader = new FileReader(file)) {
			int length = (int) file.length();
			char[] buffer = new char[length];
		    int charactersRead = fileReader.read(buffer, 0, length);
		    if (charactersRead != -1) {
		        content = new String(buffer, 0, charactersRead);
		    } else {
		        content = "";
		    }
		} catch (FileNotFoundException e) {
			//throw new exceptions.filestore.FileNotFoundException(path, fileName, e);
			return "";
		} catch (IOException e) {
			throw new ReadFileIssuesException(path, fileName, e);
		}
		return content;
	}
	
	private void validParameters(String path, String fileName) {
		if(path == null) {
			throw new InvalidPathException();
		}
		
		if(path.isEmpty()) {
			throw new InvalidPathException();			
		}
		
		if(fileName == null) {
			throw new InvalidFileNameException();
		}
		
		if(fileName.isEmpty()) {
			throw new InvalidFileNameException();			
		}
	}
}
