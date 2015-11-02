package procurementsys.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.database.MySQLProductOfferDAO;
import procurementsys.model.database.ProductOfferDAO;

/**
 * @author Jan Tristan Milan
 */

public class ProductOfferTableController extends Controller {
	@FXML private TableView<ProductOffer> productOfferTable;
	@FXML private TableColumn<ProductOffer, Product> productColumn;
	@FXML private TableColumn<ProductOffer, Double> costColumn;
	@FXML private TableColumn<ProductOffer, String> tagsColumn;
	
	@FXML private void initialize() {
		productColumn.setCellValueFactory(
				new PropertyValueFactory<ProductOffer, Product>("product"));
		costColumn.setCellValueFactory(
				new PropertyValueFactory<ProductOffer, Double>("currentCost"));
		tagsColumn.setCellValueFactory(
				new PropertyValueFactory<ProductOffer, String>("tagsString"));
	}
	
	public void setSupplier(Supplier supplier) {		
		ProductOfferDAO productOfferDAO = new MySQLProductOfferDAO();
		productOfferTable.getItems().addAll(productOfferDAO.getAll(supplier));
		
		if (productOfferTable.getItems().size() > 0) {
			productOfferTable.getSelectionModel().select(0);
		}
	}
	
	public ProductOffer getSelectedProductOffer() {
		return productOfferTable.getSelectionModel().getSelectedItem();
	}
 }
