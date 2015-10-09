package procurementsys;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;

/**
 * @author Jan Tristan Milan
 */

public class BrowseProductsOfferedController extends Controller {
	@FXML private TableView<ProductOffer> productOfferTable;
	@FXML private TableColumn<ProductOffer, Product> productColumn;
	@FXML private TableColumn<ProductOffer, Double> costColumn;
	
	@FXML private void initialize() {
		productColumn.setCellValueFactory(
				new PropertyValueFactory<ProductOffer, Product>("product"));
		costColumn.setCellValueFactory(
				new PropertyValueFactory<ProductOffer, Double>("cost"));
	}
	
	public void initialize(Supplier supplier) {		
		productOfferTable.getItems().addAll(parseProductOfferList(supplier));
	}
}
