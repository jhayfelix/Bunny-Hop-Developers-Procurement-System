package procurementsys.model;

import java.time.LocalDateTime;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Jan Tristan Milan
 */

public class Payment {
	private ObjectProperty<LocalDateTime> paidDateTime;
	private DoubleProperty paidAmount;	
	
	public Payment(LocalDateTime paidDateTime, double paidAmount) {
		this.paidDateTime =
				new SimpleObjectProperty<LocalDateTime>(paidDateTime);
		this.paidAmount = new SimpleDoubleProperty(paidAmount);
	}
	
	public LocalDateTime getPaidDateTime() {
		return paidDateTime.get();
	}
	
	public double getPaidAmount() {
		return paidAmount.get();
	}
	
}
