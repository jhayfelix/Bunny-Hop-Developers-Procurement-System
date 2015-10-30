package procurementsys.controller;

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
import javafx.stage.Stage;
import procurementsys.model.Order;
import procurementsys.model.ProductOffer;

/**
 * @author Jan Tristan Milan
 */

public class AddNewDeliveryController extends Controller {
	@FXML private DatePicker deliveryDatePicker;
	@FXML private ChoiceBox<String> hourChoiceBox;
	@FXML private ChoiceBox<String> minuteChoiceBox;
	@FXML private ChoiceBox<String> amPmChoiceBox;
	@FXML ListView<HBox> productOfferListView;
	private List<TextField> quantityTextFields;
	
	public void initialize(Order order) {
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
		
		this.quantityTextFields = new ArrayList<>();
		this.displayProductsOrdered(order);
	}
	
	
	private void displayProductsOrdered(Order order ) {
		List<ProductOffer> orderedProducts = new ArrayList<>(order.getProductsOffersOrdered());
		for (int i = 0; i < orderedProducts.size(); i++) {
			HBox hBox = new HBox();
			Label productLabel 
				= new Label(orderedProducts.get(i)
						.getProduct().getName() + " x ");
			TextField textField = new TextField();
			textField.setPrefWidth(50);
			quantityTextFields.add(textField);
			// TODO - Better if adding is done outside the
			// loop. This can be done using addAll()
			hBox.getChildren().add(productLabel);
			hBox.getChildren().add(textField);
			productOfferListView.getItems().add(hBox);
		}
	}
	
	@FXML 
	protected void handleConfirmDeliveryDetails(ActionEvent event) throws IOException {
		// TODO - Save new payment to the database
		Parent root = FXMLLoader.load(getClass().getResource("view/add_new_delivery_success_dialog.fxml"));
		Stage stage = getStage(event);
		stage.setScene(new Scene(root));
		stage.setTitle("Success");
	}
	
	@FXML protected void handleCancelBtnPress(ActionEvent event) {
		getStage(event).close();
	}
	
}
