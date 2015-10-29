package procurementsys.model;

import java.time.LocalDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CostChange {
	private ObjectProperty<LocalDateTime> changeDateTime;
	private double cost;
	
	public CostChange(LocalDateTime changeDateTime, double cost) {
		this.changeDateTime = new SimpleObjectProperty<>(changeDateTime);
		this.cost = cost;
	}
	
	public LocalDateTime getChangeDateTime() {
		return changeDateTime.get();
	}
	
	public double getCost() {
		return cost;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		
		CostChange c = (CostChange) o;
		return cost == c.getCost() &&
				getChangeDateTime().equals(c.getChangeDateTime());
	}
	
	public boolean isFuture() {
		return changeDateTime.get().isAfter(LocalDateTime.now());
	}
	
	
	@Override
	public String toString() {
		return cost + "";
	}
}
