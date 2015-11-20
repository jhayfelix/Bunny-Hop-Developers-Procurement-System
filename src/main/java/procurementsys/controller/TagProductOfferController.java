package procurementsys.controller;

import java.io.IOException;
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

import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.Tag;
import procurementsys.model.database.MySQLSupplierDAO;
import procurementsys.model.database.MySQLTagDAO;
import procurementsys.model.database.SupplierDAO;
import procurementsys.model.database.TagDAO;
import procurementsys.view.SoftwareNotification;

public class TagProductOfferController {
	
	private static TagListController tagController;
	private static ProductOfferTableController tableController;
	private static SupplierListController supplierController;
	
	@SuppressWarnings("unchecked")
	public static void run() {
		Dialog<Map<String, Object>> dialog  = new Dialog<>();
		dialog.setTitle("Tag Product Offer");
		//dialog.setHeaderText("Tag a supplier");
		dialog.getDialogPane().getButtonTypes()
			.addAll(ButtonType.OK, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 20, 10, 10));
		
		
		Label productOfferLbl = new Label("Product Offer:");
		productOfferLbl.setStyle("-fx-font-weight: bold");
		TableView<ProductOffer> productOfferTable = createTable();
		grid.add(productOfferLbl, 1, 0);
		grid.add(productOfferTable, 1, 1);
		
		Label supplierLbl = new Label("Supplier:");
		supplierLbl.setStyle("-fx-font-weight: bold");
		Node supplierListView = createSupplierListView();
		grid.add(supplierLbl, 0, 0);
		grid.add(supplierListView, 0, 1);
		
		
		Label tagsLbl = new Label("Tags:");
		tagsLbl.setStyle("-fx-font-weight: bold");
		grid.add(tagsLbl, 2, 0);
		Node tagListView = createTagsListView();
		grid.add(tagListView, 2, 1);
		
		
		showSuppliers();
		showTags();
		dialog.getDialogPane().setContent(grid);
		
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == ButtonType.OK) {
		    	Map<String, Object> map = new HashMap<>();
		    	map.put("PRODUCT_OFFER", getSelectedProductOffer());
		    	map.put("SELECTED_TAGS", tagController.getListView()
		    								.getSelectionModel().getSelectedItems());
		    	return map;
		    }
		    return null;
		});
		
		final Button btOk = (Button) dialog.getDialogPane()
				.lookupButton(ButtonType.OK);
		btOk.addEventFilter(ActionEvent.ACTION, event -> {
			if (!isValidProductOffer() || !isValidTags()) {
				event.consume();
			}
		});
		
		Optional<Map<String, Object>> result = dialog.showAndWait();
		if (result.isPresent()) {
			Map<String, Object> map = result.get();
			ProductOffer po = (ProductOffer) map.get("PRODUCT_OFFER");
			List<Tag> tags = (List<Tag>) map.get("SELECTED_TAGS");
			
			TagDAO tagDAO = new MySQLTagDAO();
			tagDAO.tagProductOffer(po, tags);
			SoftwareNotification.notifySuccess("The product offer has"
					+ " been succesfully tagged.");
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
	
	private static Node createTagsListView() {
		try {
			FXMLLoader loader = new FXMLLoader(TagProductOfferController
					.class.getResource("/procurementsys/view/tag_list.fxml"));
			Parent root = loader.load();
			tagController = loader.getController();
		
			
			TextField filterField = tagController.getFilterTextField();
			filterField.textProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(
						ObservableValue<? extends String> observable,
						String oldValue, String newValue) {
					showTags();
				}
				
			});
			
			return root;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;	
	}
	
	private static void showTags() {
		TextField filterField = tagController.getFilterTextField();
		TagDAO tagDAO = new MySQLTagDAO();
		List<Tag> tags = tagDAO.getAll(filterField.getText());
		
		ListView<Tag> listView = tagController.getListView();
		listView.getItems().clear();
		listView.getItems().addAll(tags);
		
		if (listView.getItems().size() > 0) {
			listView.getSelectionModel().select(0);
		}
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
	
	private static boolean isValidTags() {
		List<Tag> selectedTags = tagController.getListView().getSelectionModel().getSelectedItems();
		ProductOffer selectedProductOffer = getSelectedProductOffer();
		if (selectedTags == null || selectedTags.size() == 0) {
			String errorMsg = "Tags cannot be empty. "
					+ "Please select at least one tag.";
			SoftwareNotification.notifyError(errorMsg);
			return false;
		} 
		
		List<Tag> productOfferTags = selectedProductOffer.getTags();
		for (Tag t : selectedTags) {
			if (productOfferTags.contains(t)) {
				String errorMsg = "The product offer selected already contains the tag '" + t + "'."
						+ " Please remove the tag from the selection.";
				SoftwareNotification.notifyError(errorMsg);
				return false;
			}
		}
		return true;
	}
	
}
