package procurementsys.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import procurementsys.model.Supplier;
import procurementsys.model.database.MySQLSupplierDAO;
import procurementsys.model.database.SupplierDAO;

public class SupplierListController {
	public final static String LISTVIEW_ID = "supplierListView";
	
	@FXML private ListView<Supplier> supplierListView;

	@FXML private void initialize() {
		SupplierDAO supplierDAO = new MySQLSupplierDAO();
		supplierListView.getItems().addAll(supplierDAO.getAll());
		if (supplierListView.getItems().size() > 0) {
			supplierListView.getSelectionModel().select(0);
		}
	}
	
	public void resize(double width, double height) {
		Parent parent = supplierListView.getParent();
		parent.prefWidth(width);
		parent.prefHeight(height);
		
		supplierListView.setPrefWidth(width);
		supplierListView.setPrefHeight(height);
	}
	
	public double getWidth() {
		return supplierListView.getWidth();
	}
	
	public double getHeight() {
		return  supplierListView.getHeight();
	}
	
	public ListView<Supplier> getListView() {
		return supplierListView;
	}
}
