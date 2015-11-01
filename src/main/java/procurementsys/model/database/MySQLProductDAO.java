package procurementsys.model.database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import procurementsys.model.Product;
import procurementsys.model.Supplier;
import procurementsys.model.Tag;

public class MySQLProductDAO implements ProductDAO {

	@Override
	public void add(Product product) {
		String test;
		// TODO - DEVS implement this
		  try {
			  
			  System.out.println("product: "+ product.getName());
			 // test=String.format("INSERT INTO products(product_name) VALUES (\"%s\")",product.getName());
			  //String test = String.format("test goes here %s more text", "Testing");
			  //test=product.getName();
			 // INSERT INTO procurementdb.products (product_name) VALUES ('d');
	            String query = String.format("INSERT INTO procurementdb.products(product_name) VALUES (\"%s\")",product.getName());
	            java.sql.PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query);
	            java.sql.Connection conn = DBConnection.getConnection();
				java.sql.Statement statement = conn.createStatement();
				
				statement.executeUpdate(query);
				conn.close();
	           // ResultSet resultSet = preparedStatement.executeQuery();
	          //  return resultSet;
	        } catch (SQLException sqlException) {
	            sqlException.printStackTrace();
	        }
	        System.out.println("it happened");
	      //  return null;
	    }
		
	

	@Override
	public Product get(String productNameFilter) {
		// TODO - DEVS implement this
		return new Product(productNameFilter);
	}
	
	@Override
	public List<Product> getAll() {
		// TODO - DEVS implement this
		List<Product> ret = new ArrayList<>();
		
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
		
		return ret;
	}
	

	@Override
	public List<Product> getAll(String productNameFilter) {
		// TODO - DEVS implement this
		List<Product> ret = new ArrayList<>();
		
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
	public List<Product> getAll(List<Tag> tags, String productNameFilter) {
		// TODO - DEVS implement this
		List<Product> ret = new ArrayList<>();
		
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
	public boolean isEmpty() {
		return getAll().size() == 0;
	}



}
