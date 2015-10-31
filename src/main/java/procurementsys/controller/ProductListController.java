package procurementsys.controller;

import java.util.List;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import procurementsys.model.Product;

public class ProductListController {
	@FXML private TextField filterTextField;
	@FXML private ListView<Product> productListView;
	
	
	@FXML private void initialize() {
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
	
	public TextField getFilterTextField() {
		return filterTextField;
	}
	
	public ListView<Product> getListView() {
		return productListView;
	}
	
	public void showAll(List<Product> products) {
		productListView.getItems().addAll(products);
	}
	
	public void fireMouseClickEvent() {
		Event.fireEvent(productListView, new MouseEvent(MouseEvent.MOUSE_CLICKED,
				0, 0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true,
				true, true, true, true, true, null));
	}
	
}
