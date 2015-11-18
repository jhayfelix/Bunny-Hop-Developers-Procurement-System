package procurementsys.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import procurementsys.model.Order;
import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.database.MySQLOrderDAO;
import procurementsys.model.database.MySQLProductDAO;
import procurementsys.model.database.MySQLProductOfferDAO;
import procurementsys.model.database.MySQLSupplierDAO;
import procurementsys.model.database.OrderDAO;
import procurementsys.model.database.ProductDAO;
import procurementsys.model.database.ProductOfferDAO;
import procurementsys.model.database.SupplierDAO;
import procurementsys.view.SoftwareNotification;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import jfxtras.scene.control.LocalDateTimeTextField;

public class AddOrderController {
	private static LocalDateTimeTextField dateTimeField;
	private static TextField supplierFilterTextField;
	private static ListView<Supplier> supplierListView;
	private static TextField productFilter;
	private static ListView<Product> productListView;
	private static ListView<HBox> qtyTextFields;
	
	@SuppressWarnings("unchecked")
	public static void run() {
		try {
			Dialog<Map<String, Object>> dialog  = new Dialog<>();
			dialog.setTitle("Add Order");
			dialog.setHeaderText("Enter order details");
			dialog.getDialogPane().getButtonTypes()
				.addAll(ButtonType.OK, ButtonType.CANCEL);
			
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(20, 20, 10, 10));
			
			// Hack method - supplierListView and supplierFilter need to be initialized
			//				 right away
			FXMLLoader supplierLoader = new FXMLLoader(AddOrderController
					.class.getResource("/procurementsys/view/supplier_list.fxml"));
			Parent supplierParent = supplierLoader.load();
			SupplierListController supplierController =
					supplierLoader.getController();
			supplierListView = supplierController.getListView();
			supplierFilterTextField = supplierController.getFilterTextField();
			
			// Hack method - productListView and productFilter need to be initialized
			//				 right away
			FXMLLoader productLoader = new FXMLLoader(AddOrderController
					.class.getResource("/procurementsys/view/product_list.fxml"));
			Parent productParent = productLoader.load();
			ProductListController productController =
					productLoader.getController();
			productListView = productController.getListView();
			productFilter = productController.getFilterTextField();
			
			initializeDateTimeSelection(grid);
			initalizeSupplierSelection(grid, supplierParent, supplierController);
			initializeProductSelection(grid, productParent, productController);
			initializeQtyTextFields(grid);
			
			dialog.getDialogPane().setContent(grid);
			
			dialog.setResultConverter(dialogButton -> {
			    if (dialogButton == ButtonType.OK) {
			    	Map<String, Object> map = new HashMap<>();
			    	map.put("DATE_ORDERED", getDateTimeOrdered());
			    	map.put("SUPPLIER", getSelectedSupplier());
			    	map.put("ORDERED_PRODUCTOFFERS", getSelectedProductOffers());
			    	return map;
			    }
			    return null;
			});
						
			final Button btOk = (Button) dialog.getDialogPane()
					.lookupButton(ButtonType.OK);
	        btOk.addEventFilter(ActionEvent.ACTION, event -> {
	        	if (!isValidDateTime() || !isValidSupplier() 
	        			|| !isValidProductQtyMap() ) {
	        		event.consume();
	        	}
	        });
	        
			Optional<Map<String, Object>> result = dialog.showAndWait();
			if (result.isPresent()) {
				Map<String, Object> map = result.get();
				
				LocalDateTime dateTime = (LocalDateTime) map.get("DATE_ORDERED");
				Supplier supplier = (Supplier) map.get("SUPPLIER");
				Map<ProductOffer, Integer> orderedProductOffers = 
						(Map<ProductOffer, Integer>) map.get("ORDERED_PRODUCTOFFERS");
				Order order = new Order(dateTime, supplier, orderedProductOffers);
				
				OrderDAO orderDAO = new MySQLOrderDAO();
				orderDAO.add(order);
				SoftwareNotification.notifySuccess("The order has been succesfully "
						+ "added to the system");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void initializeDateTimeSelection(GridPane grid) {
		Label dateTimeLbl = new Label("Date Time Ordered:");
		dateTimeLbl.setStyle("-fx-font-weight: bold");
		grid.add(dateTimeLbl, 0, 0);
		
		dateTimeField = new LocalDateTimeTextField();
		dateTimeField.setPrefWidth(200);
		grid.add(dateTimeField, 0, 1);
	}
	
	private static void initalizeSupplierSelection(GridPane grid, Parent supplierRoot,
			SupplierListController supplierController) throws IOException {
		
		Label supplierLbl = new Label("Supplier:");
		supplierLbl.setStyle("-fx-font-weight: bold");
		grid.add(supplierLbl, 0, 2);
		
		supplierController.resize(supplierController.getWidth(), 300);
		grid.add(supplierRoot, 0, 3);
		
		// Get initial set of suppliers
		showSuppliers();
		showProducts(); // Products wont show since, listener has not yet been assigned
		supplierListView.getSelectionModel().selectedItemProperty()
			.addListener(new ChangeListener<Supplier>() {
			@Override
			public void changed(ObservableValue<? extends Supplier> observable,
					Supplier oldValue, Supplier newValue) {
				showProducts();
			}
		});

		supplierFilterTextField.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				showSuppliers();	
			}	
		});
	}
	
	
	private static void showSuppliers() {
		SupplierDAO supplierDAO = new MySQLSupplierDAO();
		List<Supplier> suppliers = supplierDAO
				.getAll(supplierFilterTextField.getText());
		supplierListView.getItems().clear();
		supplierListView.getItems().addAll(suppliers);
		if (supplierListView.getItems().size() > 0) {
			supplierListView.getSelectionModel().select(0);
		}
		fireMouseClickEvent(productListView);
	}
	
	private static void initializeProductSelection(GridPane grid, Parent productRoot, 
			ProductListController productController ) throws IOException {
		
		Label productLbl = new Label("Product:");
		productLbl.setStyle("-fx-font-weight: bold");
		grid.add(productLbl, 1, 2);
		
		productController.resize(productController.getWidth(), 300);
		grid.add(productRoot, 1, 3);
		
		
		productFilter.textProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				showProducts();	
			}
			
		});
	}
	
	private static void showProducts() {
		ProductDAO productDAO = new MySQLProductDAO();
		List<Product> products = productDAO.getAll(getSelectedSupplier(), productFilter.getText());
		productListView.getItems().clear();
		productListView.getItems().addAll(products);
		if (productListView.getItems().size() > 0) {
			productListView.getSelectionModel().select(0);
		}
		fireMouseClickEvent(productListView);
	}
	
	private static void initializeQtyTextFields(GridPane grid) {
		Label qtyTextFieldsLbl = new Label("Quantity of Products:");
		qtyTextFieldsLbl.setStyle("-fx-font-weight: bold");
		grid.add(qtyTextFieldsLbl, 2, 2);
		
		qtyTextFields = new ListView<>();
		qtyTextFields.setMaxHeight(300);
		
		productListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				qtyTextFields.getItems().clear();
				List<Product> selectedProducts = productListView
							.getSelectionModel().getSelectedItems();
				
				for (int i = 0; i < selectedProducts.size(); i++) {
					HBox hBox = new HBox();
					Label productLabel 
						= new Label(selectedProducts.get(i).getName()
								+ " x ");
					TextField textField = new TextField();
					textField.setPrefWidth(100);
					
					hBox.getChildren().add(productLabel);
					hBox.getChildren().add(textField);
					
					qtyTextFields.getItems().add(hBox);
				}
			}
			
		});

		fireMouseClickEvent(productListView);	
		grid.add(qtyTextFields, 2, 3);
		
	}

	private static void fireMouseClickEvent(EventTarget eventTarget) {
		Event.fireEvent(eventTarget, new MouseEvent(MouseEvent.MOUSE_CLICKED,
				0, 0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true,
				true, true, true, true, true, null));
		
	}

	private static LocalDateTime getDateTimeOrdered() {
		return dateTimeField.getLocalDateTime();
	}
	
	private static Supplier getSelectedSupplier() {
		return supplierListView.getSelectionModel()
				.getSelectedItem();
	}
	
	private static Map<ProductOffer, Integer> getSelectedProductOffers() {
		Map<ProductOffer, Integer> map = new HashMap<>();
		
		ProductOfferDAO productOfferDAO = 
				new MySQLProductOfferDAO();
		for (int i = 0; i < qtyTextFields.getItems().size(); i++) {
			HBox hBox = qtyTextFields.getItems().get(i);
			Label lbl = (Label) hBox.getChildren().get(0);
			Product selectedProduct = parseProduct(lbl.getText() + "");
			ProductOffer x = productOfferDAO.get(getSelectedSupplier(),
							selectedProduct);
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
		LocalDateTime dateTime = getDateTimeOrdered();
		if (dateTime == null) {
			String errorMsg = "Order datetime cannot be empty."
					+ " Please select an order datetime.";
			SoftwareNotification.notifyError(errorMsg);
			return false;
		}
		return true;
	}
	
	private static boolean isValidSupplier() {
		if (getSelectedSupplier() == null) {
			String errorMsg = "Supplier cannot be empty."
					+ " Please select a supplier";
			SoftwareNotification.notifyError(errorMsg);
			return false;
		}
		return true;
	}
	
	private static boolean isValidProductQtyMap() {
		if (qtyTextFields.getItems().size() == 0) {
			String errorMsg = "Product ordered cannot be empty. Please select a product.";
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
