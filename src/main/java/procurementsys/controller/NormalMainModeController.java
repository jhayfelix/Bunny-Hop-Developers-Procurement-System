package procurementsys.controller;

import java.io.IOException;
import java.util.List;

import org.controlsfx.control.Notifications;

import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.Tag;
import procurementsys.model.database.MySQLProductDAO;
import procurementsys.model.database.MySQLProductOfferDAO;
import procurementsys.model.database.ProductDAO;
import procurementsys.model.database.ProductOfferDAO;
import procurementsys.view.NumericTextField;
import procurementsys.view.SoftwareNotification;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class NormalMainModeController {
	@FXML private ListView<Product> taggedProductsListView;
	@FXML private TextField productFilterTextField;
	@FXML private NumericTextField quantityTextField;
	@FXML private TextField supplierFilterTextField;
	@FXML private TableView<ProductOffer> productOffersTable;
	@FXML private TableColumn<ProductOffer, Supplier> supplierCol;
	@FXML private TableColumn<ProductOffer, String> contactNumberCol;
	@FXML private TableColumn<ProductOffer, Number> costCol;
	@FXML private TableColumn<ProductOffer, Number> upcomingCostCol;
	@FXML private TableColumn<ProductOffer, String> upcomingCostChangeDateCol;
	
	protected void initialize(ListView<Tag> selectedTagsListView) {
		// Filter the products shown in the list whenever the filter changes
				productFilterTextField.textProperty().addListener(new ChangeListener<String>(){
					@Override
					public void changed(ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						showTaggedProducts(selectedTagsListView.getItems());
					}
				});
				
				// Show product offers of different suppliers for the selected product in the table
				taggedProductsListView.getSelectionModel().selectedItemProperty()
					.addListener(new ChangeListener<Product>() {
						@Override
						public void changed(
								ObservableValue<? extends Product> observable,
								Product oldValue, Product newValue) {
							showProductOffers(newValue);		
						}
					});
				
				// Refresh the table everytime quantity has changed
				quantityTextField.textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						try {
							Integer.parseInt(quantityTextField.getText());
							productOffersTable.refresh();
						} catch (NumberFormatException e) {
							if (!quantityTextField.getText().equals("")) {
								quantityTextField.clear();
								SoftwareNotification.notifyError("Quantity entered is too large."
										+ " Please enter a lower quantity");
							}
						}
						
					}
				});
				
				// Filter the product offers shown whenever the supplier filter changes
				supplierFilterTextField.textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						showProductOffers(taggedProductsListView.getSelectionModel().getSelectedItem());
					}
				});
				
				// Set up the table columns
				supplierCol.setCellValueFactory(
						new PropertyValueFactory<ProductOffer, Supplier>("supplier"));
				contactNumberCol.setCellValueFactory(
						new PropertyValueFactory<ProductOffer, String>("contactNumber"));
				costCol.setCellValueFactory(new Callback<CellDataFeatures<ProductOffer, Number>,ObservableValue<Number>>() {
					@Override
					public ObservableValue<Number> call(CellDataFeatures<ProductOffer, Number> param) {
						ObservableValue<Number> ret = 
								new SimpleDoubleProperty(param.getValue().getCurrentCost(getQuantityInput()));
						return ret;
					}
				});
				upcomingCostCol.setCellValueFactory(new Callback<CellDataFeatures<ProductOffer, Number>,ObservableValue<Number>>() {
					@Override
					public ObservableValue<Number> call(CellDataFeatures<ProductOffer, Number> param) {
						ObservableValue<Number> ret = 
								new SimpleDoubleProperty(param.getValue().getUpcomingCost(getQuantityInput()));
						return ret;
					}
				});
				
				upcomingCostCol.setCellFactory(column -> {
				    return new TableCell<ProductOffer, Number>() {
				        @Override
				        protected void updateItem(Number item, boolean empty) {
				            super.updateItem(item, empty);
				            setTextFill(Color.BLACK);
				            
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
				
	}
	
	protected void handleSearchTag(ComboBox<Tag> tagSearchComboBox,
			ListView<Tag> selectedTagsListView, Tooltip toolTip) throws IOException {
		
		Tag selectedTag = tagSearchComboBox.getSelectionModel().getSelectedItem();
		
		if (selectedTag != null && !selectedTagsListView.getItems().contains(selectedTag)) {
			
			selectedTagsListView.getItems().add(selectedTag);
			showTaggedProducts(selectedTagsListView.getItems());
			tagSearchComboBox.getSelectionModel().clearSelection();
			selectedTagsListView.setTooltip(toolTip);
			
		} else if (selectedTag != null && selectedTagsListView.getItems().contains(selectedTag)) {
			String errorMsg = "The tag \'" + selectedTag + "\' is already included in the current search.";
			Notifications.create().title("Error").text(errorMsg)
			.position(Pos.TOP_RIGHT).showError();
			tagSearchComboBox.getSelectionModel().clearSelection();
		}
	}
	
	protected int getQuantityInput() {
		String qtyStr = quantityTextField.getText();
		return (qtyStr.equals("")) ? 1 : Integer.parseInt(qtyStr);
	}
	
	protected void showTaggedProducts(List<Tag> tags) {
		ProductDAO productDAO = new MySQLProductDAO();
		List<Product> taggedProducts = productDAO.getAll(tags, productFilterTextField.getText());
		
		taggedProductsListView.getItems().clear();
		taggedProductsListView.getItems().addAll(taggedProducts);
		
		if (taggedProductsListView.getItems().size() > 0) {
			taggedProductsListView.getSelectionModel().select(0);
			showProductOffers(taggedProductsListView.getSelectionModel().getSelectedItem());
		}
	}

	
	protected void showProductOffers(Product product) {
		ProductOfferDAO productOffersDAO = new MySQLProductOfferDAO();
		productOffersTable.getItems().clear();
		productOffersTable.getItems().setAll(productOffersDAO.getAll(product, supplierFilterTextField.getText()));
	}
	
}
