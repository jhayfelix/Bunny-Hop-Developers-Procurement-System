package procurementsys.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import procurementsys.model.Order;

import procurementsys.model.Product;
import procurementsys.model.ProductOffer;
import procurementsys.model.Supplier;

/**
 * @author Jan Tristan Milan
 */

public abstract class Controller {
	
	protected Stage getStage(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		return stage;
	}
	
    protected void loadNewStage(String title, String path, boolean isResizable)
    		throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource(path));
    	Stage stage = new Stage();
    	stage.setTitle(title);
    	stage.setScene(new Scene(root));
    	stage.setResizable(isResizable);
    	stage.show();
    }
    
    
    /*
     *  The parseLists functions only contain dummy data at this point.
     *  There is no database at the moment.
     */
    
    protected List<Product> parseProductList() {
		List<Product> ret = new ArrayList<>();
		// TODO - get from database
		
		ret.add(new Product("Ballpen"));
		ret.add(new Product("Brown Envelope"));
		ret.add(new Product("Century Tuna"));
		ret.add(new Product("Crayons"));
		ret.add(new Product("Diapers"));
		ret.add(new Product("DLSU Lanyard"));
		ret.add(new Product("Folder"));
		ret.add(new Product("Glue"));
		ret.add(new Product("Intermediate Pad Paper"));
		ret.add(new Product("Marker"));
		ret.add(new Product("Mineral Water"));
		ret.add(new Product("Stapler"));
		
		return ret;
	}
	
	public List<Supplier> parseSupplierList() {
		List<Supplier> ret = new ArrayList<>();
		// TODO - implement this
		return ret;
	}
	
	public List<ProductOffer> parseProductOfferList(Product product)  {
		List<ProductOffer> ret = new ArrayList<>();
		// TODO - implement this
		return ret;
	}
	
	public List<ProductOffer> parseProductOfferList(Supplier supplier) {
		List<ProductOffer> ret = new ArrayList<>();		
		// TODO - implement this
		return ret;
	}
	
	public List<Order> parseOrderList() {
	   	List<Order> ret = new ArrayList<>();
	   	// TODO - implement this
    	return ret;
	}
}
