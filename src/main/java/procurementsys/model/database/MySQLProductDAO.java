package procurementsys.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.Tag;
import procurementsys.view.SoftwareNotification;

public class MySQLProductDAO implements ProductDAO {
	private static Connection conn;
	static {
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
	
	public MySQLProductDAO() {

	}
	
	@Override 
	public void add(Product product) {
		try {
			String addStr = "INSERT INTO products(product_name) VALUES(?)";
			PreparedStatement addProduct =  conn.prepareStatement(addStr);
			addProduct.setString(1, product.getName());
			addProduct.execute();
    		String successMsg = "The product \'" + product.getName() 
				  + "\' has been successfully added to the system.";
    		SoftwareNotification.notifySuccess(successMsg);
		} catch (SQLException e) {
			SoftwareNotification.notifyError("The product '" + product.getName()
					+ "' already exists in the database.");
		}
	}

	@Override
	public Product get(String name) {
		Product ret = null;
		try {
			String queryStr = "SELECT * FROM products WHERE product_name = ?";
			PreparedStatement getAll = conn.prepareStatement(queryStr);
			getAll.setString(1, name);
			ResultSet rs = getAll.executeQuery();
			
			while(rs.next()) {
				String productName = rs.getString("product_name");
				ret = new Product(productName);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			SoftwareNotification.notifyError("There is an error with database."
					+ " Please notify the developers.");
		}
		
		return ret;
	}
	
	@Override
	public List<Product> getAll() {
		List<Product> ret = new ArrayList<>();
		try {
			String queryStr = "SELECT * FROM products";
			PreparedStatement getAll = conn.prepareStatement(queryStr);
			ResultSet rs = getAll.executeQuery();
			
			while(rs.next()) {
				String productName = rs.getString("product_name");
				ret.add(new Product(productName.toUpperCase()));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			SoftwareNotification.notifyError("There is an error with database."
					+ " Please notify the developers.");
		}
		
		return ret;
	}
	

	@Override
	public List<Product> getAll(String productNameFilter) {		
		List<Product> ret = new ArrayList<>();
		try {
			String queryStr = "SELECT * FROM products WHERE product_name LIKE ?";
			PreparedStatement getAll = conn.prepareStatement(queryStr);
			getAll.setString(1, "%" + productNameFilter.toUpperCase() + "%");
			ResultSet rs = getAll.executeQuery();
			
			while(rs.next()) {
				String productName = rs.getString("product_name");
				ret.add(new Product(productName));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			SoftwareNotification.notifyError("There is an error with database."
					+ " Please notify the developers.");
		}
		
		return ret;
	}

	@Override
	public List<Product> getAll(List<Tag> tags, String productNameFilter) {
		ProductOfferDAO productOfferDAO = new MySQLProductOfferDAO();
		List<ProductOffer> productOffers = productOfferDAO.getAll();
		
		List<Product> ret = new ArrayList<>();
		for (ProductOffer po : productOffers) {
			if (po.getTags().containsAll(tags)) {
				Product p = po.getProduct();
				if (!ret.contains(p)) {
					ret.add(p);
				}
			}
		}
		
		List<Product> filteredRet = new ArrayList<>();
		for (Product p : ret) {
			if (p.getName().toLowerCase().contains(productNameFilter.toLowerCase())) {
				filteredRet.add(p);
			}
		}
		
		return filteredRet;
	}

	@Override
	public List<Product> getAll(Supplier selectedSupplier, String productNameFilter) {
		List<Product> ret = new ArrayList<>();
		try {
			String queryStr =   "SELECT PO.product_name "
							  + "FROM suppliers S JOIN product_offers PO ON (S.supplier_name = PO.supplier_name) "
							  + "WHERE PO.product_name LIKE ? AND S.supplier_name = ?;"; 
			PreparedStatement getAll = conn.prepareStatement(queryStr);
			getAll.setString(1, "%" + productNameFilter + "%");
			getAll.setString(2, selectedSupplier.getName());
			ResultSet rs = getAll.executeQuery();
			
			while(rs.next()) {
				String productName = rs.getString("product_name");
				ret.add(new Product(productName));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			SoftwareNotification.notifyError("There is an error with database."
					+ " Please notify the developers.");
		}
		return ret;
	}
	
	@Override
	public boolean isEmpty() {
		return getAll().size() == 0;
	}



}
