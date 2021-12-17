package interfaces;

import java.util.List;

import dto.BaseDto;
import dto.BillDto;

public interface BaseService <T extends BaseDto> {
	T getById(Integer id);
	List<T> getAll();
	void update(T dto);
	Integer add(T dto);
	void delete(Integer id);
}
