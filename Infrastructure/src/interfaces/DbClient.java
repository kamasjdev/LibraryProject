package interfaces;

import java.util.List;
import java.util.Map;

public interface DbClient {
	List<List<Map<String, Object>>> executeQuery(String query);
	List<List<Map<String, Object>>> executeQuery(String query, Object... params);
	List<List<Map<String, Object>>> executeQuery(String query, Map<String, Object> params);
	Integer insert(String query, Object... params);
	void update(String query, Object... params);
	void delete(String query, Object... params);
}
