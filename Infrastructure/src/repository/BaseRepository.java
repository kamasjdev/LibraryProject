package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRepository {
	
	protected List<List<Map<String,Object>>> getConnectedEntities(String entityTableName, List<List<Map<String, Object>>> dataFromDb) {
		List<List<Map<String,Object>>> entities = new ArrayList<List<Map<String,Object>>>();
		
		for(List<Map<String, Object>> fields : dataFromDb) {
			List<Map<String, Object>> entity = new ArrayList<Map<String,Object>>();
			
			for(Map<String, Object> field : fields) {
				String entityField = field.keySet().stream().filter(f-> f.contains(entityTableName)).findFirst().orElse(null);
				
				if(entityField != null) {
					Object fieldValue = field.get(entityField);
					Map<String, Object> entityFieldNameAndValue = new HashMap<String, Object>();
					entityFieldNameAndValue.put(entityField, fieldValue);
					entity.add(entityFieldNameAndValue);
				}
			}
			
			entities.add(entity);			
		}
		
		return entities;
	}
	
	protected List<Map<String,Object>> getConnectedEntity(String entityTableName, List<Map<String, Object>> fields) {
		List<Map<String, Object>> entity = new ArrayList<Map<String,Object>>();
		
		for(Map<String, Object> field : fields) {
			String entityField = field.keySet().stream().filter(f-> f.contains(entityTableName)).findFirst().orElse(null);
			
			if(entityField != null) {
				Object fieldValue = field.get(entityField);
				Map<String, Object> entityFieldNameAndValue = new HashMap<String, Object>();
				entityFieldNameAndValue.put(entityField, fieldValue);
				entity.add(entityFieldNameAndValue);
			}
		}	
		
		return entity;
	}
}
