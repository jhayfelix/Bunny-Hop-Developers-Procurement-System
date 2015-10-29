package procurementsys.model;

/**
 * @author Jan Tristan Milan
 */

public class Supplier {
	private String name;
	private String contactNumber;
	private boolean active;
	
	public Supplier(String name, String contactNumber) {
		this.name = name;
		this.contactNumber = contactNumber;
		this.active = true;
	}
	
	public String getName() {
		return name;
	}
	
	public String getContactNumber() {
		return contactNumber;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		
		Supplier s = (Supplier) o;
		return name.equals(s.getName());
	}
}
