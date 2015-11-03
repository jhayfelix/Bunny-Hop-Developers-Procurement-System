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
import procurementsys.model.ProductOffer;
import procurementsys.model.Tag;
import procurementsys.view.SoftwareNotification;

public class MySQLTagDAO implements TagDAO {
	private Connection conn;
	
	public MySQLTagDAO() {
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
	public void add(Tag tag) {
		try {
			String addStr = "INSERT INTO tags(tag_name) VALUES(?)";
			PreparedStatement addTag = conn.prepareStatement(addStr);
			addTag.setString(1, tag.getName());
			addTag.execute();
		} catch (SQLException e) {
			SoftwareNotification.notifyError("The tag '" + tag.getName()
					+ "' already exists in the database.");
		}
	}
	
	@Override
	public List<Tag> getAll() {
		
		 String query = "SELECT * FROM tags";
		 List<Tag> ret = new ArrayList<>();
		 try{
				PreparedStatement getTags = conn.prepareStatement(query); 
				ResultSet rs = getTags.executeQuery(query);
				//System.out.println("TAGS: getALL no: ");
			 while(rs.next()){
				 System.out.println("listing tag 2");
				 String tags= rs.getString("tag_name");
				 //System.out.format("%s \n",tags);
				 ret.add(new Tag(tags));
			 }
			 
		 }
		catch(SQLException e) {
			SoftwareNotification.notifyError("Error in the tags database" +e.getMessage());
		}
		return ret;
	}
/*
		ret.add(new Tag("BALLPEN"));
		ret.add(new Tag("PENCIL"));
		ret.add(new Tag("FOLDER"));
		ret.add(new Tag("SUPPLIES"));
		ret.add(new Tag("PRODUCT"));
		ret.add(new Tag("GEL"));*/
	@Override
	public boolean isEmpty() {
		return getAll().size() == 0;
	}

	@Override
	public List<Tag> getAll(String tagNameFilter) {//implemented by Doms Pagtalunan
		List<Tag> ret = new ArrayList<>();
	
		String query = 
				String.format(   "select * from tags "
						+ "WHERE LOWER(REPLACE(tag_name, ' ', '')) = "
						+ "LOWER(REPLACE(\"%s\", ' ', ''))",tagNameFilter);
	    
	    try{
	    	System.out.println("listing tag 1");
	    	PreparedStatement getTags = conn.prepareStatement(query); 
	    	ResultSet rs = getTags.executeQuery(query);
	    	   //System.out.println("TAGS: getALl tagnamefilter: ");
		       while(rs.next()){
		    	   String tagName=rs.getString("tag_name");
		    	 //  System.out.format("%s \n",tagName);
		    	   ret.add(new Tag(tagName));	
		}}
	    catch(SQLException e){
			SoftwareNotification.notifyError("Error in the TAGS database: " + e.getMessage());
		}
		
		for (Tag x : getAll()) {
			String tagName = x.getName().toLowerCase();
			if (tagName.contains(tagNameFilter.toLowerCase())) {
				ret.add(x);
			}
		}
		
		return ret;
	}

	@Override
	public void tagProductOffer(ProductOffer productOffer, List<Tag> tags) {
		// TODO Auto-generated method stub
		
	}

}
