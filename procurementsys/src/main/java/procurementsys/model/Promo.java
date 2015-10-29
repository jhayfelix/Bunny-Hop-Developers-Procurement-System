package procurementsys.model;

public class Promo {
	private int qtyNeeded;
	private double discount;
	
	public Promo(int qtyNeeded, double discount) {
		this.qtyNeeded = qtyNeeded;
		this.discount = discount;
	}
	
	public int getQuantityNeeded() {
		return qtyNeeded;
	}
	
	public double getDiscount() {
		return discount;
	}
	
	public boolean isQualified(int qty) {
		return qty >= qtyNeeded;
	}
	
	public double applyDiscount(double amount) {
		double multiplier = 1 - discount;
		return amount *= multiplier;
	}
}
