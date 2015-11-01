package procurementsys.model.database;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import procurementsys.model.CostChange;
import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;

public class MySQLProductOfferDAO implements ProductOfferDAO {
	
	@Override
	public void add(ProductOffer productOffer) {
		/*try{
			java.sql.Connection conn = DBConnection.getConnection();
			Statement statement = conn.createStatement();
			productOffer.getSupplier();
			productOffer.getProduct();
			productOffer.get
			String query = String.format("INSERT INTO procurementdb.product_offers(supplier_name, contact_number, isActive)   VALUES (\"%s\", \"%s\", true)",suppName,suppContact);
			
			
			statement.executeUpdate(query);

			conn.close();}
	       catch(SQLException sqlException){
	           sqlException.printStackTrace();
	       }
	       System.out.println("successful pare.");
		*/
		
		
		
	}
	
	@Override
	public ProductOffer get(Supplier supplier, Product product) {
		// TODO - DEVS implement this
		return getAll(product,"").get(0);
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
	

}
