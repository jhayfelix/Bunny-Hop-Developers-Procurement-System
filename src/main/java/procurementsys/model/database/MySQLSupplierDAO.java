package procurementsys.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import procurementsys.model.Supplier;
import procurementsys.model.Tag;
import procurementsys.view.SoftwareNotification;

public class MySQLSupplierDAO implements SupplierDAO {
	private Connection conn;
	
	public MySQLSupplierDAO() {
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			String url = "jdbc:mysql://localhost/procurementdb";
//			conn = DriverManager.getConnection(url, "root", "pagtalunan");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		conn=DBConnection.getConnection();
	}
	
	@Override
	public void add(Supplier supplier)  {
		try {
			String addStr = "INSERT INTO suppliers(supplier_name, contact_number, isActive)"
					+ " VALUES(?,?,?);";
			PreparedStatement addSupplier = conn.prepareStatement(addStr);
			addSupplier.setString(1, supplier.getName());
			addSupplier.setString(2, supplier.getContactNumber());
			addSupplier.setBoolean(3, supplier.isActive());
			addSupplier.execute();
		} catch (SQLException e) {
			SoftwareNotification.notifyError("The supplier '" + supplier.getName()
					+ "' already exists in the database.");
		}			
	
	}
	
	@Override
	public List<Supplier> getAll(String nameFilter) {
		// TODO - DEVS implement this
		  List<Supplier> ret = new ArrayList<>();
	      String query = 
String.format(   "select * from suppliers "
		+ "WHERE LOWER(REPLACE(supplier_name, ' ', '')) = "
		+ "LOWER(REPLACE(\"%s\", ' ', ''))",nameFilter);
	      
		try {
			//st = conn.createStatement(); 
			PreparedStatement getSupplier = conn.prepareStatement(query); 
			ResultSet rs = getSupplier.executeQuery(query);
	       while(rs.next()){
	    	   String suppName=rs.getString("supplier_name");
	    	   String conNum=rs.getString("contact_number");
	    	   Boolean isActive=rs.getBoolean("isActive");
	    	   System.out.format("%s, %s, %b \n",suppName,conNum,isActive);
	    	   ret.add(new Supplier(suppName, conNum));
	    	
	    }
	    //  st.close();
	      
		} catch (SQLException e) {
			SoftwareNotification.notifyError("Error in the supplier database");

			e.printStackTrace();
		}
	  	/*
		ret.add(new Supplier("National Bookstore", "8452005"));
		ret.add(new Supplier("SM Supermarket", "4202045"));
		ret.add(new Supplier("Robinsons Supermarket", "8506453"));
		ret.add(new Supplier("Milan Industries", "7004533"));
		ret.add(new Supplier("La Senza", "2347777"));
		*/
		List<Supplier> filteredRet = new ArrayList<>();
		for (Supplier x : ret) {
			if (x.getName().toLowerCase().contains(nameFilter.toLowerCase())) {
				filteredRet.add(x);
			}
		}
		return filteredRet;
	}
	
	@Override
	public List<Supplier> getAll() { //implemented by DOms
		List<Supplier> ret = new ArrayList<>();
	      String query = "SELECT * FROM suppliers";
	      Statement st;
		try {
			//st = conn.createStatement(); 
			PreparedStatement getSupplier = conn.prepareStatement(query); 
			ResultSet rs = getSupplier.executeQuery(query);
	       while(rs.next()){
	    	   String suppName=rs.getString("supplier_name");
	    	   String conNum=rs.getString("contact_number");
	    	   Boolean isActive=rs.getBoolean("isActive");
	    	   System.out.format("%s, %s, %b \n",suppName,conNum,isActive);
	    	   ret.add(new Supplier(suppName, conNum));
	    	
	    }
	    //  st.close();
	      
		} catch (SQLException e) {
			SoftwareNotification.notifyError("Error in the supplier database");

			e.printStackTrace();
		}
		/*
		ret.add(new Supplier("National Bookstore", "8452005"));
		ret.add(new Supplier("SM Supermarket", "4202045"));
		ret.add(new Supplier("Robinsons Supermarket", "8506453"));
		ret.add(new Supplier("Milan Industries", "7004533"));
		ret.add(new Supplier("La Senza", "2347777"));
		*/
		return ret;
	}

	@Override
	public List<Supplier> getAll(List<Tag> tags, String nameFilter) {
		// TODO - DEVS implement this
		List<Supplier> ret = new ArrayList<>();
		
		ret.add(new Supplier("National Bookstore", "8452005"));
		ret.add(new Supplier("SM Supermarket", "4202045"));
		ret.add(new Supplier("Robinsons Supermarket", "8506453"));
		ret.add(new Supplier("Milan Industries", "7004533"));
		ret.add(new Supplier("La Senza", "2347777"));
		
		List<Supplier> filteredRet = new ArrayList<>();
		for (Supplier x : ret) {
			if (x.getName().toLowerCase().contains(nameFilter.toLowerCase())) {
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
