package interfaces;

import java.util.List;
import java.util.Map;

import common.BaseEntity;

public interface MapEntity<T extends BaseEntity> {
	T Map(List<Map<String, Object>> fields);
}
