package procurementsys;

import procurementsys.model.Delivery;
import procurementsys.model.ProductOffer;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

/**
 * @author Jan Tristan Milan
 */

public class CheckDeliveryDetailsController extends Controller {
	@FXML Text deliveredDateTimeText;
	@FXML ListView<String> productsDeliveredListView;
	
	public void initialize(Delivery delivery) {
		deliveredDateTimeText.setText(delivery.getDeliveryDateTime().toString());
		for (ProductOffer x : delivery.getProductOffersDelivered()) {
			productsDeliveredListView.getItems()
				.add(x.getProduct() + " x" + delivery.getQuantityDelivered(x));
		}
	}
}
