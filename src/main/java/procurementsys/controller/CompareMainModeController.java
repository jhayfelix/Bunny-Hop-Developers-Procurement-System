package procurementsys.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.Tag;
import procurementsys.model.database.MySQLProductOfferDAO;
import procurementsys.model.database.ProductOfferDAO;
import procurementsys.model.database.SupplierDAO;
import procurementsys.model.database.MySQLSupplierDAO;
import procurementsys.view.NumericTextField;
import procurementsys.view.SoftwareNotification;

public class CompareMainModeController {
	@FXML VBox leftVBox;
	@FXML VBox rightVBox;
	@FXML AnchorPane leftBlank;
	@FXML AnchorPane rightBlank;
	
	
	
	protected void initialize(ListView<Tag> selectedTagsListView) {
		leftBlank.setOnMouseClicked(event -> {
			List<Tag> selectedTags = selectedTagsListView.getItems();
			Node node = createSupplierProductOfferNode(selectedTags, leftVBox, leftBlank);
			if (node != null) {
				leftVBox.getChildren().clear();
				leftVBox.getChildren().add(node);
			}
		});
		
		rightBlank.setOnMouseClicked(event -> {
			List<Tag> selectedTags = selectedTagsListView.getItems();
			Node node = createSupplierProductOfferNode(selectedTags, rightVBox, rightBlank);
			if (node != null) {
				rightVBox.getChildren().clear();
				rightVBox.getChildren().add(node);
			}
		});
	}
	
	private Node createSupplierProductOfferNode(List<Tag> selectedTags,
			VBox rootVBox, AnchorPane emptyPane) {
		Supplier supplier = supplierSelection(selectedTags);
		
		Label qtyLbl = new Label("Quantity:");
		qtyLbl.setStyle("-fx-font-weight: bold");
		NumericTextField qtyTextField = new NumericTextField();
		Button closeBtn = new Button("Close");
		closeBtn.setOnAction(event -> {
			rootVBox.getChildren().clear();
			rootVBox.getChildren().add(emptyPane);
		});
		
		HBox hBox = new HBox();
		hBox.setSpacing(10);
		hBox.setPadding(new Insets(5, 5, 5, 5));
		hBox.getChildren().addAll(qtyLbl, qtyTextField, closeBtn);
		
		TableView<ProductOffer> table = new TableView<>();
		initializeTableColumns(table, qtyTextField);
		ProductOfferDAO productOfferDAO = new MySQLProductOfferDAO();
		List<ProductOffer> productOffers = productOfferDAO.getAll(supplier);
		table.getItems().addAll(productOffers);
		table.setPrefHeight(420);
		
		VBox vBox = new VBox();
		vBox.setSpacing(10);
		vBox.getChildren().addAll(hBox, table);
		return vBox;
	}
	
	@SuppressWarnings("unchecked")
	private void initializeTableColumns(TableView<ProductOffer> table, TextField quantityTextField) {
		TableColumn<ProductOffer, Supplier> supplierCol = 
				new TableColumn<>("Supplier");
		TableColumn<ProductOffer, Product> productCol = 
				new TableColumn<>("Product");
		TableColumn<ProductOffer, String> contactNumberCol =
				new TableColumn<>("Contact Number");
		TableColumn<ProductOffer, Number> costCol = 
				new TableColumn<>("Cost");
		TableColumn<ProductOffer, Number> upcomingCostCol = 
				new TableColumn<>("Upcoming Cost");
		TableColumn<ProductOffer, String> upcomingCostChangeDateCol = new TableColumn<>("Change Date");
		
		supplierCol.setCellValueFactory(
				new PropertyValueFactory<ProductOffer, Supplier>("supplier"));
		productCol.setCellValueFactory(
				new PropertyValueFactory<ProductOffer, Product>("product"));
		contactNumberCol.setCellValueFactory(
				new PropertyValueFactory<ProductOffer, String>("contactNumber"));
		costCol.setCellValueFactory(new Callback<CellDataFeatures<ProductOffer, Number>,ObservableValue<Number>>() {
			@Override
			public ObservableValue<Number> call(CellDataFeatures<ProductOffer, Number> param) {
				ObservableValue<Number> ret = 
						new SimpleDoubleProperty(param.getValue().getCurrentCost(getQuantityInput(quantityTextField)));
				return ret;
			}
		});
		upcomingCostCol.setCellValueFactory(new Callback<CellDataFeatures<ProductOffer, Number>,ObservableValue<Number>>() {
			@Override
			public ObservableValue<Number> call(CellDataFeatures<ProductOffer, Number> param) {
				ObservableValue<Number> ret = 
						new SimpleDoubleProperty(param.getValue().getUpcomingCost(getQuantityInput(quantityTextField)));
				return ret;
			}
		});
		
		upcomingCostCol.setCellFactory(column -> {
		    return new TableCell<ProductOffer, Number>() {
		        @Override
		        protected void updateItem(Number item, boolean empty) {
		            super.updateItem(item, empty);
		         
		            ProductOffer po = (ProductOffer) this.getTableRow().getItem();
		            if (po != null) {
			            double currCost = po.getCurrentCost();
			            double upcomingCost = po.getUpcomingCost();
			            setTextFill((currCost > upcomingCost) ? Color.RED :Color.GREEN);
		            }
		            if (!empty) {
		            	setText(item + "");
		            } else {
		            	setText("");
		            }
		        }
		    };
		});
		upcomingCostChangeDateCol.setCellValueFactory(
				new PropertyValueFactory<ProductOffer, String>("upcomingCostChangeDateStr"));
		table.getColumns().addAll(supplierCol, productCol, contactNumberCol, costCol, upcomingCostCol,
				upcomingCostChangeDateCol);
		
		// Refresh the table everytime quantity has changed
		quantityTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				try {
					Integer.parseInt(quantityTextField.getText());
					table.refresh();
				} catch (NumberFormatException e) {
					if (!quantityTextField.getText().equals("")) {
						quantityTextField.clear();
						SoftwareNotification.notifyError("Quantity entered is too large."
								+ " Please enter a lower quantity");
					}
				}
				
			}
		});
	}
	
	protected int getQuantityInput(TextField quantityTextField) {
		String qtyStr = quantityTextField.getText();
		return (qtyStr.equals("")) ? 1 : Integer.parseInt(qtyStr);
	}
	
	private Supplier supplierSelection(List<Tag> selectedTags) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass()
					.getResource("/procurementsys/view/supplier_list.fxml"));
			Node supplierList = loader.load();
			SupplierListController controller = loader.getController();
			
			ListView<Supplier> listView = controller.getListView();
			TextField filter = controller.getFilterTextField();
			
			showSuppliers(selectedTags, listView, filter.getText());
			
			filter.textProperty().addListener(p -> {
				showSuppliers(selectedTags, listView, filter.getText());
			});
			
			
			Dialog<Supplier> dialog = new Dialog<>();
			dialog.setTitle("Supplier Selection");
			dialog.setHeaderText("Please select a supplier");
			dialog.getDialogPane().getButtonTypes()
				.addAll(ButtonType.OK, ButtonType.CANCEL);
			
			dialog.setResultConverter(dialogButton -> {
			    if (dialogButton == ButtonType.OK) {
			    	return listView.getSelectionModel()
			    			.getSelectedItem();
			    }
			    return null;
			});

	        final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
	        btOk.addEventFilter(ActionEvent.ACTION, event -> {
	        	if (listView.getSelectionModel().getSelectedItem() == null) {
	    			String errorMsg = "A supplier must be selected."
	    					+ " Please selected a supplier.";
	    			SoftwareNotification.notifyError(errorMsg);
	        		event.consume();
	        	}
	        });
			
			dialog.getDialogPane().setContent(supplierList);
			Optional<Supplier> result = dialog.showAndWait();
			if (result.isPresent()) {
				return result.get();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void showSuppliers(List<Tag> selectedTags, 
				ListView<Supplier> listView, String filter) {
		
		SupplierDAO supplier = new MySQLSupplierDAO();
		listView.getItems().clear();
		listView.getItems().addAll(supplier.getAll(selectedTags,
				filter));
		
		if (listView.getItems().size() > 0) {
			listView.getSelectionModel().select(0);
		}
		
	}
	
	
	
}
