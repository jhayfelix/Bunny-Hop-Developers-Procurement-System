package procurementsys.controller;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import procurementsys.model.Product;
import procurementsys.model.Supplier;

/**
 * @author Jan Tristan Milan
 */

public class AssignProductSelectionController extends Controller {
	@FXML private ListView<Product> productListView;
	private Supplier selectedSupplier;
	
	@FXML private void initialize() {
		productListView.getItems().addAll(parseProductList());
		productListView.getSelectionModel().select(0);
		productListView.getSelectionModel()
		.setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	public void initialize(Supplier selectedSupplier) {
		this.selectedSupplier = selectedSupplier;
	}
	
	@FXML protected void handleConfirmProductsBtnPress(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass()
				.getResource("view/assign_productoffer_details_view.fxml"));
		
		Parent root = loader.load();
		Stage stage = getStage(event);
		stage.setScene(new Scene(root));
		stage.setTitle("Enter Product Offer Details");
		
		AssignProductOfferDetailsController controller =
				loader.<AssignProductOfferDetailsController>getController();
		List<Product> selectedProducts =
				productListView.getSelectionModel().getSelectedItems();
		controller.initialize(selectedSupplier, selectedProducts);
	}
	
	@FXML protected void handleCancelBtnPress(ActionEvent event) {
		getStage(event).close();
	}
	
}
