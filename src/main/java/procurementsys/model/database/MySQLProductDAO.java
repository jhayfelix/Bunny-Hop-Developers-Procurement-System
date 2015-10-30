package procurementsys.model.database;

import java.util.ArrayList;
import java.util.List;

import procurementsys.model.Product;
import procurementsys.model.Tag;

public class MySQLProductDAO implements ProductDAO {

	@Override
	public void add(Product product) {
		// TODO - DEVS implement this
		
	}
	

	@Override
	public Product get(String name) {
		// TODO - DEVS implement this
		return new Product(name);
	}
	
	@Override
	public List<Product> getAll() {
		// TODO - DEVS implement this
		List<Product> ret = new ArrayList<>();
		
		ret.add(new Product("Red Ballpen"));
		ret.add(new Product("Blue Ballpen"));
		ret.add(new Product("Green Ballpen"));
		ret.add(new Product("Black Ballpen"));
		
		ret.add(new Product("Sola (Orange)"));
		ret.add(new Product("Sola (Apple)"));
		ret.add(new Product("Sola (Lemon)"));
		ret.add(new Product("Sola (Grape)"));
		ret.add(new Product("Sola (Raspberry)"));
		
		ret.add(new Product("Minute Maid Pulpy Orange"));
		ret.add(new Product("Zesto Orange Juice Drink"));
		
		return ret;
	}

	@Override
	public List<Product> getAll(List<Tag> tags, String filterStr) {
		// TODO - DEVS implement this, note that the filtermethod does not work
		List<Product> ret = new ArrayList<>();
		
		ret.add(new Product("Red Ballpen"));
		ret.add(new Product("Blue Ballpen"));
		ret.add(new Product("Green Ballpen"));
		ret.add(new Product("Black Ballpen"));
		
		ret.add(new Product("Sola (Orange)"));
		ret.add(new Product("Sola (Apple)"));
		ret.add(new Product("Sola (Lemon)"));
		ret.add(new Product("Sola (Grape)"));
		ret.add(new Product("Sola (Raspberry)"));
			
		ret.add(new Product("Minute Maid Pulpy Orange"));
		ret.add(new Product("Zesto Orange Juice Drink"));

		List<Product> filteredRet = new ArrayList<>();
		for (int i = 0; i < ret.size(); i++) {
			Product x = ret.get(i);
			if (x.getName().toLowerCase().contains(filterStr.toLowerCase())) {
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
