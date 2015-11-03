package procurementsys.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Jan Tristan Milan
 */

public class Product {
	private StringProperty name;
	
	public Product(String name) {
		this.name = new SimpleStringProperty(name);
	}
	
	public String getName() {
		return name.get();
	}
	
	@Override
	public String toString() {
		return name.get();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) 
			return false;
		
		Product p = (Product) o;
		return name.get().equals(p.getName());
	}
}
