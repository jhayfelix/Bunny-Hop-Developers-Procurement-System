package procurementsys.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Jan Tristan Milan
 */

public class AssignProductOfferDetailsController extends Controller {
	@FXML private Text supplierText;
	@FXML private ListView<HBox> productOfferListView;
	private List<TextField> costTextFields;
	private Supplier selectedSupplier;
	private List<Product> selectedProducts;
	
	public void initialize(Supplier selectedSupplier,
			List<Product> selectedProducts) {
		this.selectedSupplier = selectedSupplier;
		supplierText.setText(selectedSupplier.toString());
		this.selectedProducts = new ArrayList<Product>(selectedProducts);
		this.costTextFields = new ArrayList<>();
		displaySelectedProducts();
	}
	
	private void displaySelectedProducts() {
		for (int i = 0; i < selectedProducts.size(); i++) {
			HBox hBox = new HBox();
			Label productLabel 
				= new Label(selectedProducts.get(i).getName()
						+ " x ");
			TextField textField = new TextField();
			textField.setPrefWidth(50);
			costTextFields.add(textField);
			// TODO - Better if adding is done outside the
			// loop. This can be done using addAll()
			hBox.getChildren().add(productLabel);
			hBox.getChildren().add(textField);
			productOfferListView.getItems().add(hBox);
		}
	}
	
	@FXML protected void handleConfirmProductOfferDetails(ActionEvent event) throws IOException {
		for (int i = 0; i < selectedProducts.size(); i++) {
			/*ProductOffer x 
			= new ProductOffer(selectedProducts.get(i), selectedSupplier,
					Double.parseDouble(costTextFields.get(i).getText()));
			// TODO - store in the database */
		}
		
		Parent root = FXMLLoader.load(getClass().getResource("view/assign_product_success_dialog.fxml"));
		Stage stage = getStage(event);
		stage.setScene(new Scene(root));
		stage.setTitle("Success");
	}
	
	@FXML protected void handleCancelBtnPress(ActionEvent event) {
		getStage(event).close();
	}

}
