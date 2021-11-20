package interfaces;

import java.util.List;

import common.BaseEntity;

public interface BaseService <T extends BaseEntity> {
	T GetById(Integer id);
	List<T> GetEntities();
	void Update(T entity);
	Integer Add(T entity);
	Integer GetLastId();
	void Delete(Integer id);
}
