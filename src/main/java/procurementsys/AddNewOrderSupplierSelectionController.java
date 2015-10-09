package procurementsys;

import java.io.IOException;

import procurementsys.model.Supplier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * @author Jan Tristan Milan
 */

public class AddNewOrderSupplierSelectionController extends Controller {
	@FXML private ListView<Supplier> supplierListView;

	@FXML private void initialize() {
		supplierListView.getItems().addAll(parseSupplierList());
		supplierListView.getSelectionModel().select(0);
	}
	
	@FXML protected void handleConfirmSupplierBtnPress(ActionEvent event) 
			throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass()
				.getResource("view/add_new_order_product_selection_view.fxml"));

		Parent root = loader.load();	
		Stage stage = getStage(event);
		stage.setScene(new Scene(root));
		stage.setTitle("Select Products");

		AddNewOrderProductSelectionController controller = 
					loader.<AddNewOrderProductSelectionController>getController();
		Supplier selectedSupplier = supplierListView
				.getSelectionModel().getSelectedItem();
		controller.initialize(selectedSupplier);
	}
	
	@FXML protected void handleCancelBtnPress(ActionEvent event) {
		getStage(event).close();
	}
	
}