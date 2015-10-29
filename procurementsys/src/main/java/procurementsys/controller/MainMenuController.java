package procurementsys.controller;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.BreadCrumbBar;

import procurementsys.model.CostChange;
import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.Tag;
import procurementsys.model.database.MySQLProductDAO;
import procurementsys.model.database.MySQLProductOfferDAO;
import procurementsys.model.database.MySQLTagDAO;
import procurementsys.model.database.ProductDAO;
import procurementsys.model.database.ProductOfferDAO;
import procurementsys.model.database.TagDAO;
import procurementsys.view.AutoCompleteComboBoxListener;
import procurementsys.view.style.ColorRectCell;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;


/**
 * @author Jan Tristan Milan
 */

public class MainMenuController extends Controller {
	@FXML private ComboBox<Tag> tagSearchComboBox;
	@FXML private ListView<Tag> selectedTagsListView;
	
	@FXML private ListView<Product> taggedProductsListView;
	@FXML private TextField productFilterTextField;
	@FXML private TextField quantityTextField;
	@FXML private TextField supplierFilterTextField;
	@FXML private TableView<ProductOffer> productOffersTable;
	@FXML private TableColumn<ProductOffer, Supplier> supplierCol;
	@FXML private TableColumn<ProductOffer, String> contactNumberCol;
	@FXML private TableColumn<ProductOffer, Number> costCol;
	@FXML private TableColumn<ProductOffer, Number> upcomingCostCol;
	@FXML private TableColumn<ProductOffer, String> upcomingCostChangeDateCol;
	
	public MainMenuController() {
		
	}
	
	@FXML private void initialize() {
		// Set up autocomplete searching of tags
		TagDAO tagDAO = new MySQLTagDAO();
		tagSearchComboBox.getItems().addAll(tagDAO.getAll());
		new AutoCompleteComboBoxListener<Tag>(tagSearchComboBox);
		showTaggedProducts(new ArrayList<>());
		
		/*
		selectedTagsListView.setCellFactory(new Callback<ListView<Tag>, ListCell<Tag>>() {
		    @Override public ListCell<Tag> call(ListView<Tag> list) {
		        return new ColorRectCell();
		    }
		});*/
		Label titleLabel = new Label("What types of products are you looking for?");
		titleLabel.setTextFill(Color.WHITE);
		titleLabel.setFont(new Font(20));
		selectedTagsListView.setPlaceholder(titleLabel);
		
		// Remove selected tag when double clicked
		selectedTagsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) {
					int index = selectedTagsListView.getSelectionModel()
							.getSelectedIndex();
					selectedTagsListView.getItems().remove(index);
					showTaggedProducts(selectedTagsListView.getItems());
				}
			}
		});
		
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
				productOffersTable.refresh();
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
	
	private int getQuantityInput() {
		String qtyStr = quantityTextField.getText();
		return (qtyStr.equals("")) ? 1 : Integer.parseInt(qtyStr);
	}
	
	private void showTaggedProducts(List<Tag> tags) {
		ProductDAO productDAO = new MySQLProductDAO();
		List<Product> taggedProducts = productDAO.getAll(tags, productFilterTextField.getText());
		
		taggedProductsListView.getItems().clear();
		taggedProductsListView.getItems().addAll(taggedProducts);
		
		if (taggedProductsListView.getItems().size() > 0) {
			taggedProductsListView.getSelectionModel().select(0);
			showProductOffers(taggedProductsListView.getSelectionModel().getSelectedItem());
		}
	}
	
	private void showProductOffers(Product product) {
		ProductOfferDAO productOffersDAO = new MySQLProductOfferDAO();
		productOffersTable.getItems().clear();
		productOffersTable.getItems().setAll(productOffersDAO.getAll(product, supplierFilterTextField.getText()));
	}
	
	@FXML protected void handleSearchTag(ActionEvent event) throws IOException {
		Tag selectedTag = tagSearchComboBox.getSelectionModel().getSelectedItem();
		if (selectedTag != null) {
			selectedTagsListView.getItems().add(selectedTag);
			showTaggedProducts(selectedTagsListView.getItems());
			tagSearchComboBox.getSelectionModel().clearSelection();
		}
	}
	
    @FXML protected void handleAddNewSupplier(ActionEvent event) throws IOException {
    	loadNewStage("Enter supplier details",
    			"/procurementsys/view/add_new_supplier_details_dialog.fxml", false);
    }
	
    @FXML protected void handleViewSuppliers(ActionEvent event)
    		throws IOException {
    	loadNewStage("Kimson Trading - Suppliers",
    			"/procurementsys/view/browse_suppliers_view.fxml", false);
    }
    
    
    @FXML protected void handleAddNewProduct(ActionEvent event) throws IOException {
    	loadNewStage("Enter product details",
    			"/procurementsys/view/add_new_product_details_dialog.fxml", false);
    }
    
    @FXML protected void handleAssignProduct(ActionEvent event) throws IOException {
    	// TODO - implement this
    }
    
    @FXML protected void handleViewProducts(ActionEvent event) 
    		throws IOException {
    	loadNewStage("Kimson Trading - Products",
    			"/procurementsys/view/browse_products_view.fxml", false);
    }
    
    @FXML protected void handleAddNewTag(ActionEvent event) throws IOException {
    	// TODO - implement this
    }
    
    @FXML protected void handleTagProductOffer(ActionEvent event) throws IOException {
    	// TODO - implement this
    }
    
    @FXML protected void handleViewTags(ActionEvent event) throws IOException {
    	// TODO - implement this
    }
    
    @FXML protected void handleAddNewOrder(ActionEvent event) throws IOException {
    	// supplier -> product_offers (only show product name) -> quantity of each product and date
    	loadNewStage("Select Supplier",
    			"procurementsys/view/add_new_order_supplier_selection_view.fxml", false);
    }
    
    @FXML protected void handleViewOrders(ActionEvent event) throws IOException {
    	// TODO - implement this
    }
    
}
