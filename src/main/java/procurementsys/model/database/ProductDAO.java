package procurementsys.model.database;

import java.util.List;

import procurementsys.model.Product;
import procurementsys.model.Supplier;
import procurementsys.model.Tag;

public interface ProductDAO {
	void add(Product product);
	
	Product get(String name);
	List<Product> getAll();
	List<Product> getAll(String productNameFilter);
	List<Product> getAll(List<Tag> tags, String productNameFilter);
	List<Product> getAll(Supplier selectedSupplier,
			String productNameFilter);
	
	boolean isEmpty();
	
}
