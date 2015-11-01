package procurementsys.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import procurementsys.model.Supplier;
import procurementsys.model.Tag;

public class MySQLSupplierDAO implements SupplierDAO {
	
	@Override
	public void add(Supplier supplier) {
		String suppName,suppContact;
		//Connection conn = DriverManager.getConnection(url,"","");
		//java.sql.PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query);
       try{
		java.sql.Connection conn = DBConnection.getConnection();
		Statement statement = conn.createStatement();
		suppName=supplier.getName();
		suppContact=supplier.getContactNumber();
		String query = String.format("INSERT INTO procurementdb.suppliers(supplier_name, contact_number, isActive)   VALUES (\"%s\", \"%s\", true)",suppName,suppContact);
		
		
		statement.executeUpdate(query);

		conn.close();}
       catch(SQLException sqlException){
           sqlException.printStackTrace();
       }
       System.out.println("successful pare.");
		// TODO - DEVS implement this
		
	}
	
	@Override
	public List<Supplier> getAll(String nameFilter) {
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
	public List<Supplier> getAll() {
		// TODO - DEVS implement this
		List<Supplier> ret = new ArrayList<>();
		
		ret.add(new Supplier("National Bookstore", "8452005"));
		ret.add(new Supplier("SM Supermarket", "4202045"));
		ret.add(new Supplier("Robinsons Supermarket", "8506453"));
		ret.add(new Supplier("Milan Industries", "7004533"));
		ret.add(new Supplier("La Senza", "2347777"));
		
		return ret;
	}

	@Override
	public List<Supplier> getAll(List<Tag> tags) {
		// TODO - DEVS implement this
		return null;
	}

	@Override
	public boolean isEmpty() {
		return getAll().size() == 0;
	}



}
