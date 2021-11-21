package interfaces;

import java.util.List;

import common.BaseEntity;

public interface BaseService <T extends BaseEntity> {
	T getById(Integer id);
	List<T> getEntities();
	void update(T entity);
	Integer add(T entity);
	Integer getLastId();
	void delete(Integer id);
}
