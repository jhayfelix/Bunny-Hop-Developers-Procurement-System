package procurementsys.model.database;

import java.util.ArrayList;
import java.util.List;

import procurementsys.model.Supplier;
import procurementsys.model.Tag;

public class MySQLSupplierDAO implements SupplierDAO {
	
	@Override
	public void add(Supplier supplier) {
		// TODO - DEVS implement this
		
	}
	
	@Override
	public List<Supplier> getAll(String nameFilter) {
		// TODO - DEVS implement this
		List<Supplier> ret = new ArrayList<>();
		
		ret.add(new Supplier("National Bookstore", "8452005"));
		ret.add(new Supplier("SM Supermarket", "4202045"));
		ret.add(new Supplier("Robinsons Supermarket", "8506453"));
		ret.add(new Supplier("Milan Industries", "7004533"));
		ret.add(new Supplier("La Senza", "2347777"));
		
		List<Supplier> filteredRet = new ArrayList<>();
		for (Supplier x : ret) {
			if (x.getName().toLowerCase().contains(nameFilter.toLowerCase())) {
				filteredRet.add(x);
			}
		}
		return filteredRet;
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
	public List<Supplier> getAll(List<Tag> tags, String nameFilter) {
		// TODO - DEVS implement this
		List<Supplier> ret = new ArrayList<>();
		
		ret.add(new Supplier("National Bookstore", "8452005"));
		ret.add(new Supplier("SM Supermarket", "4202045"));
		ret.add(new Supplier("Robinsons Supermarket", "8506453"));
		ret.add(new Supplier("Milan Industries", "7004533"));
		ret.add(new Supplier("La Senza", "2347777"));
		
		List<Supplier> filteredRet = new ArrayList<>();
		for (Supplier x : ret) {
			if (x.getName().toLowerCase().contains(nameFilter.toLowerCase())) {
				filteredRet.add(x);
			}
		}
		return filteredRet;
	}
	
	@Override
	public boolean isEmpty() {
		return getAll().size() == 0;
	}



}
