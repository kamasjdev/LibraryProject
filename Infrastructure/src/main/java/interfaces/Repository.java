package interfaces;

import java.util.List;

import common.BaseEntity;

public interface Repository<T extends BaseEntity> {
	Integer add(T entity);
	void delete(Integer id);
	void delete(T entity);
	void update(T entity);
	T get(Integer id);
	List<T> getAll();
}
