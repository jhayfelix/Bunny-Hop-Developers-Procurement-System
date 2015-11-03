package procurementsys.controller;
import java.io.IOException;
import java.util.ArrayList;

import org.controlsfx.control.SegmentedButton;

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
import procurementsys.view.SoftwareNotification;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


/**
 * @author Jan Tristan Milan
 */

public class MainMenuController extends Controller {
	@FXML private VBox rootVBox;
	@FXML private ComboBox<Tag> tagSearchComboBox;
	@FXML private ListView<Tag> selectedTagsListView;
	@FXML private SegmentedButton segmentedButton;
	
	private SplitPane normalRoot;
	private SplitPane compareRoot;
	
	private NormalMainModeController normalModeController;
	private CompareMainModeController compareModeController;
	private Tooltip toolTip;
	
	public MainMenuController() {
		
	}
	
	@FXML private void initialize() {
		initializeMode();
		
		// Set up autocomplete searching of tags
		TagDAO tagDAO = new MySQLTagDAO();
		tagSearchComboBox.getItems().addAll(tagDAO.getAll());
		new AutoCompleteComboBoxListener<Tag>(tagSearchComboBox);
		normalModeController.showTaggedProducts(new ArrayList<>());
		
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
					normalModeController.showTaggedProducts(selectedTagsListView.getItems());
					
					selectedTagsListView.setTooltip(
							(selectedTagsListView.getItems().size() == 0) 
							? null 
							: toolTip);
				}
			}
		});
		
		// Initialize segemented button
		ToggleButton compareSuppliersToggle = new ToggleButton("Suppliers");
		compareSuppliersToggle.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				titleLabel.setText("What type of suppliers are you looking for?");
				int index = rootVBox.getChildren().size() - 1;
				rootVBox.getChildren().remove(index);
				rootVBox.getChildren().add(compareRoot);
			}
			
		});
		ToggleButton normalToggle  = new ToggleButton("Products");
		normalToggle.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				titleLabel.setText("What types of products are you looking for?");
				int index = rootVBox.getChildren().size() - 1;
				rootVBox.getChildren().remove(index);
				rootVBox.getChildren().add(normalRoot);
			}
			
		});
		normalToggle.setSelected(true);
		segmentedButton.getButtons().addAll(normalToggle, compareSuppliersToggle);
		segmentedButton.setPrefSize(300, 100);
		segmentedButton.getStyleClass().add(SegmentedButton.STYLE_CLASS_DARK);
		
		//segmentedButton.getToggleGroup().selectToggle(productTagsToggle);
		
		
		
	}
	
    
	private void initializeMode() {
		try {
			
			FXMLLoader normalLoader = new FXMLLoader(getClass()
					.getResource("/procurementsys/view/normal_mode_main.fxml"));
			normalRoot = normalLoader.load();
			normalModeController = normalLoader.getController();
			normalModeController.initialize(selectedTagsListView);
			rootVBox.getChildren().add(normalRoot);			
			
			FXMLLoader compareLoader = new FXMLLoader(getClass()
					.getResource("/procurementsys/view/compare_mode_main.fxml"));
			compareRoot = compareLoader.load();
			compareModeController = compareLoader.getController();
			compareModeController.initialize(selectedTagsListView);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void refresh() {
		TagDAO tagDAO = new MySQLTagDAO();
		tagSearchComboBox.getItems().clear();
		tagSearchComboBox.getItems().addAll(tagDAO.getAll());
		
		normalModeController.refresh();
		compareModeController.refresh();
		
	}
	

	@FXML protected void handleSearchTag(ActionEvent event) throws IOException {
		
		normalModeController.handleSearchTag(tagSearchComboBox, selectedTagsListView, toolTip);
	}
	
    @FXML protected void handleAddNewSupplier(ActionEvent event) throws IOException {
    	AddSupplierController.run();
    	refresh();
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
    	refresh();
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
    	refresh();
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
    	refresh();
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
    	refresh();
    }
    
    @FXML protected void handleAddCostChange(ActionEvent event) throws IOException {
    	ProductOfferDAO productOfferDAO = new MySQLProductOfferDAO();
    	
    	if (productOfferDAO.isEmpty()) {
			String errorMsg = "There are no product offers in the system. Please add a product offer first.";
			SoftwareNotification.notifyError(errorMsg);
    	} else {
    		ChangeCostController.run();
    	}
    	refresh();
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
    	refresh();
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
    	refresh();
    }
    
}
