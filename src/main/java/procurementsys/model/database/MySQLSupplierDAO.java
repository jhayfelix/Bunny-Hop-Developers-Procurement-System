package procurementsys.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.Tag;
import procurementsys.view.SoftwareNotification;

public class MySQLSupplierDAO implements SupplierDAO {
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
	
	public MySQLSupplierDAO() {

		
	}
	
	@Override
	public void add(Supplier supplier)  {
		try {
			String addStr = "INSERT INTO suppliers(supplier_name, contact_number, is_active)"
					+ " VALUES(?,?,?);";
			PreparedStatement addSupplier = conn.prepareStatement(addStr);
			addSupplier.setString(1, supplier.getName());
			addSupplier.setString(2, supplier.getContactNumber());
			addSupplier.setBoolean(3, supplier.isActive());
			addSupplier.execute();
			String successMsg = "The supplier \'" + supplier.getName() 
					  + "\' has been successfully added to the system.";
			SoftwareNotification.notifySuccess(successMsg);
			
		} catch (SQLException e) {
			SoftwareNotification.notifyError("The supplier '" + supplier.getName()
					+ "' already exists in the database.");
		}			
	
	}
	
	@Override
	public Supplier get(String name) {		
		try {
			String queryStr = "SELECT * FROM suppliers WHERE supplier_name = ?";
			PreparedStatement getAll = conn.prepareStatement(queryStr);
			getAll.setString(1, name);
			ResultSet rs = getAll.executeQuery();
			while (rs.next()) {
				String supplierName = rs.getString("supplier_name");
				String contactNumber = rs.getString("contact_number");
				return new Supplier(supplierName, contactNumber);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<Supplier> getAll() {
		List<Supplier> ret = new ArrayList<>();
		
		try {
			String queryStr = "SELECT * FROM suppliers";
			PreparedStatement getAll = conn.prepareStatement(queryStr);
			ResultSet rs = getAll.executeQuery();
			
			while (rs.next()) {
				String supplierName = rs.getString("supplier_name");
				String contactNumber = rs.getString("contact_number");
				ret.add(new Supplier(supplierName, contactNumber));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	@Override
	public List<Supplier> getAll(String nameFilter) {
		List<Supplier> ret = new ArrayList<>();
		
		try {
			String queryStr = "SELECT * FROM suppliers WHERE supplier_name LIKE ?";
			PreparedStatement getAll = conn.prepareStatement(queryStr);
			getAll.setString(1, "%" + nameFilter +  "%");
			ResultSet rs = getAll.executeQuery();
			while (rs.next()) {
				String supplierName = rs.getString("supplier_name");
				String contactNumber = rs.getString("contact_number");
				ret.add(new Supplier(supplierName, contactNumber));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public List<Supplier> getAll(List<Tag> tags, String nameFilter) {
		ProductOfferDAO productOfferDAO = new MySQLProductOfferDAO();
		List<ProductOffer> productOffers = productOfferDAO.getAll();
		
		List<Supplier> ret = new ArrayList<>();
		for (ProductOffer po : productOffers) {
			if (po.getTags().containsAll(tags)) {
				Supplier supplier = po.getSupplier();
				if (!ret.contains(supplier)) {
					ret.add(supplier);
				}
			}
		}
		
		return ret;
	}
	
	@Override
	public boolean isEmpty() {
		return getAll().size() == 0;
	}


}