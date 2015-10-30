package procurementsys.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import procurementsys.model.Supplier;

public class SupplierListController {
	@FXML private TextField filterTextField;
	@FXML private ListView<Supplier> supplierListView;

	@FXML private void initialize() {		
		
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
	
	public TextField getFilterTextField() {
		return filterTextField;
	}
	
	public ListView<Supplier> getListView() {
		return supplierListView;
	}
	
}
