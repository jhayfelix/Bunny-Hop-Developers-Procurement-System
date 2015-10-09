package procurementsys.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
	private List<Payment> payments;

	public Order(LocalDateTime orderDateTime, Supplier supplier,
			Map<ProductOffer, Integer> productOffersOrdered) {
		this.orderDateTime = new SimpleObjectProperty<LocalDateTime>(
				orderDateTime);
		this.supplier = new SimpleObjectProperty<Supplier>(supplier);
		this.productOffersOrdered = new HashMap<>(productOffersOrdered);
		this.deliveries = new ArrayList<>();
		this.payments = new ArrayList<>();
	}

	public Supplier getSupplier() {
		return supplier.get();
	}

	public LocalDateTime getOrderDateTime() {
		return orderDateTime.get();
	}

	public OrderDeliveryStatus getOrderDeliveryStatus() {
		return OrderDeliveryStatus.CANCELLED;
	}

	public double getTotalAmount() {
		double totalAmount = 0;
		for (ProductOffer x : productOffersOrdered.keySet()) {
			totalAmount = totalAmount
					+ (x.getCost() * productOffersOrdered.get(x));
		}
		return totalAmount;
	}

	public double getUnpaidAmount() {
		double paidAmount = 0;
		for (Payment x : payments) {
			paidAmount += x.getPaidAmount();
		}
		return getTotalAmount() - paidAmount;
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
		return ret; // TODO - derive from the deliveries
	}

	public void addDelivery(Delivery delivery) {
		deliveries.add(delivery);
	}

	public List<Delivery> getDeliveries() {
		return new ArrayList<>(deliveries);
	}

	public void addPayment(Payment payment) {
		payments.add(payment);
	}

	public List<Payment> getPayments() {
		return new ArrayList<>(payments);
	}

}
