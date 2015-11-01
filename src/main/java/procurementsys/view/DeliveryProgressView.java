package procurementsys.view;

import procurementsys.model.Order;
import procurementsys.model.ProductOffer;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class DeliveryProgressView extends ScrollPane {
	@SuppressWarnings("unused")
	private Order order;
	private VBox progressVBox;
	
	public DeliveryProgressView() {
		this.progressVBox = new VBox();
		this.progressVBox.setSpacing(10);
		setContent(progressVBox);
	}
	
	public DeliveryProgressView(Order order) {
		setOrder(order);
	}
	
	public void setOrder(Order order) {
		this.order = order;
		
		progressVBox.getChildren().clear();
		if (order != null) {
			for (ProductOffer po :
				order.getProductsOffersOrdered()) {			
				
				int qtyDelivered = order.quantityDelivered(po);
				int qtyOrdered = order.quantityOrdered(po);
				
				ProgressIndicatorBar progressBar = 
						new ProgressIndicatorBar(new SimpleDoubleProperty(qtyDelivered), qtyOrdered,
								po.getProduct() + " (" + qtyDelivered + " out of " + qtyOrdered + ")");
				
				progressVBox.getChildren().add(progressBar);
			}
		}
	}
}
