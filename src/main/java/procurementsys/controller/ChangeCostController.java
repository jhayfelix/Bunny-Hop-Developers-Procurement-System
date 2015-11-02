package procurementsys.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import jfxtras.scene.control.LocalDateTimeTextField;
import procurementsys.model.CostChange;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.database.MySQLProductOfferDAO;
import procurementsys.model.database.MySQLSupplierDAO;
import procurementsys.model.database.ProductOfferDAO;
import procurementsys.model.database.SupplierDAO;
import procurementsys.view.SoftwareNotification;

public class ChangeCostController {
	
	private static ProductOfferTableController tableController;
	private static SupplierListController supplierController;
	private static LocalDateTimeTextField dateTimeField;
	private static TextField costField;
	
	public static void run() {
		Dialog<Map<String, Object>> dialog  = new Dialog<>();
		dialog.setTitle("Add Cost Change");
		dialog.setHeaderText("Enter cost change details");
		dialog.getDialogPane().getButtonTypes()
			.addAll(ButtonType.OK, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 20, 10, 10));
		
		Label dateTimeLbl = new Label("Cost Change DateTime:");
		dateTimeLbl.setStyle("-fx-font-weight: bold");
		dateTimeField = new LocalDateTimeTextField();
		grid.add(dateTimeLbl, 1, 0);
		grid.add(dateTimeField, 1, 1);
		
		Label costLbl = new Label("Cost:");
		costLbl.setStyle("-fx-font-weight: bold");
		costField = new TextField();
		grid.add(costLbl, 0, 0);
		grid.add(costField, 0, 1);
		
		Label productOfferLbl = new Label("Product Offer:");
		productOfferLbl.setStyle("-fx-font-weight: bold");
		TableView<ProductOffer> productOfferTable = createTable();
		grid.add(productOfferLbl, 1, 2);
		grid.add(productOfferTable, 1, 3);
		
		Label supplierLbl = new Label("Supplier");
		supplierLbl.setStyle("-fx-font-weight: bold");
		Node supplierListView = createSupplierListView();
		grid.add(supplierLbl, 0, 2);
		grid.add(supplierListView, 0, 3);
		
		showSuppliers();
		dialog.getDialogPane().setContent(grid);
		
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == ButtonType.OK) {
		    	Map<String, Object> map = new HashMap<>();
		    	map.put("PRODUCT_OFFER", getSelectedProductOffer());
		    	map.put("CHANGE_DATETIME", dateTimeField.getLocalDateTime());
		    	map.put("COST", Double.parseDouble(costField.getText()));
		    	return map;
		    }
		    return null;
		});
		
		final Button btOk = (Button) dialog.getDialogPane()
				.lookupButton(ButtonType.OK);
		btOk.addEventFilter(ActionEvent.ACTION, event -> {
			if (!isValidProductOffer() || !isValidCost() || !isValidDateTime()) {
				event.consume();
			}
		});
		
		Optional<Map<String, Object>> result = dialog.showAndWait();
		if (result.isPresent()) {
			Map<String, Object> map = result.get();
			ProductOffer po = (ProductOffer) map.get("PRODUCT_OFFER");
			LocalDateTime dateTime = (LocalDateTime) map.get("CHANGE_DATETIME");
			double cost = (Double) map.get("COST");
			
			CostChange costChange = new CostChange(dateTime, cost);
			ProductOfferDAO productOffer = new MySQLProductOfferDAO();
			productOffer.addCostChange(po, costChange);
			SoftwareNotification.notifySuccess("The cost change has been successfully "
					+ "added to the product offer.");
		}
		
	}
	

	private static Node createSupplierListView() {
		try {
			FXMLLoader loader = new FXMLLoader(ViewProductOffersController
					.class.getResource("/procurementsys/view/supplier_list.fxml"));
			Parent root = loader.load();
			
			supplierController = loader.getController();
			ListView<Supplier> listView = supplierController.getListView();
			listView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<Supplier>() {
				@Override
				public void changed(
						ObservableValue<? extends Supplier> observable,
						Supplier oldValue, Supplier newValue) {
					tableController.setSupplier(newValue);
					
				}
			});
			
			TextField filterField = supplierController.getFilterTextField();
			filterField.textProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(
						ObservableValue<? extends String> observable,
						String oldValue, String newValue) {
					showSuppliers();
				}
			});
			
			return root;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static void showSuppliers() {
		TextField filterField = supplierController.getFilterTextField();
		
		SupplierDAO supplierDAO = new MySQLSupplierDAO();
		List<Supplier> suppliers = supplierDAO.getAll(filterField.getText());
		
		ListView<Supplier> listView = supplierController.getListView();
		listView.getItems().clear();
		listView.getItems().addAll(suppliers);
		
		if (listView.getItems().size() > 0) {
			listView.getSelectionModel().select(0);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static TableView<ProductOffer> createTable() {
		try {
			
			FXMLLoader loader = new FXMLLoader(ViewProductOffersController
					.class.getResource("/procurementsys/view/product_offer_table.fxml"));
			Parent root = loader.load();
			tableController = loader.getController();
			TableView<ProductOffer> table = (TableView<ProductOffer>) root;
			return table;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private static boolean isValidProductOffer() {
		ProductOffer selectedProductOffer = getSelectedProductOffer();
		if (selectedProductOffer == null) {
			String errorMsg = "Product offer cannot be empty. "
					+ "Please select a product offer.";
			SoftwareNotification.notifyError(errorMsg);
			return false;
		}
		
		return true;
	}
	
	private static ProductOffer getSelectedProductOffer() {
		return tableController.getSelectedProductOffer();
	}
	
	private static boolean isValidCost() {
		String costInput = costField.getText();
		
		try {
			if (costInput.length() == 0) {
				costInput = null;
			}
			double value = Double.parseDouble(costInput);
			if (value < 0) {
				throw new NumberFormatException();
			}
		} catch (NullPointerException e) {
			SoftwareNotification.notifyError("Cost cannot be empty."
					+ " Please enter a cost.");
			return false;
		} catch (NumberFormatException e) {
			SoftwareNotification.notifyError("Cost must be "
					+ "a non-negative number. Please enter another cost.");
			return false;
		}
		
		return true;
	}
	
	private static boolean isValidDateTime() {
		LocalDateTime dateTime = dateTimeField.getLocalDateTime();
		if (dateTime == null) {
			String errorMsg = "Order datetime cannot be empty."
					+ " Please select an order datetime.";
			SoftwareNotification.notifyError(errorMsg);
			return false;
		}
		return true;
	}
	
}
