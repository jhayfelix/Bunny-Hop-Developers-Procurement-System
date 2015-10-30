package procurementsys.model.database;

import java.util.List;

import procurementsys.model.Product;
import procurementsys.model.Supplier;
import procurementsys.model.Tag;

public interface ProductDAO {
	void add(Product product);
	Product get(String name);
	List<Product> getAll();
	List<Product> getAll(List<Tag> tags, String filterStr);
	List<Product> getAll(Supplier selectedSupplier, String filterStr);
	boolean isEmpty();
	
}
