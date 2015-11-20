package procurementsys.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import procurementsys.model.Delivery;
import procurementsys.model.Order;
import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.view.SoftwareNotification;

public class MySQLOrderDAO implements OrderDAO {
	private Connection conn;
	
	public MySQLOrderDAO() {
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost/procurementdb";
			conn = DriverManager.getConnection(url, "root", "DLSU1234");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void add(Order order) {
		try {
			String addStr = "INSERT INTO orders(order_datetime) VALUES (?);";
			PreparedStatement addOrder = conn.prepareStatement(addStr);
			addOrder.setString(1, order.getOrderDateTime() + "");
			addOrder.execute();
			
			for (ProductOffer po : order.getProductsOffersOrdered()) {
				addStr = "INSERT INTO products_ordered(order_datetime, "
													+ "supplier_name, "
													+ "product_name, "
													+ "qty_ordered) "
													+ "VALUES (?,?,?,?);";
				PreparedStatement addProductOrdered = conn.prepareStatement(addStr);
				addProductOrdered.setString(1, order.getOrderDateTime() + "");
				addProductOrdered.setString(2, po.getSupplier().getName());
				addProductOrdered.setString(3, po.getProduct().getName());
				addProductOrdered.setInt(4, order.quantityOrdered(po));
				addProductOrdered.execute();
				
				SoftwareNotification.notifySuccess("The order has been succesfully "
						+ "added to the system");
				
			}
		
		} catch (SQLException e) {
			SoftwareNotification.notifyError("An identical order already exists in the system.");
		}
	}

	@Override
	public List<Order> getAll() {
		List<Order> ret = new ArrayList<>();
			
		try {
			String queryStr = "SELECT * FROM orders";
			PreparedStatement getAll = conn.prepareStatement(queryStr);
			ResultSet rs = getAll.executeQuery();
			
			List<LocalDateTime> dateTimes = new ArrayList<>();
			while (rs.next()) {
				dateTimes.add(rs.getTimestamp("order_datetime")
						.toLocalDateTime());
			}
			
			for (LocalDateTime dt : dateTimes) {
				ret.add(getOrder(dt));
			}
			
		} catch (SQLException e) {
			
		}
		
		return ret;
	}
	
	private Order getOrder(LocalDateTime dateTimeOrdered) {
		try {
			String queryStr = "SELECT PO.supplier_name, PO.product_name, "
							+ "qty_ordered FROM orders O "
							+ "JOIN products_ordered PO ON (O.order_datetime = PO.order_datetime) "
							+ "WHERE O.order_datetime = ?";
			
			PreparedStatement getOrderedProducts = conn.prepareStatement(queryStr);
			getOrderedProducts.setString(1, dateTimeOrdered.toString());
			ResultSet rs = getOrderedProducts.executeQuery();
			Supplier orderSupplier = null;
			Map<ProductOffer, Integer> orderedMap = new HashMap<>();
			while (rs.next()) {
				String supplierName = rs.getString("supplier_name");
				String productName = rs.getString("product_name");
				int quantity = rs.getInt("qty_ordered");
				
				SupplierDAO supplierDAO = new MySQLSupplierDAO();
				Supplier supplier = supplierDAO.get(supplierName);
				orderSupplier = supplier;
				
				ProductDAO productDAO = new MySQLProductDAO();
				Product product = productDAO.get(productName);
				
				ProductOfferDAO productOfferDAO = new MySQLProductOfferDAO();
				ProductOffer productOffer =  productOfferDAO.get(supplier, product);
				orderedMap.put(productOffer, quantity);
			}
		
			Order order = new Order(dateTimeOrdered, orderSupplier, orderedMap);
			
			return order;
			/* get deliveries
			queryStr = "SELECT D.delivery_datetime, D.supplier_name, "
					+ "D.product_name FROM orders O JOIN deliveries D ON "
					+ "(O.order_datetime = D.order_datetime)";
			
			PreparedStatement getDeliveries = conn.prepareStatement(queryStr);
			rs = getDeliveries.executeQuery();
			
			while (rs.next()) {
				LocalDateTime dateTime = rs.getTimestamp("delivery_datetime")
						.toLocalDateTime();
				
			}*/
			
		} catch (SQLException e) {
			
		}
		return null;
	}
	
	@Override
	public boolean isEmpty() {
		// TODO - DEVS implement this
		return getAll().size() == 0;
	}

	@Override
	public void addDelivery(Order order, Delivery delivery) {
		/* Code 1 Add Delivery implemented by Antonio Angeles. 
		try {
			//quantity, delivery_datetime, supplier_name, product_name, order_datetime
			String query = "INSERT INTO deliveries(quantity, delivery_datetime, supplier_name, product_name, order_datetime) VALUES(?,?,?,?,?);";
			PreparedStatement addDelivery =  conn.prepareStatement(query);
			//addDelivery.setInt(1, delivery.getQuantityDelivered());
			addDelivery.setString(2, delivery.getDeliveryDateTime() + "");
			addDelivery.setString(3, order.getSupplier().getName());
			addDelivery.setString(4, order.getDeliveries() + "");
			addDelivery.setString(5, order.getOrderDateTime() + "");
			addDelivery.execute();
		} catch (SQLException e) {
			SoftwareNotification.notifyError("Error adding delivery to database.");
		}
	
		*/
		
		//Code 2 Add Delivery implemented by Antonio Angeles. Same code as add order. Has a NullPointerException error I can't seem to fix
		try {
			for (ProductOffer po : delivery.getProductOffersDelivered()) {
				String query = "INSERT INTO deliveries(quantity, "
													+ "delivery_datetime, "
													+ "supplier_name, "
													+ "product_name), "
													+ "order_datetime) "
													+ "VALUES (?,?,?,?,?);";
				PreparedStatement addDelivery = conn.prepareStatement(query);
				addDelivery.setInt(1, delivery.getQuantityDelivered(po));
				addDelivery.setString(2, delivery.getDeliveryDateTime() + "");
				addDelivery.setString(3, order.getSupplier().getName());
				addDelivery.setString(4, delivery.getDeliveredProducts());
				addDelivery.setString(5, order.getOrderDateTime() + "");
				addDelivery.execute();
				SoftwareNotification.notifySuccess("The delivery has been succesfully"
						+ "added to the system");
				
			}
			
			} catch (SQLException e) {
			SoftwareNotification.notifyError("An identical delivery already exists in the system.");
			}
		
	}



}
