package procurementsys.controller;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.BreadCrumbBar;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.SegmentedButton;

import procurementsys.model.CostChange;
import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.Tag;
import procurementsys.model.database.MySQLOrderDAO;
import procurementsys.model.database.MySQLProductDAO;
import procurementsys.model.database.MySQLProductOfferDAO;
import procurementsys.model.database.MySQLSupplierDAO;
import procurementsys.model.database.MySQLTagDAO;
import procurementsys.model.database.OrderDAO;
import procurementsys.model.database.ProductDAO;
import procurementsys.model.database.ProductOfferDAO;
import procurementsys.model.database.SupplierDAO;
import procurementsys.model.database.TagDAO;
import procurementsys.view.AutoCompleteComboBoxListener;
import procurementsys.view.NumericTextField;
import procurementsys.view.SoftwareNotification;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
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
	@FXML private SegmentedButton segmentedButton;
	
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
	
	private Tooltip toolTip;
	
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
		
		toolTip = new Tooltip("Double click tag to remove from current search");
		
		// Remove selected tag when double clicked
		selectedTagsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) {
					int index = selectedTagsListView.getSelectionModel()
							.getSelectedIndex();
					selectedTagsListView.getItems().remove(index);
					showTaggedProducts(selectedTagsListView.getItems());
					
					selectedTagsListView.setTooltip(
							(selectedTagsListView.getItems().size() == 0) 
							? null 
							: toolTip);
				}
			}
		});
		
		// Initialize segemented button
		ToggleButton compareSuppliersToggle = new ToggleButton("Suppliers");
		ToggleButton productTagsToggle  = new ToggleButton("Products");
		productTagsToggle.setSelected(true);
		segmentedButton.getButtons().addAll(productTagsToggle, compareSuppliersToggle);
		segmentedButton.setPrefSize(300, 100);
		segmentedButton.getStyleClass().add(SegmentedButton.STYLE_CLASS_DARK);
		//segmentedButton.getToggleGroup().selectToggle(productTagsToggle);
		
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
	
    @FXML protected void handleAddNewSupplier(ActionEvent event) throws IOException {
    	AddSupplierController.run();
    }
	
    @FXML protected void handleViewSuppliers(ActionEvent event)
    		throws IOException {
    	SupplierDAO supplierDAO = new MySQLSupplierDAO();
    	
    	if (supplierDAO.isEmpty()) {
			String errorMsg = "There are no suppliers in the system. Please add a supplier first.";
			SoftwareNotification.notifyError(errorMsg);
    	} else {
    		ViewAllSuppliersController.run();
    	}
    }
    
    
    @FXML protected void handleAddNewProduct(ActionEvent event) throws IOException {
    	AddProductController.run();
    }
    
    @FXML protected void handleAssignProduct(ActionEvent event) throws IOException {
    	SupplierDAO supplierDAO = new MySQLSupplierDAO();
    	ProductDAO productDAO = new MySQLProductDAO();
    	
    	if (supplierDAO.isEmpty()) {
			String errorMsg = "There are no suppliers in the system. Please add a supplier first.";
			SoftwareNotification.notifyError(errorMsg);
    	} else if(productDAO.isEmpty()) {
			String errorMsg = "There are no products in the system. Please add a product first.";
			SoftwareNotification.notifyError(errorMsg);
    	} else {
    		AssignProductController.run();
    	}
    }
    
    @FXML protected void handleViewProducts(ActionEvent event) 
    		throws IOException {
    	ProductDAO productDAO = new MySQLProductDAO();
    	
    	if(productDAO.isEmpty()) {
			String errorMsg = "There are no products in the system."
					+ " Please add a product first.";
			SoftwareNotification.notifyError(errorMsg);
    	} else {
    		ViewAllProductsController.run();
    	}
    }
    
    @FXML protected void handleAddNewTag(ActionEvent event) throws IOException {
    	AddTagController.run();
    }
    
    @FXML protected void handleViewProductOffers(ActionEvent event) throws IOException {
    	SupplierDAO supplierDAO = new MySQLSupplierDAO();
    	ProductOfferDAO productOfferDAO = new MySQLProductOfferDAO();
    	
    	if (supplierDAO.isEmpty()) {
			String errorMsg = "There are no suppliers in the system. Please add a supplier first.";
			SoftwareNotification.notifyError(errorMsg);
    	} else if (productOfferDAO.isEmpty()) {
			String errorMsg = "There are no product offers in the system. Please add a product offer first.";
			SoftwareNotification.notifyError(errorMsg);
    	} else {
    		ViewProductOffersController.run();
    	}
    	
    }
    
    @FXML protected void handleTagProductOffer(ActionEvent event) throws IOException {
    	TagDAO tagDAO = new MySQLTagDAO();
    	ProductOfferDAO productOfferDAO = new MySQLProductOfferDAO();
    	
    	if (tagDAO.isEmpty()) {
    		String errorMsg = "There are no tags in the system."
    				+ " Please add a tag first.";
    		SoftwareNotification.notifyError(errorMsg);
    	} else if (productOfferDAO.isEmpty()) {
			String errorMsg = "There are no product offers in the system. Please add a product offer first.";
			SoftwareNotification.notifyError(errorMsg);
    	} else {
    		TagProductOfferController.run();
    	}
    }
    
    @FXML protected void handleAddCostChange(ActionEvent event) throws IOException {
    	ProductOfferDAO productOfferDAO = new MySQLProductOfferDAO();
    	
    	if (productOfferDAO.isEmpty()) {
			String errorMsg = "There are no product offers in the system. Please add a product offer first.";
			SoftwareNotification.notifyError(errorMsg);
    	} else {
    		ChangeCostController.run();
    	}
    }
    
    @FXML protected void handleViewCostChanges(ActionEvent event) throws IOException {
    	OrderDAO orderDAO = new MySQLOrderDAO();
    	
    	if (orderDAO.isEmpty()) {
    		String errorMsg = "There are no orders in the system."
    				+ " Please add an order first.";
    		SoftwareNotification.notifyError(errorMsg);
    	} else {
    		ViewCostChangesController.run();
    	}
    }
    
    @FXML protected void handleViewTags(ActionEvent event) throws IOException {
    	TagDAO tagDAO = new MySQLTagDAO();
    	
    	if (tagDAO.isEmpty()) {
    		String errorMsg = "There are no tags in the system."
    				+ " Please add a tag first.";
    		SoftwareNotification.notifyError(errorMsg);
    	} else {
    		ViewAllTagsController.run();
    	}
    }
    
    @FXML protected void handleAddNewOrder(ActionEvent event) throws IOException {
    	SupplierDAO supplierDAO = new MySQLSupplierDAO();
    	ProductDAO productDAO = new MySQLProductDAO();
    	if (supplierDAO.isEmpty()) {
			String errorMsg = "There are no suppliers in the system. Please add a supplier first.";
			SoftwareNotification.notifyError(errorMsg);
    	} else if(productDAO.isEmpty()) {
			String errorMsg = "There are no products in the system. Please add a product first.";
			SoftwareNotification.notifyError(errorMsg);
    	} else {
    		AddOrderController.run();
    	}
    }
    
    @FXML protected void handleViewOrders(ActionEvent event) throws IOException {
    	OrderDAO orderDAO = new MySQLOrderDAO();
    	
    	if (orderDAO.isEmpty()) {
    		String errorMsg = "There are no orders in the system."
    				+ " Please add an order first.";
    		SoftwareNotification.notifyError(errorMsg);
    	} else {
    		ViewAllOrdersController.run();
    	}
    }
    
    @FXML protected void handleAddDelivery(ActionEvent event) throws IOException {
    	OrderDAO orderDAO = new MySQLOrderDAO();
    	
    	if (orderDAO.isEmpty()) {
    		String errorMsg = "There are no orders in the system."
    				+ " Please add an order first.";
    		SoftwareNotification.notifyError(errorMsg);
    	} else {
    		AddDeliveryController.run();
    	}
    	
    }
    
}
