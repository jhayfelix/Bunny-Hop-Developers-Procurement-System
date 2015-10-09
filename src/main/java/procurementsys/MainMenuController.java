package procurementsys;
import java.io.IOException;
import java.time.LocalDateTime;

import procurementsys.model.OrderDeliveryStatus;
import procurementsys.model.Order;
import procurementsys.model.Supplier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * @author Jan Tristan Milan
 */

public class MainMenuController extends Controller {
	@FXML private TableView<Order> orderTable;
	@FXML private TableColumn<Order, LocalDateTime> dateTimeOrderedColumn;
	@FXML private TableColumn<Order, Supplier> supplierColumn;
	@FXML private TableColumn<Order, OrderDeliveryStatus> deliveryStatusColumn;
	@FXML private TableColumn<Order, Double> unpaidAmountColumn;
	
	//private ObservableList<Order> orders;
	
	public MainMenuController() {
		//orders = FXCollections.observableArrayList();
	}
	
	@FXML private void initialize() {
		orderTable.setRowFactory( tableView -> {
		    TableRow<Order> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
		        	try {
			        	FXMLLoader loader = new FXMLLoader(getClass()
			        			.getResource("view/check_order_details_view.fxml"));
						Parent root = loader.load();
						Stage stage = new Stage();
						stage.setScene(new Scene(root));
						stage.show();
						stage.setTitle("Order Details");
						stage.setResizable(false);
						CheckOrderDetailsController controller 
							= loader.<CheckOrderDetailsController>getController();
						controller.initialize(row.getItem());
					} catch (Exception e) {
						e.printStackTrace();
					}
		        }
		    });
		    return row ;
		});
		
		dateTimeOrderedColumn.setCellValueFactory(new PropertyValueFactory<Order,
				LocalDateTime>("orderDateTime"));
		supplierColumn.setCellValueFactory(new PropertyValueFactory<Order,
				Supplier>("supplier"));
		deliveryStatusColumn.setCellValueFactory(new PropertyValueFactory<Order,
				OrderDeliveryStatus>("orderDeliveryStatus"));
		unpaidAmountColumn.setCellValueFactory(
				new PropertyValueFactory<Order, Double>("unpaidAmount"));
		orderTable.getItems().setAll(parseOrderList());
	}
	
    @FXML protected void handleViewSuppliersBtnPress(ActionEvent event)
    		throws IOException {
    	loadNewStage("Kimson Trading - Suppliers",
    			"view/browse_suppliers_view.fxml", false);
    }
    
    @FXML protected void handleViewProductsBtnPress(ActionEvent event) 
    		throws IOException {
    	loadNewStage("Kimson Trading - Products",
    			"view/browse_products_view.fxml", false);
    }
    
    @FXML protected void handleAddNewSupplierBtnPress(ActionEvent event) throws IOException {
    	loadNewStage("Enter supplier details",
    			"view/add_new_supplier_details_dialog.fxml", false);
    }
    
    @FXML protected void handleAddNewProductBtnPress(ActionEvent event) throws IOException {
    	loadNewStage("Enter product details",
    			"view/add_new_product_details_dialog.fxml", false);
    }
    
    @FXML protected void handleAddNewOrderBtnPress(ActionEvent event) throws IOException {
    	// supplier -> product_offers (only show product name) -> quantity of each product and date
    	loadNewStage("Select Supplier",
    			"view/add_new_order_supplier_selection_view.fxml", false);
    }
    
}
