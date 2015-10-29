package procurementsys.controller;

import java.io.IOException;

import org.controlsfx.control.Notifications;

import procurementsys.model.Supplier;
import procurementsys.model.database.MySQLSupplierDAO;
import procurementsys.model.database.SupplierDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
		SupplierDAO supplierDAO = new MySQLSupplierDAO();
		supplierListView.getItems().addAll(supplierDAO.getAll());
		if (supplierListView.getItems().size() > 0) {
			supplierListView.getSelectionModel().select(0);
		}
	}
	
	@FXML protected void handleConfirmSupplierBtnPress(ActionEvent event) 
			throws IOException {
		Supplier selectedSupplier = supplierListView
				.getSelectionModel().getSelectedItem();
		if (selectedSupplier == null) {
			String errorMsg = "Please select a supplier from the list.";
			Notifications.create().title("Error").text(errorMsg)
			.position(Pos.TOP_RIGHT).showError();
		} else {
			FXMLLoader loader = new FXMLLoader(getClass()
					.getResource("/procurementsys/view/add_new_order_product_selection_view.fxml"));
			Parent root = loader.load();	
			Stage stage = getStage(event);
			stage.setScene(new Scene(root));
			stage.setTitle("Select Products");

			AddNewOrderProductSelectionController controller = 
						loader.<AddNewOrderProductSelectionController>getController();
			controller.initialize(selectedSupplier);
		}
	}
	
	@FXML protected void handleCancelBtnPress(ActionEvent event) {
		getStage(event).close();
	}
	
}