package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Bill;
import exceptions.repository.bill.BillCannotBeNullException;
import exceptions.repository.bill.BillIdCannotBeNullException;
import interfaces.BillRepository;
import interfaces.DbClient;
import interfaces.MapEntity;

public class BillRepositoryImpl extends BaseRepository implements BillRepository {
	private DbClient dbClient;
	private MapEntity<Bill> mapper;
	
	public BillRepositoryImpl(DbClient dbClient, MapEntity<Bill> mapper) {
		this.dbClient = dbClient;
		this.mapper = mapper;
	}
	
	@Override
	public Integer add(Bill entity) {
		if(entity == null) {
			throw new BillCannotBeNullException();
		}
		
		Integer id = dbClient.insert("INSERT INTO BILLS(cost, customer_id) VALUES(?, ?)", entity.cost, entity.customerId);
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		if(id == null) {
			throw new BillIdCannotBeNullException();
		}
		
		dbClient.delete("DELETE FROM BILLS WHERE id = ?", id);
	}

	@Override
	public void delete(Bill entity) {
		if(entity == null) {
			throw new BillCannotBeNullException();
		}
		
		delete(entity.id);
	}

	@Override
	public void update(Bill entity) {
		if(entity == null) {
			throw new BillCannotBeNullException();
		}
		
		if(entity.id == null) {
			throw new BillIdCannotBeNullException();
		}
		
		dbClient.update("UPDATE BILLS SET cost = ?, customer_id = ? WHERE id = ?", entity.cost, entity.customerId, entity.id);
	}

	@Override
	public Bill get(Integer id) {
		if(id == null) {
			throw new BillIdCannotBeNullException();
		}
		
		List<List<Map<String, Object>>> bills = dbClient.executeQuery("SELECT * FROM BILLS WHERE id = ?", id);
		Bill bill = null;
		
		if(bills.size() > 0) {
			List<Map<String, Object>> billsFields = bills.get(0);
			bill = mapper.Map(billsFields);
		}
		
		return bill;
	}

	@Override
	public List<Bill> getAll() {
		List<Bill> bills = new ArrayList<Bill>();
		List<List<Map<String, Object>>> billsFromDb = dbClient.executeQuery("SELECT * FROM BILLS");

		for(List<Map<String,Object>> bill : billsFromDb) {
			bills.add(mapper.Map(bill));
		}
		
		return bills;
	}

}
