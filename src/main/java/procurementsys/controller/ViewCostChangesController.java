package procurementsys.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import procurementsys.model.CostChange;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.database.MySQLSupplierDAO;
import procurementsys.model.database.SupplierDAO;

public class ViewCostChangesController {
	private static ProductOfferTableController tableController;
	private static SupplierListController supplierController;
	private static TableView<CostChange> costChangeTable;
	
	public static void run() {
		Stage stage = new Stage();
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 20, 10, 10));
		
		Label costChangeLbl = new Label("Cost Changes:");
		costChangeLbl.setStyle("-fx-font-weight: bold");
		costChangeTable = createCostChangeTable();
		grid.add(costChangeLbl, 2, 0);
		grid.add(costChangeTable, 2, 1);
		
		Label productOfferLbl = new Label("Product Offer:");
		productOfferLbl.setStyle("-fx-font-weight: bold");
		TableView<ProductOffer> productOfferTable = createTable();
		grid.add(productOfferLbl, 1, 0);
		grid.add(productOfferTable, 1, 1);
		
		Label supplierLbl = new Label("Supplier");
		supplierLbl.setStyle("-fx-font-weight: bold");
		Node supplierListView = createSupplierListView();
		grid.add(supplierLbl, 0, 0);
		grid.add(supplierListView, 0, 1);
		
		showSuppliers();	
		
		stage.setTitle("View Cost Changes");
		stage.setScene(new Scene(grid));
		stage.show();
	}
	

	private static TableView<CostChange> createCostChangeTable() {
		TableView<CostChange> table = new TableView<>();
		
		TableColumn<CostChange, LocalDateTime> dateTimeCol = 
				new TableColumn<>("Change DateTime");
		dateTimeCol.setCellValueFactory(new PropertyValueFactory<CostChange,
				LocalDateTime>("changeDateTime"));
		
		TableColumn<CostChange, Double> costCol = new TableColumn<>("Cost");
		costCol.setCellValueFactory(new PropertyValueFactory<CostChange, Double>("cost"));
		
		table.getColumns().add(dateTimeCol);
		table.getColumns().add(costCol);
		dateTimeCol.prefWidthProperty().bind(table.widthProperty().divide(2)); // w * 1/2
		costCol.prefWidthProperty().bind(table.widthProperty().divide(2)); // w * 1/2
		
		return table;
	}


	private static void showCostChanges(ProductOffer po) {
		costChangeTable.getItems().clear();
		if (po != null) {
			List<CostChange> costChanges = po.getCostChanges();
			costChangeTable.getItems().addAll(costChanges);
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
			table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProductOffer>() {

				@Override
				public void changed(
						ObservableValue<? extends ProductOffer> observable,
						ProductOffer oldValue, ProductOffer newValue) {
					showCostChanges(newValue);
					
				}
				
			});
			return table;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
