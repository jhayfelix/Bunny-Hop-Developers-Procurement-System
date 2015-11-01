package procurementsys.controller;

import java.io.IOException;
import java.util.List;

import org.controlsfx.control.MasterDetailPane;

import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.database.MySQLSupplierDAO;
import procurementsys.model.database.SupplierDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewProductOffersController {
	private static ProductOfferTableController tableController;
	private static SupplierListController supplierController;
	
	public static void run() {
		Stage stage = new Stage();
		stage.setTitle("Product Offers");
		stage.setMinWidth(780);
		
		TableView<ProductOffer> productOfferTable = createTable();
		Node supplierListView = createSupplierListView();
		
		MasterDetailPane masterDetailPane = new MasterDetailPane();
		masterDetailPane.setMasterNode(supplierListView);
		masterDetailPane.setDetailNode(productOfferTable);
		
		masterDetailPane.setDividerPosition(.25);
		
		stage.setScene(new Scene(masterDetailPane));
		stage.show();
		
		showSuppliers();
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
	
}
