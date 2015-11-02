package procurementsys.model.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import procurementsys.model.CostChange;
import procurementsys.model.Delivery;
import procurementsys.model.Order;
import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.view.SoftwareNotification;

public class MySQLOrderDAO implements OrderDAO {

	@Override
	public void add(Order order) {
		// TODO - DEVS implement this
		String query = 
//				String.format("");
//		try{
//			PreparedStatement getProduct = conn.prepareStatement(query); 
//		    ResultSet rs = getProduct.executeQuery(query);
//		    while(rs.next()){
//		    	   
//		}
//		}
//		catch(SQLException e){
//			SoftwareNotification.notifyError("Error in the Order database: " + e.getMessage());
//		}
	}

	@Override
	public List<Order> getAll() {
//		String query = 
//				String.format("");
//		try{
//			PreparedStatement getProduct = conn.prepareStatement(query); 
//		    ResultSet rs = getProduct.executeQuery(query);
//		    while(rs.next()){
//		    	   
//		}
//		}
//		catch(SQLException e){
//			SoftwareNotification.notifyError("Error in the Order database: " + e.getMessage());
//		}
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

	@Override
	public void addDelivery(Order order, Delivery delivery) {
//		try {
//			String query = "INSERT INTO products(product_name) VALUES(?)";
//			PreparedStatement addProduct =  conn.prepareStatement(addStr);
//		
//			addProduct.execute();
//		} catch (SQLException e) {
//			SoftwareNotification.notifyError("error in add delivery.");
//		}
		// TODO - DEVS implement this
		
	}



}
