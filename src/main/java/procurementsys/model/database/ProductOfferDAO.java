package procurementsys.model.database;

import java.util.List;

import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;

public interface ProductOfferDAO {
	void add(ProductOffer productOffer);
	ProductOffer get(Supplier supplier, Product product);
	List<ProductOffer> getAll(Supplier supplier);
	List<ProductOffer> getAll(Product product, String filterStr);
	boolean isEmpty();

}
