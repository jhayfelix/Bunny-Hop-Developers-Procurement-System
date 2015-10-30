package procurementsys.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import procurementsys.model.Product;
import procurementsys.model.database.MySQLProductDAO;
import procurementsys.model.database.ProductDAO;

public class ProductListController {
	@FXML private ListView<Product> productListView;
	
	@FXML private void initialize() {
		ProductDAO productDAO = new MySQLProductDAO();
		productListView.getItems().addAll(productDAO.getAll());
		productListView.getSelectionModel().select(0);
		productListView.getSelectionModel()
		.setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	public void resize(double width, double height) {
		Parent parent = productListView.getParent();
		parent.prefWidth(width);
		parent.prefHeight(height);
		
		
		productListView.setPrefWidth(width);
		productListView.setPrefHeight(height);
	}
	
	public double getWidth() {
		return productListView.getWidth();
	}
	
	public double getHeight() {
		return  productListView.getHeight();
	}
	
	public ListView<Product> getListView() {
		return productListView;
	}
	
}
