package procurementsys.model;

/**
 * @author Jan Tristan Milan
 */

public enum OrderDeliveryStatus {
	ONGOING, COMPLETED, CANCELLED;
	
	@Override
	public String toString() {
		return this.name();
	}
}
