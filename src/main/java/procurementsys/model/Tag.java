package procurementsys.model;

public class Tag {
	// Edit default tag
	public static Tag DEFAULT_TAG = new Tag("PRODUCT");
	
	private String name;
	
	public Tag(String name) {
		this.name = name.toUpperCase();
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
