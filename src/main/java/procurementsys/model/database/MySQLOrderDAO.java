package procurementsys.model.database;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import procurementsys.model.CostChange;
import procurementsys.model.Order;
import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;

public class MySQLOrderDAO implements OrderDAO {

	@Override
	public void add(Order order) {
		// TODO - DEVS implement this
		
	}

	@Override
	public List<Order> getAll() {
		// TODO - DEVS implement this
		List<Order> ret = new ArrayList<>();
		
		SupplierDAO supplierDAO = new MySQLSupplierDAO();
		for (Supplier supplier : supplierDAO.getAll()) {
			LocalDateTime dateTime = LocalDateTime.now();
			
			List<CostChange> costChanges = new ArrayList<>();
			costChanges.add(new CostChange(LocalDateTime.now().minusMonths(1), 10));
			costChanges.add(new CostChange(LocalDateTime.now().minusMonths(1), 15));
			costChanges.add(new CostChange(LocalDateTime.now().minusMonths(1), 30));
			costChanges.add(new CostChange(LocalDateTime.now(), 20));
			costChanges.add(new CostChange(LocalDateTime.now().plusMonths(2), 25));
			
			ProductOffer po1 = new ProductOffer(new Product("Iphone 16GB"),
					supplier, costChanges);
			ProductOffer po2 = new ProductOffer(new Product("Hugo Boss Glasses"),
					supplier, costChanges);
			ProductOffer po3 = new ProductOffer(new Product("Wilkins Mineral Water"),
					supplier, costChanges);
			ProductOffer po4 = new ProductOffer(new Product("Elmers Glue"),
					supplier, costChanges);
			Map<ProductOffer, Integer> map = new HashMap<>();
			
			map.put(po1, 35);
			map.put(po2, 5000);
			map.put(po3, 350);
			map.put(po4, 1200);
			
			ret.add(new Order(dateTime, supplier, map));
		}
		
		return ret;
	}
	
	@Override
	public boolean isEmpty() {
		// TODO - DEVS implement this
		return false;
	}



}
