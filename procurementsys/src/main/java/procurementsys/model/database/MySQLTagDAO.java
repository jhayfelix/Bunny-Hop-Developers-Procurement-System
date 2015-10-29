package procurementsys.model.database;

import java.util.ArrayList;
import java.util.List;

import procurementsys.model.Tag;

public class MySQLTagDAO implements TagDAO {

	@Override
	public void add(Tag tag) {
		// TODO Auto-generated method stub
		
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

}
