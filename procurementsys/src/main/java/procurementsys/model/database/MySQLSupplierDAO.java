package procurementsys.model.database;

import java.util.ArrayList;
import java.util.List;

import procurementsys.model.Supplier;
import procurementsys.model.Tag;

public class MySQLSupplierDAO implements SupplierDAO {
	
	@Override
	public void add(Supplier supplier) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<Supplier> getAll() {
		// TODO - DEVS implement this
		List<Supplier> ret = new ArrayList<>();
		ret.add(new Supplier("National Bookstore", "8452005"));
		ret.add(new Supplier("SM Supermarket", "4202045"));
		ret.add(new Supplier("Robinsons Supermarket", "8506453"));
		ret.add(new Supplier("Milan Industries", "7004533"));
		ret.add(new Supplier("La Senza", "2347777"));
		return ret;
	}

	@Override
	public List<Supplier> getAll(List<Tag> tags) {
		// TODO - DEVS implement this
		return null;
	}

}
