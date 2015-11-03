package procurementsys.model.database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import procurementsys.model.CostChange;
import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.Tag;
import procurementsys.view.SoftwareNotification;
import java.text.SimpleDateFormat;

public class MySQLProductOfferDAO implements ProductOfferDAO {
	private Connection conn;
	private String supplier;
	private String product;
	private Boolean isChecked;
	private Date dt;
	private SimpleDateFormat sdf;
	
	public MySQLProductOfferDAO(){
		conn=DBConnection.getConnection();
	}
	
	@Override
	public void add(ProductOffer productOffer) {
		supplier=""+productOffer.getSupplier();
		product=""+productOffer.getProduct();
		productOffer.getCurrentCost();
		//dt =		
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime =  productOffer.getUpcomingCostChangeDateStr();		
		String[] parts = currentTime.split("T");
		
		currentTime.concat(parts[0]);
		currentTime.concat(parts[1]);
		System.out.println("time: "+ parts[1]);
		System.out.println("sys time: "+ currentTime);
		System.out.println("suplier name: "+ supplier);
		System.out.println("product name: "+ product);
		//System.out.println("isChecked? "+ isChecked);
		System.out.println("cost? "+ currentTime);
		System.out.println("Current time: "+currentTime);
			try {
				String query =String.format( "INSERT INTO cost_changes(supplier_name,product_name,change_datetime,cost)"
						+ " VALUES(%s,%s,%s,%s)",productOffer.getSupplier().toString(),productOffer.getProduct().toString(),currentTime,productOffer.getCostChanges().toString());
				PreparedStatement addProductOffer = conn.prepareStatement(query);
//				addProductOffer.setString(1, productOffer.getSupplier().toString());
//				addProductOffer.setString(2, productOffer.getProduct().toString());
//				addProductOffer.setDate(3, currentTime);
//				addProductOffer.setString(4, productOffer.getCostChanges().toString());
				
				addProductOffer.execute();
		} catch (SQLException e) {
			SoftwareNotification.notifyError("Error in product offer database.");
		}
	}
	
	
	@Override
	public ProductOffer get(Supplier supplier, Product product) {
		// TODO - DEVS implement this
		try {
			String query =String.format( "SELECT supplier_name,product_name from cost changes");
			PreparedStatement addProductOffer = conn.prepareStatement(query);
			ResultSet rs = addProductOffer.executeQuery(query);
			while(rs.next()){
				String supp = rs.getString("supplier_name");
				String prod = rs.getString("product_name");
			System.out.println("this is the supplier: "+ "this is the product");
			}
//			addProductOffer.setString(1, productOffer.getSupplier().toString());
//			addProductOffer.setString(2, productOffer.getProduct().toString());
//			addProductOffer.setDate(3, currentTime);
//			addProductOffer.setString(4, productOffer.getCostChanges().toString());
			
		
	} catch (SQLException e) {
		SoftwareNotification.notifyError("Error in product offer database.");
	}
		
		return getAll(product,"").get(0);
	}
	
	@Override
	public List<ProductOffer> getAll(Supplier supplier) {
		// TODO - DEVS implement this
		System.out.println("based on supplier?");
		List<ProductOffer> ret = new ArrayList<>();
		List<Product> products = new ArrayList<>();
		String query = 
				String.format(   "select * from cost_changes "
						+ "WHERE LOWER(REPLACE(supplier_name, ' ', '')) = "
						+ "LOWER(REPLACE(\"%s\", ' ', ''))",supplier);
		System.out.println("test");MySQLProductDAO productDAO = new MySQLProductDAO();
		
		try{
			PreparedStatement getProduct = conn.prepareStatement(query); 
		    ResultSet rs = getProduct.executeQuery(query);
		       while(rs.next()){
		    	   String productName=rs.getString("product_name");
		    	  // System.out.format("product name labas: %s \n",productName);
		    	  // ret.add(new Product(productName));	
		    	   products.add(new Product(productName));
		}}catch(SQLException e){
			SoftwareNotification.notifyError("Error in the Product offer database: " + e.getMessage());
		}
		
		//List<Product> products = productDAO.getAll();
		
		for (Product x : products) {
				List<CostChange> costChanges = new ArrayList<>();
				costChanges.add(new CostChange(LocalDateTime.of(2013, Month.JULY, 2, 0, 0),20.0));
				costChanges.add(new CostChange(LocalDateTime.of(2014, Month.MARCH, 3, 0, 0),22.50));
				costChanges.add(new CostChange(LocalDateTime.of(2015, Month.JANUARY, 4, 0, 0),21.0));
				costChanges.add(new CostChange(LocalDateTime.of(2015, Month.OCTOBER, 5, 0, 0),65.0));
				costChanges.add(new CostChange(LocalDateTime.of(2016, Month.OCTOBER, 1, 0, 0),25.0));
				ret.add(new ProductOffer(x, supplier, costChanges));
		}
		
		//TagDAO tagDAO = new MySQLTagDAO();
//		for (ProductOffer po : ret) {
//			List<Tag> tags = tagDAO.getAll();
//			for (Tag t : tags) {
//				if (!t.getName().equals("PENCIL")) {
//					List<Tag> tempList = new ArrayList<>();
//					tempList.add(t);
//					po.addAllTags(tempList);
//				}
//			}
//		}
		return ret;
	}
	
	
	@Override
	public List<ProductOffer> getAll(Product product, String filterStr) {
		// TODO - DEVS implement this
		List<ProductOffer> ret = new ArrayList<>();
		
		SupplierDAO supplierDAO = new MySQLSupplierDAO();
		List<Supplier> suppliers = supplierDAO.getAll();
		
		for (Supplier x : suppliers) {
			if (x.getName().toLowerCase().contains(filterStr.toLowerCase())) {
				List<CostChange> costChanges = new ArrayList<>();
				costChanges.add(new CostChange(LocalDateTime.of(2013, Month.JULY, 2, 0, 0),20.0));
				costChanges.add(new CostChange(LocalDateTime.of(2014, Month.MARCH, 3, 0, 0),22.50));
				costChanges.add(new CostChange(LocalDateTime.of(2015, Month.JANUARY, 4, 0, 0),21.0));
				costChanges.add(new CostChange(LocalDateTime.of(2015, Month.OCTOBER, 5, 0, 0),27.0));
				costChanges.add(new CostChange(LocalDateTime.of(2016, Month.OCTOBER, 1, 0, 0),25.0));
				ret.add(new ProductOffer(product, x, costChanges));
			}
		}
		return ret;
	}

	@Override
	public boolean isEmpty() {
		try{
			String query = "SELECT * FROM cost_changes";
			PreparedStatement addProductOffer = conn.prepareStatement(query);
			ResultSet rs = addProductOffer.executeQuery(query);
			if(rs.isLast()){
				System.out.println("no other");
			return true;}
			
		}catch(SQLException e){
			SoftwareNotification.notifyError("Error in the Product OFfer database: " + e.getMessage());
		}
	
		return false;
	}

	@Override
	public void addCostChange(ProductOffer productOffer, CostChange costChange) {
		// TODO Auto-generated method stub
		
	}




}
