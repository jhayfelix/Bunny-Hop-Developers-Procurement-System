package procurementsys.model.database;

import java.util.List;

import procurementsys.model.Product;
import procurementsys.model.Tag;

public interface ProductDAO {
	void add(Product product);
	List<Product> getAll();
	List<Product> getAll(List<Tag> tags, String filterStr);
	boolean isEmpty();
}
