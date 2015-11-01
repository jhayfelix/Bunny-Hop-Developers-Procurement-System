package procurementsys.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import jfxtras.scene.control.LocalDateTimeTextField;
import procurementsys.model.Delivery;
import procurementsys.model.Order;
import procurementsys.model.OrderDeliveryStatus;
import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.database.MySQLOrderDAO;
import procurementsys.model.database.MySQLProductDAO;
import procurementsys.model.database.OrderDAO;
import procurementsys.model.database.ProductDAO;
import procurementsys.view.SoftwareNotification;

public class AddDeliveryController {
	private static LocalDateTimeTextField dateTimeField;
	private static TableView<Order> orderTable;
	private static ListView<HBox> qtyTextFields;
	
	@SuppressWarnings("unchecked")
	public static void run() {
		Dialog<Map<String, Object>> dialog  = new Dialog<>();
		dialog.setTitle("Add Delivery");
		dialog.setHeaderText("Enter delivery details");
		dialog.getDialogPane().getButtonTypes()
			.addAll(ButtonType.OK, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 20, 10, 10));
		
		Label orderLbl = new Label("Order:");
		orderLbl.setStyle("-fx-font-weight: bold");
		grid.add(orderLbl, 0, 2);
		
		orderTable = createTable();
		grid.add(orderTable, 0, 3);
		
		initializeDateTimeSelection(grid);
		initializeQtyTextFields(grid);
		orderTable.getSelectionModel().select(0);
		
		dialog.getDialogPane().setContent(grid);
		
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == ButtonType.OK) {
		    	Map<String, Object> map = new HashMap<>();
		    	map.put("DATE_DELIVERED", getDateTimeDelivered());
		    	map.put("ORDER", getSelectedOrder());
		    	map.put("ORDERED_PRODUCTOFFERS", getDeliveredProductOffers());
		    	return map;
		    }
		    return null;
		});
					
		final Button btOk = (Button) dialog.getDialogPane()
				.lookupButton(ButtonType.OK);
		btOk.addEventFilter(ActionEvent.ACTION, event -> {
			if (!isValidDateTime() || !isValidProductQtyMap() ) {
				event.consume();
			}
		});
		
		Optional<Map<String, Object>> result = dialog.showAndWait();
		if (result.isPresent()) {
			Map<String, Object> map = result.get();
			
			LocalDateTime dateTime = (LocalDateTime) map.get("DATE_ORDERED");
			Order order = (Order) map.get("ORDER");
			Map<ProductOffer, Integer> deliveredProductOffers = 
					(Map<ProductOffer, Integer>) map.get("ORDERED_PRODUCTOFFERS");
			
			Delivery delivery = new Delivery(dateTime, deliveredProductOffers);
			OrderDAO orderDAO = new MySQLOrderDAO();
			orderDAO.addDelivery(order, delivery);
			SoftwareNotification.notifySuccess("The delivery has been succesfully "
					+ "added to the system.");
		}
	}
	
	private static TableView<Order> createTable() {
		TableView<Order> orderTable = new TableView<>();
		orderTable.setMinWidth(460);
		initializeTableColumns(orderTable);
		
		OrderDAO orderDAO = new MySQLOrderDAO();
		orderTable.getItems().setAll(orderDAO.getAll());
		return orderTable;
	}
	
	@SuppressWarnings("unchecked")
	private static void initializeTableColumns(TableView<Order> table) {
		TableColumn<Order, LocalDateTime> dateTimeOrderedColumn = new TableColumn<>();
		dateTimeOrderedColumn.setText("DateTime Ordered");
		
		TableColumn<Order, Supplier> supplierColumn = new TableColumn<>();
		supplierColumn.setText("Supplier");
		
		TableColumn <Order, OrderDeliveryStatus> deliveryStatusColumn = new TableColumn<>();
		deliveryStatusColumn.setText("Delivery Status");
		
		dateTimeOrderedColumn.setCellValueFactory(new PropertyValueFactory<Order,
				LocalDateTime>("orderDateTime"));
		supplierColumn.setCellValueFactory(new PropertyValueFactory<Order,
				Supplier>("supplier"));
		deliveryStatusColumn.setCellValueFactory(new PropertyValueFactory<Order,
				OrderDeliveryStatus>("orderDeliveryStatus"));
		
		table.getColumns().addAll(dateTimeOrderedColumn, supplierColumn, deliveryStatusColumn);
	}
	
	private static void initializeDateTimeSelection(GridPane grid) {
		Label dateTimeLbl = new Label("Date Time Delivered:");
		dateTimeLbl.setStyle("-fx-font-weight: bold");
		grid.add(dateTimeLbl, 0, 0);
		
		dateTimeField = new LocalDateTimeTextField();
		dateTimeField.setPrefWidth(200);
		grid.add(dateTimeField, 0, 1);
	}
	
	
	private static void initializeQtyTextFields(GridPane grid) {
		Label qtyTextFieldsLbl = new Label("Quantity of Products Delivered:");
		qtyTextFieldsLbl.setStyle("-fx-font-weight: bold");
		grid.add(qtyTextFieldsLbl, 1, 2);
		
		qtyTextFields = new ListView<>();
		qtyTextFields.setMaxHeight(500);
		
		orderTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Order>() {

			@Override
			public void changed(ObservableValue<? extends Order> observable,
					Order oldValue, Order newValue) {
				qtyTextFields.getItems().clear();
				
				Set<ProductOffer> productOffersOrdered = newValue.getProductsOffersOrdered();
		
				for (ProductOffer po : productOffersOrdered) {
					Product product = po.getProduct();
					int qtyDelivered = newValue.quantityDelivered(po);
					int qtyOrdered = newValue.quantityOrdered(po);
					
					HBox hBox = new HBox();
					hBox.setSpacing(10);
					Label productLabel 
						= new Label(product + " x ");
					TextField textField = new TextField();
					Label currStatsLbl = new Label("(" +  qtyDelivered + " out of " + qtyOrdered + " delivered)" );
					textField.setPrefWidth(100);
					
					hBox.getChildren().addAll(productLabel, textField, currStatsLbl);
					qtyTextFields.getItems().add(hBox);
				}
			}
			
		});
		grid.add(qtyTextFields, 1, 3);
		
	}

	private static LocalDateTime getDateTimeDelivered() {
		return dateTimeField.getLocalDateTime();
	}
	
	private static Order getSelectedOrder() {
		return orderTable.getSelectionModel().getSelectedItem();
	}

	private static Map<ProductOffer, Integer> getDeliveredProductOffers() {
		Map<ProductOffer, Integer> map = new HashMap<>();
		
		for (int i = 0; i < qtyTextFields.getItems().size(); i++) {
			HBox hBox = qtyTextFields.getItems().get(i);
			Label lbl = (Label) hBox.getChildren().get(0);
			Product selectedProduct = parseProduct(lbl.getText() + "");
			ProductOffer x = getSelectedOrder().getProductOffer(selectedProduct);
			TextField textField = (TextField) hBox.getChildren().get(1);
			map.put(x, Integer.parseInt(textField.getText()));
		}
		
		return map;
	}
	
	private static Product parseProduct(String label) {
		String name = label.substring(0, label.length() - 3);
	
		ProductDAO productDAO = new MySQLProductDAO();
		return productDAO.get(name);
	}
	
	private static boolean isValidDateTime() {
		LocalDateTime dateTime = getDateTimeDelivered();
		if (dateTime == null) {
			String errorMsg = "Delivery datetime cannot be empty."
					+ " Please select an delivery datetime.";
			SoftwareNotification.notifyError(errorMsg);
			return false;
		}
		return true;
	}
	
	private static boolean isValidProductQtyMap() {
		if (qtyTextFields.getItems().size() == 0) {
			String errorMsg = "Product delivered cannot be empty. Please select a product.";
			SoftwareNotification.notifyError(errorMsg);
			return false;
		}
		
		for (HBox hBox : qtyTextFields.getItems()) {
			Label lbl = (Label) hBox.getChildren().get(0);
			Product product = parseProduct(lbl.getText() + "");
			TextField qtyField = (TextField) hBox.getChildren().get(1);
			if (qtyField.getText() == null || qtyField.getText().equals("")) {
				String errorMsg = "The quantity for the product '" 
						+ product.getName() + "\' cannot be empty."
						+ " Please enter a quantity.";
				SoftwareNotification.notifyError(errorMsg);
				return false;
			} else if (!qtyField.getText().matches("[0-9]+")) {
				String errorMsg = "The quantity for the product '" 
						+ product.getName() + "\' can only be composed of digits."
						+ " Please enter a positive integer instead.";
				SoftwareNotification.notifyError(errorMsg);
				return false;
			}
		}
		return true;
	}


}
