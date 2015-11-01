package procurementsys.model.database;

import java.util.List;

import procurementsys.model.Tag;

public interface TagDAO {
	void add(Tag tag);
	List<Tag> getAll();
	List<Tag> getAll(String tagNameFilter);
	boolean isEmpty();
}
