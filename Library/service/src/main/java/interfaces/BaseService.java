package interfaces;

import java.util.List;

import dto.BaseDto;

public interface BaseService <T extends BaseDto> {
	T getById(Integer id);
	List<T> getEntities();
	void update(T dto);
	Integer add(T dto);
	void delete(Integer id);
	int getCount();
}
