package procurementsys.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Jan Tristan Milan
 */

public class ProductOffer {
	private ObjectProperty<Product> product;
	private ObjectProperty<Supplier> supplier;
	private List<CostChange> costChanges;
	private List<Tag> tags;
	private Promo promo;
	
	public ProductOffer(Product product, Supplier supplier,
			List<CostChange> costChanges) {
		this.product = new SimpleObjectProperty<Product>(product);
		this.supplier = new SimpleObjectProperty<Supplier>(supplier);
		this.costChanges = new ArrayList<>(costChanges);
		this.tags = new ArrayList<Tag>();
		this.tags.add(Tag.DEFAULT_TAG);
		this.promo = null;
	}
	
	public ProductOffer(Product product, Supplier supplier,
			List<CostChange> costChanges, List<Tag> tags) {
		this(product, supplier, costChanges);
		this.tags = new ArrayList<>(tags);
	}
	
	public Product getProduct() {
		return product.get();
	}
	
	public Supplier getSupplier() {
		return supplier.get();
	}
	
	public CostChange getCurrentCostChange() {
		CostChange currCostChange = costChanges.get(0);
		
		for (int i = 1; i < costChanges.size(); i++) {
			CostChange c = costChanges.get(i);
			if (!c.isFuture() && c.getChangeDateTime()
					.isAfter(currCostChange.getChangeDateTime())) {
				currCostChange = c;
			}
		}
		
		return currCostChange;
	}
	
	public CostChange getUpcomingCostChange() {
		CostChange upcomingCostChange = null;
		for (CostChange c : costChanges) {
			if (c.isFuture()) { // TODO - FIIIIIIIIIIIX THIS condition is incomplete
				upcomingCostChange = c;
			}
		}
		return upcomingCostChange;
	}
	
	public Double getCurrentCost() {
		return getCurrentCostChange().getCost();
	}
	
	public Double getCurrentCost(int qty) {
		double totalCost = getCurrentCost() * qty;
		if (promo != null && promo.isQualified(qty)) {
			totalCost = promo.applyDiscount(totalCost);
		}
		return totalCost;
	}
	
	public Double getUpcomingCost() {
		return getUpcomingCostChange().getCost();
	}
	
	public Double getUpcomingCost(int qty) {
		double totalCost = getUpcomingCost() * qty;
		if (promo != null && promo.isQualified(qty)) {
			totalCost = promo.applyDiscount(totalCost);
		}
		return totalCost;
	}
	
	public String getUpcomingCostChangeDateStr() {
		return getUpcomingCostChange().getChangeDateTime().toString();
	}
	
	public void addCostChange(CostChange costChange) {
		if (!costChanges.contains(costChange)) {
			costChanges.add(costChange);
		}
	}
	
	public void setPromo(Promo promo) {
		this.promo = promo;
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