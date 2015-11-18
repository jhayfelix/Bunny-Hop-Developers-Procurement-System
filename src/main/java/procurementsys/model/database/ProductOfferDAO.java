package procurementsys.model.database;

import java.util.List;

import procurementsys.model.CostChange;
import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.Promo;

public interface ProductOfferDAO {
	void add(ProductOffer productOffer);
	ProductOffer get(Supplier supplier, Product product);
	List<ProductOffer> getAll(Supplier supplier);
	List<ProductOffer> getAll(Product product, String filterStr);
	boolean isEmpty();
	void addCostChange(ProductOffer productOffer, CostChange costChange);
	void changeAvailability(ProductOffer productOffer, boolean available);
	void setPromo(ProductOffer productOffer);

}
