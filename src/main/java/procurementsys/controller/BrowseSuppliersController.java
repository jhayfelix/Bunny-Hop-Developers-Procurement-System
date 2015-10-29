package procurementsys.controller;

import java.io.IOException;

import procurementsys.model.Supplier;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * @author Jan Tristan Milan
 */
public class BrowseSuppliersController extends Controller {
	@FXML private ListView<Supplier> supplierList;
	@FXML private Text nameText;
	@FXML private Text contactNumberText;
	@FXML private Text activityText;
	
	@FXML private void initialize() {
		supplierList.getSelectionModel().selectedItemProperty()
			.addListener(new ChangeListener<Supplier>() {
			@Override
			public void changed(ObservableValue<? extends Supplier> observable,
					Supplier oldValue, Supplier newValue) {
				nameText.setText(newValue.getName());
				contactNumberText.setText(newValue.getContactNumber());
				activityText.setText((newValue.isActive()) ? "ACTIVE" : "INACTIVE");
			}
		});
		supplierList.getItems().addAll(parseSupplierList());
		supplierList.getSelectionModel().select(0);
	}
	
	@FXML protected void handleViewProductsSoldBtnPress(ActionEvent event)
			throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass()
				.getResource("view/browse_products_offered_view.fxml"));
		
		Parent root = loader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.setTitle(nameText.getText() + " - Products");
		stage.setResizable(false);
		stage.show();
		
		BrowseProductsOfferedController controller = 
				loader.<BrowseProductsOfferedController>getController();
		controller.initialize(supplierList.getSelectionModel().getSelectedItem());
	}
    
	@FXML protected void handleAssignProductBtnPress(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("view/assign_product_selection_view.fxml"));
    	Parent root = loader.load();
    	Stage stage = new Stage();
    	stage.setScene(new Scene(root));
    	stage.setTitle("Select Products");
    	AssignProductSelectionController controller = 
    			loader.<AssignProductSelectionController>getController();
    	controller.initialize(supplierList.getSelectionModel().getSelectedItem());
    	stage.show();
	}
	
	@FXML protected void handleToggleActivityStatusBtnPress(ActionEvent event) {
		// TODO - toggle the activity in the database
	}
	

}
