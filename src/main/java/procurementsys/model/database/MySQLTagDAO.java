package procurementsys.model.database;

import java.util.ArrayList;
import java.util.List;

import procurementsys.model.ProductOffer;
import procurementsys.model.Tag;

public class MySQLTagDAO implements TagDAO {

	@Override
	public void add(Tag tag) {
		// TODO - DEVS implement this
		
	}
	
	@Override
	public List<Tag> getAll() {
		// TODO - DEVS implement this properly
		List<Tag> ret = new ArrayList<>();
		ret.add(new Tag("BALLPEN"));
		ret.add(new Tag("PENCIL"));
		ret.add(new Tag("FOLDER"));
		ret.add(new Tag("SUPPLIES"));
		ret.add(new Tag("PRODUCT"));
		ret.add(new Tag("GEL"));
		return ret;
	}

	@Override
	public boolean isEmpty() {
		return getAll().size() == 0;
	}

	@Override
	public List<Tag> getAll(String tagNameFilter) {
		// TODO - DEVS implement this
		List<Tag> ret = new ArrayList<>();
		
		for (Tag x : getAll()) {
			String tagName = x.getName().toLowerCase();
			if (tagName.contains(tagNameFilter.toLowerCase())) {
				ret.add(x);
			}
		}
		
		return ret;
	}

	@Override
	public void tagProductOffer(ProductOffer productOffer, List<Tag> tags) {
		// TODO Auto-generated method stub
		
	}

}
