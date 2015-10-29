package procurementsys.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

import procurementsys.model.Delivery;
import procurementsys.model.Order;
import procurementsys.model.ProductOffer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Jan Tristan Milan
 */

public class CheckOrderDetailsController extends Controller {
	private Order order;
	
	// info of order
	@FXML private Text dateTimeOrderedText;
	@FXML private Text supplierText;
	@FXML private ListView<String> deliveredProductsListView; // format: product_name - 10 out of 100
	@FXML private Text orderDeliveryStatusText; // ONGOING, COMPLETED, CANCELLED - make an Enum
	@FXML private Text unpaidCostText; // format: 1000 out of 5000
	
	// table of deliveries
	@FXML private TableView<Delivery> deliveriesTableView;
	@FXML private TableColumn<Delivery, LocalDateTime> deliveryDateTimeColumn;
	@FXML private TableColumn<Delivery, String> deliveredProductsColumn; // just create a property shorthand-notation
	
	
	public void initialize(Order order) {
		this.order = order;
		
		// info of order
		dateTimeOrderedText.setText(order.getOrderDateTime() + "");
		supplierText.setText(order.getSupplier() + "");
		displayDeliveredProducts(order);
		orderDeliveryStatusText.setText(order.getOrderDeliveryStatus() + "");
		
		// table of deliveries
		deliveriesTableView.setRowFactory(tableView -> {
		    TableRow<Delivery> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
		        	try {
			        	FXMLLoader loader = new FXMLLoader(getClass()
			        			.getResource("view/check_delivery_details_view.fxml"));
						Parent root = loader.load();
						Stage stage = new Stage();
						stage.setScene(new Scene(root));
						stage.show();
						stage.setResizable(false);
						CheckDeliveryDetailsController controller 
							= loader.<CheckDeliveryDetailsController>getController();
						controller.initialize(row.getItem());
					} catch (Exception e) {
						e.printStackTrace();
					}
		        }
		    });
		    return row;
		});
		deliveryDateTimeColumn.setCellValueFactory(new PropertyValueFactory<Delivery, LocalDateTime>("deliveryDateTime"));
		deliveredProductsColumn.setCellValueFactory(new PropertyValueFactory<Delivery, String>("deliveredProducts"));
		deliveriesTableView.getItems().addAll(order.getDeliveries());
	}
	
	private void displayDeliveredProducts(Order order) {
		Set<ProductOffer> productOffers = order.getProductsOffersOrdered();
		for (ProductOffer x : productOffers) {
			deliveredProductsListView.getItems().add(x.getProduct() + " - " 
					+ order.quantityDelivered(x) + " out of " + order.quantityOrdered(x));
		}
	}
	
	@FXML protected void handleAddNewDeliveryBtnPress(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass()
				.getResource("view/add_new_delivery_details_view.fxml"));
		Parent root = loader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.setTitle("Enter delivery details");
		stage.show();
		
		AddNewDeliveryController controller = 
				loader.<AddNewDeliveryController>getController();
		controller.initialize(order);
	}
	
	@FXML protected void handleAddNewPaymentBtnPress(ActionEvent event) throws IOException {
		loadNewStage("Enter payment details", "view/add_new_payment_details_view.fxml", false);
	}
	
	@FXML protected void handleCancelOrderBtnPress(ActionEvent event) {
		// TODO - cancel the order 
	}
}
