package procurementsys.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import procurementsys.model.Tag;

public class TagListController {
	@FXML private TextField filterTextField;
	@FXML private ListView<Tag> tagListView;
	
	@FXML private void initialize() {
		
	}
	
	public void resize(double width, double height) {
		Parent parent = tagListView.getParent();
		parent.prefWidth(width);
		parent.prefHeight(height);
		
		tagListView.setPrefWidth(width);
		tagListView.setPrefHeight(height);
	}
	
	public double getWidth() {
		return tagListView.getWidth();
	}
	
	public double getHeight() {
		return  tagListView.getHeight();
	}
	
	public TextField getFilterTextField() {
		return filterTextField;
	}
	
	public ListView<Tag> getListView() {
		return tagListView;
	}
}
