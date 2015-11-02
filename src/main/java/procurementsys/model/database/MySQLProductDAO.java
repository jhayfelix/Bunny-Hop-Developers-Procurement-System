package procurementsys.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import procurementsys.model.Product;
import procurementsys.model.Supplier;
import procurementsys.model.Tag;
import procurementsys.view.SoftwareNotification;

public class MySQLProductDAO implements ProductDAO {
	private Connection conn;
	
	public MySQLProductDAO() {
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
	public void add(Product product) { // Implemented by Jan Tristan Milan

		try {
			String addStr = "INSERT INTO products(product_name) VALUES(?)";
			PreparedStatement addProduct =  conn.prepareStatement(addStr);
			addProduct.setString(1, product.getName());
			addProduct.execute();
		} catch (SQLException e) {
			SoftwareNotification.notifyError("The product '" + product.getName()
					+ "' already exists in the database.");
		}
		
		
	}

	@Override
	public Product get(String productNameFilter) {
		// TODO - DEVS implement this
		return new Product(productNameFilter);
	}
	
	@Override
	public List<Product> getAll() {//implement by Dominic Pagtalunan
		
		List<Product> ret = new ArrayList<>();
		String query = "SELECT * FROM products";
	 
	    
		try{
			//st = conn.createStatement(); 
			PreparedStatement getProduct = conn.prepareStatement(query); 
		    ResultSet rs = getProduct.executeQuery(query);
		       while(rs.next()){
		    	   String productName=rs.getString("product_name");
		    	   System.out.format("%s \n",productName);
		    	   ret.add(new Product(productName));	
		}}
		catch(SQLException e){
			SoftwareNotification.notifyError("Error in the Product database: " + e.getMessage());
		}
		
		/*
		ret.add(new Product("Red Ballpen"));
		ret.add(new Product("Blue Ballpen"));
		ret.add(new Product("Green Ballpen"));
		ret.add(new Product("Black Ballpen"));
		
		ret.add(new Product("Sola (Orange)"));
		ret.add(new Product("Sola (Apple)"));
		ret.add(new Product("Sola (Lemon)"));
		ret.add(new Product("Sola (Grape)"));
		ret.add(new Product("Sola (Raspberry)"));
		
		ret.add(new Product("Minute Maid Pulpy Orange"));
		ret.add(new Product("Zesto Orange Juice Drink"));
		*/
		return ret;
	}
	

	@Override
	public List<Product> getAll(String productNameFilter) {
		// TODO - DEVS implement this
		List<Product> ret = new ArrayList<>();
		String query = 
				String.format(   "select * from products "
						+ "WHERE LOWER(REPLACE(product_name, ' ', '')) = "
						+ "LOWER(REPLACE(\"%s\", ' ', ''))",productNameFilter);
		
	   
	    try{
			PreparedStatement getProduct = conn.prepareStatement(query); 
		    ResultSet rs = getProduct.executeQuery(query);
		       while(rs.next()){
		    	   String productName=rs.getString("product_name");
		    	   System.out.format("%s \n",productName);
		    	   ret.add(new Product(productName));	
		}}
	    catch(SQLException e){
			SoftwareNotification.notifyError("Error in the Product database: " + e.getMessage());
		}
		/*
		ret.add(new Product("Red Ballpen"));
		ret.add(new Product("Blue Ballpen"));
		ret.add(new Product("Green Ballpen"));
		ret.add(new Product("Black Ballpen"));
		
		ret.add(new Product("Sola (Orange)"));
		ret.add(new Product("Sola (Apple)"));
		ret.add(new Product("Sola (Lemon)"));
		ret.add(new Product("Sola (Grape)"));
		ret.add(new Product("Sola (Raspberry)"));
			
		ret.add(new Product("Minute Maid Pulpy Orange"));
		ret.add(new Product("Zesto Orange Juice Drink"));
*/
		List<Product> filteredRet = new ArrayList<>();
		for (int i = 0; i < ret.size(); i++) {
			Product x = ret.get(i);
			if (x.getName().toLowerCase().contains(productNameFilter.toLowerCase())) {
				filteredRet.add(x);
			}
		}
		return filteredRet;
	}

	@Override
	public List<Product> getAll(List<Tag> tags, String productNameFilter) {//implemented by DOminic Pagtalunan
		
		List<Product> ret = new ArrayList<>();
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
//			SoftwareNotification.notifyError("Error in the Product database: " + e.getMessage());
//		}
		
		
		ret.add(new Product("Red Ballpen"));
		ret.add(new Product("Blue Ballpen"));
		ret.add(new Product("Green Ballpen"));
		ret.add(new Product("Black Ballpen"));
		
		ret.add(new Product("Sola (Orange)"));
		ret.add(new Product("Sola (Apple)"));
		ret.add(new Product("Sola (Lemon)"));
		ret.add(new Product("Sola (Grape)"));
		ret.add(new Product("Sola (Raspberry)"));
			
		ret.add(new Product("Minute Maid Pulpy Orange"));
		ret.add(new Product("Zesto Orange Juice Drink"));

		List<Product> filteredRet = new ArrayList<>();
		for (int i = 0; i < ret.size(); i++) {
			Product x = ret.get(i);
			if (x.getName().toLowerCase().contains(productNameFilter.toLowerCase())) {
				filteredRet.add(x);
			}
		}
		return filteredRet;
	}

	@Override
	public List<Product> getAll(Supplier selectedSupplier, String productNameFilter) {
		// TODO - DEVS implement this
		List<Product> ret = new ArrayList<>();
		/*
		ret.add(new Product("Red Ballpen"));
		ret.add(new Product("Blue Ballpen"));
		ret.add(new Product("Green Ballpen"));
		ret.add(new Product("Black Ballpen"));
		
		ret.add(new Product("Sola (Orange)"));
		ret.add(new Product("Sola (Apple)"));
		ret.add(new Product("Sola (Lemon)"));
		ret.add(new Product("Sola (Grape)"));
		ret.add(new Product("Sola (Raspberry)"));
			
		ret.add(new Product("Minute Maid Pulpy Orange"));
		ret.add(new Product("Zesto Orange Juice Drink"));
*/
		List<Product> filteredRet = new ArrayList<>();
		for (int i = 0; i < ret.size(); i++) {
			Product x = ret.get(i);
			if (x.getName().toLowerCase().contains(productNameFilter.toLowerCase())) {
				filteredRet.add(x);
			}
		}
		return filteredRet;
	}
	
	@Override
	public boolean isEmpty() {
		return getAll().size() == 0;
	}



}
