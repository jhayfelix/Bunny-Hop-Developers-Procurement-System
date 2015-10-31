package procurementsys.controller;

import java.io.IOException;
import java.util.List;

import org.controlsfx.control.MasterDetailPane;
import org.controlsfx.control.PopOver;

import procurementsys.model.Supplier;
import procurementsys.model.database.MySQLSupplierDAO;
import procurementsys.model.database.SupplierDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ViewAllSuppliersController {
	private static SupplierListController supplierListController;
	private static PopOver popOver;
	private static Text nameInfo;
	private static Text contactNumInfo;
	private static Text activeInfo;
	
	public static void run() {
		try {
			Stage stage = new Stage();
			
			FXMLLoader supplierListLoader =
					new FXMLLoader(ViewAllSuppliersController.class
							.getResource("/procurementsys/view/supplier_list.fxml"));
			Parent supplierListRoot = supplierListLoader.load();
			supplierListController = supplierListLoader.getController();
			stage.setScene(new Scene(supplierListRoot));
			
			initializePopOver();
			initializeSupplierList();
			
			stage.show();
			showSuppliers();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void initializePopOver() {
		popOver = new PopOver();
		popOver.setHeaderAlwaysVisible(true);
		popOver.setTitle("Supplier Details");
		popOver.setAutoHide(false);
		
		HBox nameHBox = new HBox();
		Label nameLbl = new Label("Name:");
		nameLbl.setStyle("-fx-font-weight: bold");
		nameInfo = new Text();
		nameHBox.getChildren().addAll(nameLbl, nameInfo);
		
		HBox contactNumHBox = new HBox();
		Label contactNumLbl = new Label("Contact Number:");
		contactNumLbl.setStyle("-fx-font-weight: bold");
		contactNumInfo = new Text();
		contactNumHBox.getChildren().addAll(contactNumLbl, contactNumInfo);
		
		HBox activeBox = new HBox();
		Label activeLbl = new Label("Activity Status:");
		activeLbl.setStyle("-fx-font-weight: bold");
		activeInfo = new Text();
		activeBox.getChildren().addAll(activeLbl, activeInfo);
		
		VBox vBox = new VBox();
		vBox.getChildren().addAll(nameHBox, contactNumHBox, activeBox);
		
		popOver.setContentNode(vBox);
	}
	
	private static void initializeSupplierList() {
		TextField supplierFilter = supplierListController.getFilterTextField();
		supplierFilter.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				showSuppliers();
			}
			
		});
		
		
		ListView<Supplier> supplierListView = supplierListController.getListView();
		supplierListView.getSelectionModel().selectedItemProperty()
			.addListener(new ChangeListener<Supplier>(){
				@Override
				public void changed(ObservableValue<? extends Supplier> observable,
						Supplier oldValue, Supplier newValue) {
					if (newValue == null) {
						popOver.hide();
					} else {
						nameInfo.setText(newValue.getName());
						contactNumInfo.setText(newValue.getContactNumber());
						activeInfo.setText(newValue.isActive() + "");
						popOver.show(supplierListView);
					}
				}
		});
		
	}
	
	private static void showSuppliers() {
		TextField supplierFilter = supplierListController.getFilterTextField();
		ListView<Supplier> supplierListView = supplierListController.getListView();
		
		SupplierDAO supplierDAO = new MySQLSupplierDAO();
		List<Supplier> suppliers = supplierDAO.getAll(supplierFilter.getText());
		supplierListView.getItems().clear();
		supplierListView.getItems().setAll(suppliers);
		
		if (supplierListView.getItems().size() > 0) {
			supplierListView.getSelectionModel().select(0);
		}	
	}
	
}
