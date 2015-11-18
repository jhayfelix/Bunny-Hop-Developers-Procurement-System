package procurementsys.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;

import org.controlsfx.control.Notifications;

import procurementsys.model.ProductOffer;
import procurementsys.model.Tag;
import procurementsys.view.SoftwareNotification;

public class MySQLTagDAO implements TagDAO {
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
	public MySQLTagDAO() {

	}

	@Override
	public void add(Tag tag) {
		try {
			String addStr = "INSERT INTO tags(tag_name) VALUES(?)";
			PreparedStatement addTag = conn.prepareStatement(addStr);
			addTag.setString(1, tag.getName());
			addTag.execute();
    		Notifications.create().title("Success")
			.text("The tag \'" + tag.getName() 
				  + "\' has been successfully added to the system.")
			.position(Pos.TOP_RIGHT).showInformation();
		} catch (SQLException e) {
			SoftwareNotification.notifyError("The tag '" + tag.getName()
					+ "' already exists in the database.");
		}
	}
	
	@Override
	public List<Tag> getAll() {
		List<Tag> ret = new ArrayList<>();
		try {
			String queryStr = "SELECT * FROM tags";
			PreparedStatement getAll = conn.prepareStatement(queryStr);
			ResultSet rs = getAll.executeQuery();
		
			
			while (rs.next()) {
				Tag t = new Tag(rs.getString("tag_name"));
				ret.add(t);
			}
		} catch (SQLException e) {
			
		}
		return ret;
	}

	@Override
	public boolean isEmpty() {
		return getAll().size() == 0;
	}

	@Override
	public List<Tag> getAll(String tagNameFilter) {
		List<Tag> ret = new ArrayList<>();
		try {
			String queryStr = "SELECT * FROM tags WHERE tag_name LIKE ?";
			PreparedStatement getAll = conn.prepareStatement(queryStr);
			getAll.setString(1, "%" + tagNameFilter + "%");
			ResultSet rs = getAll.executeQuery();
			
			while (rs.next()) {
				Tag t = new Tag(rs.getString("tag_name"));
				ret.add(t);
			}
		} catch (SQLException e) {
			
		}
		return ret;
	}

	@Override
	public void tagProductOffer(ProductOffer productOffer, List<Tag> tags) {
		for (Tag t : tags) {
			tagProductOffer(productOffer, t);
		}
	}
	
	private void tagProductOffer(ProductOffer productOffer, Tag tag) {
		try {
			String addStr = "INSERT INTO product_offers_tags(supplier_name,"
														+ "product_name,"
														+ "tag_name) "
														+ "VALUES(?,?,?);";
			PreparedStatement addTags = conn.prepareStatement(addStr);
			addTags = conn.prepareStatement(addStr);
			addTags.setString(1, productOffer.getSupplier().getName());
			addTags.setString(2, productOffer.getProduct().getName());
			addTags.setString(3, tag.getName());
			addTags.execute();
			
		} catch (SQLException e) {
			SoftwareNotification.notifyError("The product offer already has the tag. "
					+ "Please select another product offer");
		}
	}

}
