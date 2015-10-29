package procurementsys.model.database;

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
		// TODO - DEVS implement this
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