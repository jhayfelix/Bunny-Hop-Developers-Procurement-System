package procurementsys.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Jan Tristan Milan
 */

public class Order {
	private ObjectProperty<LocalDateTime> orderDateTime;
	private ObjectProperty<Supplier> supplier;
	private Map<ProductOffer, Integer> productOffersOrdered; // <ProductOffer,
																// Quantity>
	private List<Delivery> deliveries;

	public Order(LocalDateTime orderDateTime, Supplier supplier,
			Map<ProductOffer, Integer> productOffersOrdered) {
		this.orderDateTime = new SimpleObjectProperty<LocalDateTime>(
				orderDateTime);
		this.supplier = new SimpleObjectProperty<Supplier>(supplier);
		this.productOffersOrdered = new HashMap<>(productOffersOrdered);
		this.deliveries = new ArrayList<>();
	}

	public Supplier getSupplier() {
		return supplier.get();
	}

	public LocalDateTime getOrderDateTime() {
		return orderDateTime.get();
	}

	public OrderDeliveryStatus getOrderDeliveryStatus() {
		return OrderDeliveryStatus.ONGOING; // TODO - implement this
	}


	public Set<ProductOffer> getProductsOffersOrdered() {
		return productOffersOrdered.keySet();
	}

	public int quantityOrdered(ProductOffer productOffer) {
		return productOffersOrdered.get(productOffer);
	}

	public int quantityDelivered(ProductOffer productOffer) {
		int ret = 0;
		for (Delivery x : deliveries) {
			ret += x.getQuantityDelivered(productOffer);
		}
		return ret;
	}

	public void addDelivery(Delivery delivery) {
		deliveries.add(delivery);
	}

	public List<Delivery> getDeliveries() {
		return new ArrayList<>(deliveries);
	}

	public ProductOffer getProductOffer(Product product) {
		for (ProductOffer po : productOffersOrdered.keySet()) {
			if (po.getProduct().equals(product)) {
				return po;
			}
		}
		return null;
	}
	
}
