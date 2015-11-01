package procurementsys.model.database;

import java.util.List;

import procurementsys.model.Order;

public interface OrderDAO {
	void add(Order order);
	List<Order> getAll();
	boolean isEmpty();


}
