package abstracts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import common.BaseEntity;
import exceptions.baseservice.CannotDeserializeObjectsException;
import exceptions.baseservice.CannotSerializeObjectsException;
import interfaces.BaseService;

public abstract class AbstractBaseService<T extends BaseEntity> implements BaseService<T> {
	protected List<T> objects;
	
	public AbstractBaseService() {
		objects = new ArrayList<T>();
	}
	
	public abstract T getById(Integer id);
	public abstract List<T> getEntities();
	public abstract void update(T entity);
	public abstract Integer add(T entity);
	public abstract void delete(Integer id);
	
	public Integer getLastId() {
		if(objects.size() == 0) {
			return 1;
		}
		
		List<T> listSorted = objects.stream().sorted((o1, o2) -> Integer.compare(o2.id, o1.id)).collect(Collectors.toList());
		Integer lastId = listSorted.get(0).id;
		return lastId+1;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void deserializeObjects(String jsonString) {
		if(jsonString.isEmpty()) {
			return;
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<T> listObject;
		try {
			listObject = objectMapper.readValue(jsonString, List.class);
		} catch (JsonProcessingException e) {
			@SuppressWarnings("rawtypes")
			Class<? extends List> clazz = objects.getClass();
			throw new CannotDeserializeObjectsException(clazz, e);
		}
		
		objects = listObject; 
	}
	
	public String serializeObjects() {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString;
		try {
			jsonString = objectMapper.writeValueAsString(objects);
		} catch (JsonProcessingException e) {
			@SuppressWarnings("rawtypes")
			Class<? extends List> clazz = objects.getClass();
			throw new CannotSerializeObjectsException(clazz, e);
		}
		return jsonString;
	}
}
