package procurementsys.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import procurementsys.model.ProductOffer;
import procurementsys.model.Tag;
import procurementsys.view.SoftwareNotification;

public class MySQLTagDAO implements TagDAO {
	private Connection conn;
	
	public MySQLTagDAO() {
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
		// TODO - DEVS implement this
		List<Tag> ret = new ArrayList<>();
		
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
