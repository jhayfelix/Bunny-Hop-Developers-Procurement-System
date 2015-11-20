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
//implemented by Alen Gapuz, Edited by Dominic Pagtalunan and Antonio Angeles
//copied and edited from AssignProductController 
//to become functional(Code of Tristan Milan)
public class EditContactNumberController{
	private static SupplierListController supplierController;
	private static TextField contactField;
	public static void run() {
		try {
			Dialog<Map<String, Object>> dialog = new Dialog<>();
			dialog.setTitle("Edit Supplier Contact Number");
			dialog.setHeaderText("Select the supplier contact number to edit");
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

			initializeSupplierSelection(grid, supplierRoot);
			
			Label editLbl = new Label("Enter new contact number:");
			editLbl.setStyle("-fx-font-weight: bold");
			grid.add(editLbl, 0, 0);
			contactField = new TextField();
			grid.add(contactField, 0, 1);
			initializeDialog(dialog);
			
			Optional<Map<String, Object>> result = dialog.showAndWait();
			
			if (result.isPresent()) {
				
				Map<String, Object> map = result.get();
				Supplier supplier = (Supplier) map.get("SUPPLIER");
				String contactNumber =(String) map.get("CONTACT_NUMBER");
				Supplier supp = new Supplier(supplier.getName(),contactNumber);
				SupplierDAO suppDAO = new MySQLSupplierDAO();
				suppDAO.updateContactNumber(supp);
			}
		} catch (IOException e) {
			System.out.println("exception in Edit Contact Number class. Check it out Dom. ");
			e.printStackTrace();
		}

	}


private static void initializeSupplierSelection(GridPane grid,   //by Tristan
			Parent supplierRoot) {
		Label supplierLbl = new Label("Supplier:");
		supplierLbl.setStyle("-fx-font-weight: bold");
		supplierController.resize(150, 300);
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
private static void showSuppliers() {							//by Tristan
		
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
	
	
	
	private static void initializeDialog(Dialog<Map<String, Object>> dialog) {
		Button btOk = (Button) dialog.getDialogPane()
				.lookupButton(ButtonType.OK);
		btOk.addEventFilter(ActionEvent.ACTION, event -> {
			if (!isValidSupplier() ) {
				event.consume();
			}
		});
		
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == ButtonType.OK) {
				Map<String, Object> map = new HashMap<>();
				map.put("SUPPLIER", getSelectedSupplier());
				map.put("CONTACT_NUMBER",getEditContactNumberInput());
				return map;
			}
			return null;
		});
	}
	
		private static Supplier getSelectedSupplier() {
		ListView<Supplier> supplierListView = supplierController.getListView();
		return supplierListView.getSelectionModel().getSelectedItem();
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
	
	private static String getEditContactNumberInput() {
		return contactField.getText();
	}
}