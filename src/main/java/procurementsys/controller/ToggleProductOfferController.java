package procurementsys.controller;

import java.io.IOException;
import java.util.List;

import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;
import procurementsys.model.database.MySQLSupplierDAO;
import procurementsys.model.database.SupplierDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ToggleProductOfferController {
	private static ProductOfferTableController tableController;
	
	public static void run() {
		Stage stage = new Stage();
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 20, 10, 10));
		
		
		Label productOfferLbl = new Label("Product Offer:");
		productOfferLbl.setStyle("-fx-font-weight: bold");
		TableView<ProductOffer> productOfferTable = createTable();
		grid.add(productOfferLbl, 1, 0);
		grid.add(productOfferTable, 1, 1);
		
		stage.setTitle("View Product Offers");
		stage.setScene(new Scene(grid));
		stage.show();
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
	
}