package procurementsys.model;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Jan Tristan Milan
 */

public class Delivery {
	private ObjectProperty<LocalDateTime> deliveryDateTime;
	private Map<ProductOffer, Integer> productOffersDelivered;
	
	public Delivery(LocalDateTime deliveryDateTime,
			Map<ProductOffer, Integer> productOffersDelivered) {
		this.deliveryDateTime =
				new SimpleObjectProperty<LocalDateTime>(deliveryDateTime);
		this.productOffersDelivered = new HashMap<>(productOffersDelivered);
	}
	
	public LocalDateTime getDeliveryDateTime() {
		return deliveryDateTime.get();
	}
	
	public Set<ProductOffer> getProductOffersDelivered() {
		return productOffersDelivered.keySet();
	}
	
	public int getQuantityDelivered(ProductOffer productOffer) {
		return productOffersDelivered.get(productOffer);
	}
	
	public String getDeliveredProducts() {
		StringBuilder sb = new StringBuilder();
		for (ProductOffer x : productOffersDelivered.keySet()) {
			sb.append(x.getProduct() + " x" + getQuantityDelivered(x) + ", ");
		}
		return (sb.length() == 0) ? sb.toString() : sb.substring(0, sb.length() - 2);
	}
}
