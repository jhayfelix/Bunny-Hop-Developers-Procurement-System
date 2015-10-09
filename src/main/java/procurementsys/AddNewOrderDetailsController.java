package procurementsys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;

/**
 * @author Jan Tristan Milan
 */

public class AddNewOrderDetailsController extends Controller {
	@FXML private DatePicker orderDatePicker;
	@FXML private ChoiceBox<String> hourChoiceBox;
	@FXML private ChoiceBox<String> minuteChoiceBox;
	@FXML private ChoiceBox<String> amPmChoiceBox;
	@FXML private Text supplierText;
	@FXML private ListView<HBox> productOfferListView;
	private List<TextField> costTextFields;
	private Supplier selectedSupplier;
	private List<Product> selectedProducts;
	
	
	public void initialize(Supplier selectedSupplier, List<Product> selectedProducts) {
		for (int i = 0; i < 12; i++) {
			String hour = (i + 1) + "";
			hourChoiceBox.getItems()
				.add((hour.length() == 1) ? hour : hour); // just add "0" + to the first condition
														  // if desired
		}
		hourChoiceBox.getSelectionModel().select(0);
		
		for (int i = 0; i < 59; i++) {
			String minute = (i + 1) + "";
			minuteChoiceBox.getItems()
				.add((minute.length() == 1) ? "0" + minute : minute);
		}
		minuteChoiceBox.getSelectionModel().select(0);
		
		amPmChoiceBox.getItems().add("A.M.");
		amPmChoiceBox.getItems().add("P.M.");
		amPmChoiceBox.getSelectionModel().select(0);
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
	
	@FXML protected void handleConfirmOrderDetails(ActionEvent event) throws IOException {
		for (int i = 0; i < selectedProducts.size(); i++) {
			ProductOffer x 
			= new ProductOffer(selectedProducts.get(i), selectedSupplier,
					Double.parseDouble(costTextFields.get(i).getText()));
			// TODO - store in the database
		}
		Parent root = FXMLLoader.load(getClass().getResource("view/add_new_order_success_dialog.fxml"));
		Stage stage = getStage(event);
		stage.setScene(new Scene(root));
		stage.setTitle("Success");
	}
	
	@FXML protected void handleCancelBtnPress(ActionEvent event) {
		getStage(event).close();
	}
	
}
