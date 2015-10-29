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
import procurementsys.model.database.MySQLProductDAO;
import procurementsys.model.database.ProductDAO;

/**
 * @author Jan Tristan Milan
 */

public class AddNewOrderProductSelectionController extends Controller {
	@FXML private ListView<Product> productListView;
	private Supplier selectedSupplier;
	
	@FXML private void initialize() {
		ProductDAO productDAO = new MySQLProductDAO();
		productListView.getItems().addAll(productDAO.getAll());
		productListView.getSelectionModel().select(0);
		productListView.getSelectionModel()
		.setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	public void initialize(Supplier selectedSupplier) {
		this.selectedSupplier = selectedSupplier;
	}
	
	@FXML protected void handleConfirmProductsBtnPress(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass()
				.getResource("view/add_new_order_details_view.fxml"));
		
		Parent root = loader.load();
		Stage stage = getStage(event);
		stage.setScene(new Scene(root));
		stage.setTitle("Enter Order Details");
		
		AddNewOrderDetailsController controller =
				loader.<AddNewOrderDetailsController>getController();
		List<Product> selectedProducts =
				productListView.getSelectionModel().getSelectedItems();
		controller.initialize(selectedSupplier, selectedProducts);
	}
	
	@FXML protected void handleCancelBtnPress(ActionEvent event) {
		getStage(event).close();
	}
}
