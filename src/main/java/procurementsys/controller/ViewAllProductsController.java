package procurementsys.controller;

import java.io.IOException;
import java.util.List;

import procurementsys.model.Product;
import procurementsys.model.database.MySQLProductDAO;
import procurementsys.model.database.ProductDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewAllProductsController {
	private static ProductListController controller;
	
	public static void run() {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(ViewAllProductsController
					.class.getResource("/procurementsys/view/product_list.fxml"));
			Parent root = loader.load();
			controller = loader.getController();
			
			initializeSupplierList();
			
			stage.setScene(new Scene(root));
			stage.show();
			
			showProducts();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void initializeSupplierList() {
		TextField productFilter = controller.getFilterTextField();
		
		productFilter.textProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				showProducts();	
			}
			
		});
	}
	
	public static void showProducts() {
		ListView<Product> productListView = controller.getListView();
		TextField productFilter = controller.getFilterTextField();
		
		ProductDAO productDAO = new MySQLProductDAO();
		List<Product> products = productDAO.getAll(productFilter.getText());
		productListView.getItems().clear();
		productListView.getItems().addAll(products);
		
		if (productListView.getItems().size() > 0) {
			productListView.getSelectionModel().select(0);
		}
		
		controller.fireMouseClickEvent();
	}
}
