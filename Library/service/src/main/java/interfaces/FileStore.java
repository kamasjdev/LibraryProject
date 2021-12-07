package interfaces;

public interface FileStore {
	void saveFile(String path, String fileName, String content);
	String loadFile(String path, String fileName);
}
