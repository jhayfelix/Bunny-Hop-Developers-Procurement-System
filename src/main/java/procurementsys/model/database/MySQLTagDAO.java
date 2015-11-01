package procurementsys.model.database;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import procurementsys.model.Tag;

public class MySQLTagDAO implements TagDAO {

	@Override
	public void add(Tag tag) {
		String tags;
		 try{
				java.sql.Connection conn = DBConnection.getConnection();
				Statement statement = conn.createStatement();
				tags=tag.getName();
				//suppContact=supplier.getContactNumber();
				String query = String.format("INSERT INTO procurementdb.tags(tag_name)   VALUES (\"%s\"	)",tags);
				
				
				statement.executeUpdate(query);

				conn.close();}
		       catch(SQLException sqlException){
		           sqlException.printStackTrace();
		       }
		       System.out.println("successful pare 2.");

	}
	
	@Override
	public List<Tag> getAll() {
		// TODO - DEVS implement this properly
		List<Tag> ret = new ArrayList<>();
		ret.add(new Tag("BALLPEN"));
		ret.add(new Tag("PENCIL"));
		ret.add(new Tag("FOLDER"));
		ret.add(new Tag("SUPPLIES"));
		ret.add(new Tag("PRODUCT"));
		ret.add(new Tag("GEL"));
		return ret;
	}

	@Override
	public boolean isEmpty() {
		return getAll().size() == 0;
	}

	@Override
	public List<Tag> getAll(String tagNameFilter) {

		List<Tag> ret = new ArrayList<>();
		
		for (Tag x : getAll()) {
			String tagName = x.getName().toLowerCase();
			if (tagName.contains(tagNameFilter.toLowerCase())) {
				ret.add(x);
			}
		}
		
		return ret;
	}

}
