package procurementsys.model.database;

import java.util.List;

import procurementsys.model.Supplier;
import procurementsys.model.Tag;

public interface SupplierDAO {
	void add(Supplier supplier);
	List<Supplier> getAll();
	List<Supplier> getAll(String nameFilter);
	List<Supplier> getAll(List<Tag> tags, String nameFilter);
	boolean isEmpty();
	Supplier get(String name);
	
}
