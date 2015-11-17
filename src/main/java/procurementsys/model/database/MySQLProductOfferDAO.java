package procurementsys.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import procurementsys.model.CostChange;
import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Promo;
import procurementsys.model.Supplier;
import procurementsys.model.Tag;
import procurementsys.view.SoftwareNotification;

public class MySQLProductOfferDAO implements ProductOfferDAO {
	
	private Connection conn;
	
	public MySQLProductOfferDAO() {
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
	public void add(ProductOffer productOffer) {
		try {
			Supplier supplier = productOffer.getSupplier();
			Product product = productOffer.getProduct();
			boolean isAvailable = true;
			Promo promo = productOffer.getPromo();
			
			String addStr = "INSERT INTO product_offers(supplier_name,"
													 + "product_name,"
													 + "is_Available,"
													 + "promo_qty_needed,"
													 + "promo_discount_percentage)"
													 + "VALUES (?,?,?,?,?);";
			PreparedStatement addProductOffer = conn.prepareStatement(addStr);
			addProductOffer.setString(1, supplier.getName());
			addProductOffer.setString(2, product.getName());
			addProductOffer.setBoolean(3, isAvailable);
			addProductOffer.setInt(4, (promo == null)  ? -1 : promo.getQuantityNeeded());
			addProductOffer.setDouble(5, (promo == null) ? 0.0 : promo.getDiscount());
			addProductOffer.execute();
			
			
			// add initial cost change
			CostChange costChange = productOffer.getCurrentCostChange();
			addStr = "INSERT INTO cost_changes(supplier_name,"
											+ "product_name,"
											+ "change_datetime,"
											+ "cost) "
											+ "VALUES(?,?,?,?);";
			PreparedStatement addCostChanges = conn.prepareStatement(addStr);
			addCostChanges.setString(1, supplier.getName());
			addCostChanges.setString(2, product.getName());
			addCostChanges.setString(3, costChange.getChangeDateTime() + "");
			addCostChanges.setDouble(4, costChange.getCost());
			addCostChanges.execute();
			
			// No need to add tags yet
			
			SoftwareNotification.notifySuccess("The product offer has been"
					+ " successfully added to the system.");
			
		} catch (SQLException e) {
			SoftwareNotification.notifyError("Error with adding order. "
					+ "Please contact the developers.");
		}
	}
	
	@Override
	public ProductOffer get(Supplier supplier, Product product) {
		try {
			String queryStr = "SELECT CC.change_datetime, CC.cost "
					+ "FROM product_offers PO "
					+ "JOIN cost_changes CC ON (PO.supplier_name = CC.supplier_name "
											+ "AND PO.product_name = CC.product_name) "
					+ "WHERE PO.supplier_name = ? AND PO.product_name = ?;";
			PreparedStatement getCostChanges = conn.prepareStatement(queryStr);
			getCostChanges.setString(1, supplier.getName());
			getCostChanges.setString(2, product.getName());
			ResultSet rs = getCostChanges.executeQuery();
			
			List<CostChange> costChanges = new ArrayList<>();
			while (rs.next()) {
				LocalDateTime dateTime = rs.getTimestamp("change_datetime").toLocalDateTime();
				double cost = rs.getDouble("cost");
				costChanges.add(new CostChange(dateTime, cost));
			}
			
			/* TODO  - get promo
			queryStr = "SELECT supplier_name, product_name, change_datetime, cost "
				     + "FROM product_offer PO "
				     + "WHERE supplier_name = ? AND product_name = ?";
			PreparedStatement getPromo = conn.prepareStatement(sql);
			*/
			
			ProductOffer po = new ProductOffer(product, supplier, costChanges);
			
			queryStr = "SELECT POT.tag_name "
					 + "FROM product_offers PO "
					 + "JOIN product_offers_tags POT ON(PO.supplier_name = POT.supplier_name "
					 								+ "AND PO.product_name = POT.product_name) "
					 + "WHERE PO.supplier_name = ? AND PO.product_name = ?;";
			PreparedStatement getTags = conn.prepareStatement(queryStr);
			getTags.setString(1, supplier.getName());
			getTags.setString(2, product.getName());
			rs = getTags.executeQuery();
			
			while (rs.next()) {
				String tagName = rs.getString("tag_name");
				po.addTag(new Tag(tagName));
			}
			
			return po;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	
	@Override
	public List<ProductOffer> getAll(Supplier supplier) {
		List<ProductOffer> ret = new ArrayList<>();
		try {
			String queryStr = "SELECT product_name "
							+ "FROM product_offers "
							+ "WHERE supplier_name = ?";
			PreparedStatement getProducts = conn.prepareStatement(queryStr);
			getProducts.setString(1, supplier.getName());
			ResultSet rs = getProducts.executeQuery();
			
			List<Product> products = new ArrayList<>();
			while (rs.next()) {
				Product p = new Product(rs.getString("product_name"));
				products.add(p);
			}
			
			for (Product p : products) {
				ProductOffer po = get(supplier, p);
				if (po != null) {
					ret.add(po);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	
	@Override
	public List<ProductOffer> getAll(Product product, String filterSupplier) {
		
		List<ProductOffer> ret = new ArrayList<>();
		
		try {
			String queryStr = "SELECT supplier_name "
							+ "FROM product_offers "
							+ "WHERE product_name = ? AND supplier_name LIKE ?";
			PreparedStatement getSuppliers = conn.prepareStatement(queryStr);
			getSuppliers.setString(1, product.getName());
			getSuppliers.setString(2, "%" + filterSupplier + "%");
			ResultSet rs = getSuppliers.executeQuery();
			
			List<String> supplierNames = new ArrayList<>();
			while (rs.next()) {
				supplierNames.add(rs.getString("supplier_name"));
			}
			
			List<Supplier> suppliers = new ArrayList<>();
			SupplierDAO supplierDAO = new MySQLSupplierDAO();
			for (String name : supplierNames) {
				Supplier s = supplierDAO.get(name);
				if (!suppliers.contains(s)) {
					suppliers.add(s);
				}
			}
			
			for (Supplier s : suppliers) {
				ProductOffer po = get(s, product);
				if (po != null) {
					ret.add(po);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ret;
	}

	@Override
	public List<ProductOffer> getAll() {
		List<ProductOffer> ret = new ArrayList<>();
		try {
			String queryStr = "SELECT supplier_name "
							+ "FROM product_offers";
			PreparedStatement getSuppliers = conn.prepareStatement(queryStr);
			ResultSet rs = getSuppliers.executeQuery();
			
			List<String> supplierNames = new ArrayList<>();
			while (rs.next()) {
				supplierNames.add(rs.getString("supplier_name"));
			}
			
			List<Supplier> suppliers = new ArrayList<>();
			SupplierDAO supplierDAO = new MySQLSupplierDAO();
			for (String name : supplierNames) {
				Supplier s = supplierDAO.get(name);
				suppliers.add(s);
			}
			
			for (Supplier s : suppliers) {
				ret.addAll(getAll(s));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	@Override
	public boolean isEmpty() {
		return getAll().size() == 0;
	}

	@Override
	public void addCostChange(ProductOffer productOffer, CostChange costChange) {
		try {
			String addStr = "INSERT INTO cost_changes(supplier_name,"
				       + "product_name,"
				       + "change_datetime,"
				       + "cost) "
			           + "VALUES(?,?,?,?);";
			PreparedStatement addCostChanges = conn.prepareStatement(addStr);
			addCostChanges.setString(1, productOffer.getSupplier().getName());
			addCostChanges.setString(2, productOffer.getProduct().getName());
			addCostChanges.setString(3, costChange.getChangeDateTime() + "");
			addCostChanges.setDouble(4, costChange.getCost());
			addCostChanges.execute();
		} catch (SQLException e) {
			SoftwareNotification.notifyError("The selected product offer already "
					+ "has an identical cost change. Please enter a different cost change.");
		}
		
	}
	@Override
	public void toggleProductOfferAvailability(ProductOffer productOffer){
		try {
					Supplier supplier=productOffer.getSupplier();
					Product product = productOffer.getProduct();
					int changer=2;
					String getStr= "SELECT supplier_name,product_name,isAvailable FROM product_offers WHERE supplier_name LIKE ? AND product_name LIKE ?";
					PreparedStatement get = conn.prepareStatement(getStr);
					get.setString(1,supplier.getName());
					get.setString(2,product.getName());
					ResultSet rs = get.executeQuery();
					
					while(rs.next()) {
						Boolean isAvailable= rs.getBoolean("isAvailable");
						if(isAvailable)
						changer=0;
						else
						changer=1;
					}
					
					
					String queryStr = "UPDATE product_offers SET isAvailable=? WHERE supplier_name=? and product_name=?";
					PreparedStatement setAvailability= conn.prepareStatement(queryStr);
					setAvailability.setInt(1,changer);
					setAvailability.setString(2, supplier.getName());
					setAvailability.setString(3, product.getName());
					setAvailability.executeUpdate();
				
					System.out.println("product offer has been toggled");
					String successMsg = "The Product offer of  " + supplier.getName()
					+ " offering the product : "+product.getName()
					+" is now changed to " +changer;
					System.out.println(successMsg);
					//SoftwareNotification.notifySuccess(successMsg);
				} catch (SQLException e) {
					e.printStackTrace();
					//SoftwareNotification.notifyError("There is an error with database trying to toggle product Offer Availability."
					//		+ " Please notify the developers.");
				}

		}






}
