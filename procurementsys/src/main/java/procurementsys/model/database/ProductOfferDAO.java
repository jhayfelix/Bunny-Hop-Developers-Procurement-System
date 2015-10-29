package procurementsys.model.database;

import java.util.List;

import procurementsys.model.Product;
import procurementsys.model.ProductOffer;

public interface ProductOfferDAO {
	void add(ProductOffer productOffer);
	List<ProductOffer> getAll(Product product, String filterStr);
}
