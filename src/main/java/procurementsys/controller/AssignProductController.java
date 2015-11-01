package procurementsys.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import procurementsys.model.CostChange;
import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.database.MySQLProductDAO;
import procurementsys.model.database.MySQLProductOfferDAO;
import procurementsys.model.database.MySQLSupplierDAO;
import procurementsys.model.database.ProductDAO;
import procurementsys.model.database.ProductOfferDAO;
import procurementsys.model.database.SupplierDAO;
import procurementsys.view.SoftwareNotification;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import jfxtras.scene.control.LocalDateTimeTextField;

public class AssignProductController {
	private static SupplierListController supplierController;
	private static ProductListController productController;
	private static TextField costField;
	private static LocalDateTimeTextField dateTimeField;
	
	public static void run() {
		try {
			Dialog<Map<String, Object>> dialog = new Dialog<>();
			dialog.setTitle("Assign Product");
			dialog.setHeaderText("Enter product offer details");
			dialog.getDialogPane().getButtonTypes()
				.addAll(ButtonType.OK, ButtonType.CANCEL);
			
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(20,20,10,10));
			dialog.getDialogPane().setContent(grid);
			
			FXMLLoader supplierLoader = 
					new FXMLLoader(AssignProductController.class
					.getResource("/procurementsys/view/supplier_list.fxml"));
			Parent supplierRoot = supplierLoader.load();
			supplierController = supplierLoader.getController();
			
			FXMLLoader productLoader = new FXMLLoader(AssignProductController.class
					.getResource("/procurementsys/view/product_list.fxml"));
			Parent productRoot = productLoader.load();
			productController = productLoader.getController();
			
			initializeSupplierSelection(grid, supplierRoot);
			initializeProductSelection(grid, productRoot);
			
			Label costLbl = new Label("Initial Cost:");
			costLbl.setStyle("-fx-font-weight: bold");
			grid.add(costLbl, 0, 0);
			costField = new TextField();
			grid.add(costField, 0, 1);
			
			Label dateTimeLbl = new Label("Initial Cost Assignment Datetime:");
			dateTimeLbl.setStyle("-fx-font-weight: bold");
			grid.add(dateTimeLbl, 1, 0);
			dateTimeField = new LocalDateTimeTextField();
			dateTimeField.setLocalDateTime(LocalDateTime.now());
			grid.add(dateTimeField, 1, 1);
			
			initializeDialog(dialog);
			
			Optional<Map<String, Object>> result = dialog.showAndWait();
			
			if (result.isPresent()) {
				
				Map<String, Object> map = result.get();
				Supplier supplier = (Supplier) map.get("SUPPLIER");
				Product product = (Product) map.get("PRODUCT");
				LocalDateTime costDateTime =
						(LocalDateTime) map.get("COSTCHANGE_DATETIME");
				double cost =  (Double) map.get("INITIAL_COST");
				
				List<CostChange> costChanges = new ArrayList<>();
				costChanges.add(new CostChange(costDateTime, cost));
				ProductOffer productOffer = new ProductOffer(product, supplier,
						costChanges);
				ProductOfferDAO productOfferDAO = new MySQLProductOfferDAO();
				productOfferDAO.add(productOffer);
				
				SoftwareNotification.notifySuccess("The product offer has been"
						+ " successfully added to the system.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void initializeSupplierSelection(GridPane grid,
			Parent supplierRoot) {
		Label supplierLbl = new Label("Supplier:");
		supplierLbl.setStyle("-fx-font-weight: bold");
		supplierController.resize(150, 150);
		grid.add(supplierLbl, 0, 2);
		grid.add(supplierRoot, 0, 3);
		
		
		TextField supplierFilter = supplierController.getFilterTextField();
		supplierFilter.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				showSuppliers();
			}
			
		});
		showSuppliers();
	}
	
	private static void showSuppliers() {
		
		TextField supplierFilter = supplierController.getFilterTextField();
		ListView<Supplier> supplierListView = supplierController.getListView();
		
		SupplierDAO supplierDAO = new MySQLSupplierDAO();
		List<Supplier> suppliers = supplierDAO.getAll(supplierFilter.getText());
		supplierListView.getItems().clear();
		supplierListView.getItems().setAll(suppliers);
		
		if (supplierListView.getItems().size() > 0) {
			supplierListView.getSelectionModel().select(0);
		}
		
	}
	
	private static void initializeProductSelection(GridPane grid,
			Parent productRoot) {
		
		Label productLbl = new Label("Product:");
		productLbl.setStyle("-fx-font-weight: bold");
		productController.resize(150, 150);
		grid.add(productLbl, 1, 2);
		grid.add(productRoot, 1, 3);
		
		TextField productFilter = productController.getFilterTextField();
		productFilter.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				showProducts();	
			}	
		});
		
		showProducts();
	}
	
	private static void showProducts() {
		
		TextField productFilter = productController.getFilterTextField();
		ListView<Product> productListView = productController.getListView();
		
		ProductDAO productDAO = new MySQLProductDAO();
		List<Product> products = productDAO.getAll(productFilter.getText());
		productListView.getItems().clear();
		productListView.getItems().setAll(products);
		
		if (productListView.getItems().size() > 0) {
			productListView.getSelectionModel().select(0);
		}
		
	}
	
	private static void initializeDialog(Dialog<Map<String, Object>> dialog) {
		Button btOk = (Button) dialog.getDialogPane()
				.lookupButton(ButtonType.OK);
		btOk.addEventFilter(ActionEvent.ACTION, event -> {
			if (!isValidSupplier() || !isValidProduct() 
					|| !isValidDateTime() || !isValidCost()) {
				event.consume();
			}
		});
		
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == ButtonType.OK) {
				Map<String, Object> map = new HashMap<>();
				map.put("COSTCHANGE_DATETIME", getCostChangeDateTime());
				map.put("INITIAL_COST",
						Double.parseDouble(getInitialCostInput()));
				map.put("SUPPLIER", getSelectedSupplier());
				map.put("PRODUCT", getSelectedProduct());
				
				return map;
			}
			return null;
		});
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
	
	private static Supplier getSelectedSupplier() {
		ListView<Supplier> supplierListView = supplierController.getListView();
		return supplierListView.getSelectionModel().getSelectedItem();
	}
	
	private static boolean isValidProduct() {
		if (getSelectedProduct() == null) {
			String errorMsg = "Supplier cannot be empty."
					+ " Please select a supplier";
			SoftwareNotification.notifyError(errorMsg);
			return false;
		}
		return true;
	}
	
	private static Product getSelectedProduct() {
		ListView<Product> productListView = productController.getListView();
		return productListView.getSelectionModel().getSelectedItem();
	}
	
	private static boolean isValidDateTime() {
		LocalDateTime dateTime = getCostChangeDateTime();
		if (dateTime == null) {
			String errorMsg = "Order datetime cannot be empty."
					+ " Please select an order datetime.";
			SoftwareNotification.notifyError(errorMsg);
			return false;
		}
		return true;
	}
	
	private static LocalDateTime getCostChangeDateTime() {
		return dateTimeField.getLocalDateTime();
	}
	
	private static boolean isValidCost() {
		String costInput = getInitialCostInput();
		
		try {
			if (costInput.length() == 0) {
				costInput = null;
			}
			double value = Double.parseDouble(costInput);
			if (value < 0) {
				throw new NumberFormatException();
			}
		} catch (NullPointerException e) {
			SoftwareNotification.notifyError("Initial cost cannot be empty."
					+ " Please enter an initial cost.");
			return false;
		} catch (NumberFormatException e) {
			SoftwareNotification.notifyError("Initial cost must be "
					+ "a non-negative number. Please enter another initial cost.");
			return false;
		}
		
		return true;
	}
	
	private static String getInitialCostInput() {
		return costField.getText();
	}
}
