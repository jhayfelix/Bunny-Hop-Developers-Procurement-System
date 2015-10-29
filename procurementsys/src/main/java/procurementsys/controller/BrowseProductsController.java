package procurementsys.controller;

import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * @author Jan Tristan Milan
 */

public class BrowseProductsController extends Controller {
	@FXML private ListView<Product> productList;
	@FXML private TableView<ProductOffer> suppliersForProductTable;
	@FXML private TableColumn<ProductOffer, Double> costColumn;
	@FXML private TableColumn<ProductOffer, Supplier> supplierColumn;
	@FXML private TableColumn<ProductOffer, String> contactNumberColumn;
	
	@FXML private void initialize() {		
		costColumn.setCellValueFactory(
				new PropertyValueFactory<ProductOffer, Double>("cost"));
		supplierColumn.setCellValueFactory(
				new PropertyValueFactory<ProductOffer,Supplier>("supplier"));
		contactNumberColumn.setCellValueFactory(
				new PropertyValueFactory<ProductOffer, String>("contactNumber"));
		
		productList.getSelectionModel().selectedItemProperty()
			.addListener(new ChangeListener<Product>() {
				@Override
				public void changed(
						ObservableValue<? extends Product> observable,
						Product oldValue, Product newValue) {
					suppliersForProductTable.getItems().setAll(parseProductOfferList(newValue));
				}
			});
		
		productList.getItems().addAll(parseProductList());
		productList.getSelectionModel().select(0);
	}
}
