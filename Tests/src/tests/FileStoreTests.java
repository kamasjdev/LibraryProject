package tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;

import exceptions.filestore.InvalidFileNameException;
import exceptions.filestore.InvalidPathException;
import interfaces.FileStore;
import services.FileStoreImpl;

public class FileStoreTests {
	private FileStore fileStore;
	
	public FileStoreTests() {
		fileStore = new FileStoreImpl();
	}
	
	@Test
	public void given_invalid_path_when_save_file_should_throw_an_exception() {
		String path = null;
		String fileName = "name";
		String content = "{\"abc\": 123}";
		InvalidPathException expectedException = new InvalidPathException(path);
		
		InvalidPathException thrown = (InvalidPathException) catchThrowable(() -> fileStore.saveFile(path, fileName, content));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
	}
	
	@Test
	public void given_invalid_file_name_when_save_file_should_throw_an_exception() {
		String path = "path";
		String fileName = "";
		String content = "{\"abc\": 123}";
		InvalidFileNameException expectedException = new InvalidFileNameException(fileName);
		
		InvalidFileNameException thrown = (InvalidFileNameException) catchThrowable(() -> fileStore.saveFile(path, fileName, content));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
	}
	
	@Test
	public void given_invalid_path_when_load_file_should_throw_an_exception() {
		String path = null;
		String fileName = "name";
		InvalidPathException expectedException = new InvalidPathException(path);
		
		InvalidPathException thrown = (InvalidPathException) catchThrowable(() -> fileStore.loadFile(path, fileName));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
	}
	
	@Test
	public void given_invalid_file_name_when_load_file_should_throw_an_exception() {
		String path = "path";
		String fileName = "";
		InvalidFileNameException expectedException = new InvalidFileNameException(fileName);
		
		InvalidFileNameException thrown = (InvalidFileNameException) catchThrowable(() -> fileStore.loadFile(path, fileName));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
	}
}
