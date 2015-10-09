package procurementsys.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Jan Tristan Milan
 */

public class ProductOffer {
	private ObjectProperty<Product> product;
	private ObjectProperty<Supplier> supplier;
	private double cost;
	
	public ProductOffer(Product product, Supplier supplier,
			double cost) {
		this.product = new SimpleObjectProperty<Product>(product);
		this.supplier = new SimpleObjectProperty<Supplier>(supplier);
		this.cost = cost;
	}
	
	public Product getProduct() {
		return product.get();
	}
	
	public Supplier getSupplier() {
		return supplier.get();
	}
	
	public Double getCost() {
		return cost;
	}
	
	public String getContactNumber() {
		return supplier.get().getContactNumber();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) 
			return false;
		
		ProductOffer po = (ProductOffer) o;
		return (product.equals(po.getProduct()) 
				&& supplier.equals(po.getSupplier()));
	}
}
