package procurementsys;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import procurementsys.model.Delivery;
import procurementsys.model.Order;
import procurementsys.model.Payment;
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
		ret.add(new Supplier("De La Salle University", "524-4611"));
		ret.add(new Supplier("National Bookstore", "332-1206"));
		ret.add(new Supplier("SM Supermarket", "632-7122"));
		return ret;
	}
	
	public List<ProductOffer> parseProductOfferList(Product product)  {
		List<ProductOffer> ret = new ArrayList<>();
		if (product.getName().equals("Century Tuna")) {
			ret.add(new ProductOffer(product, 
					new Supplier("De La Salle University", "09275905177"), 57.0));
		} else if (product.getName().equals("Mineral Water")) {
			ret.add(new ProductOffer(product, 
					new Supplier("Anton's Factory", "09275905177"), 25.0));
		}
		
		switch (product.getName()) {
			case "Century Tuna" : {
				ret.add(new ProductOffer(product, new Supplier("SM Supermarket", "632-7122"), 45.75));
				break;
			}
			case "Diapers" : {
				ret.add(new ProductOffer(product, new Supplier("SM Supermarket", "632-7122"), 30.0));
				break;
			}
			case "Marker" : {
				ret.add(new ProductOffer(product, new Supplier("National Bookstore", "332-1206"), 20.0));
				break;
			}
			case "Stapler" : {
				ret.add(new ProductOffer(product, new Supplier("National Bookstore", "332-1206"), 250.0));
				break;
			}
			case "Crayons" : {
				ret.add(new ProductOffer(product, new Supplier("National Bookstore", "332-1206"), 25.0));
				break;
			}
			case "Glue" : {
				ret.add(new ProductOffer(product, new Supplier("National Bookstore", "332-1206"), 27.0));
				break;
			}
			case "DLSU Lanyard": {
				ret.add(new ProductOffer(product, new Supplier("De La Salle University", "524-4611"), 150.0));
				break;
			}
			case "Intermediate Pad Paper": {
				ret.add(new ProductOffer(product, new Supplier("National Bookstore", "332-1206"), 20.0));
				ret.add(new ProductOffer(product, new Supplier("De La Salle University", "524-4611"), 25.0));
				break;
			}
			case "Brown Envelope": {
				ret.add(new ProductOffer(product, new Supplier("National Bookstore", "332-1206"), 10.0));
				ret.add(new ProductOffer(product, new Supplier("De La Salle University", "524-4611"), 12.0));
				break;
			}
			case "Folder": {
				ret.add(new ProductOffer(product, new Supplier("National Bookstore", "332-1206"), 15.0));
				ret.add(new ProductOffer(product, new Supplier("De La Salle University", "524-4611"), 20.0));
				break;
			}
			case "Ballpen": {
				ret.add(new ProductOffer(product, new Supplier("SM Supermarket", "632-7122"), 12.0));
				ret.add(new ProductOffer(product, new Supplier("National Bookstore", "332-1206"), 10.0));
				ret.add(new ProductOffer(product, new Supplier("De La Salle University", "524-4611"), 15.0));
				break;
			}
			case "Mineral Water": {
				ret.add(new ProductOffer(product, new Supplier("SM Supermarket", "632-7122"), 15.0));
				ret.add(new ProductOffer(product, new Supplier("National Bookstore", "332-1206"), 25.0));
				ret.add(new ProductOffer(product, new Supplier("De La Salle University", "524-4611"), 30.0));
				break;
			}
		}
		return ret;
	}
	
	public List<ProductOffer> parseProductOfferList(Supplier supplier) {
		List<ProductOffer> ret = new ArrayList<>();		
		List<Product> products = parseProductList();
	
		for (Product p : products) {
			List<ProductOffer> productOffers = parseProductOfferList(p);
			for (ProductOffer po : productOffers) {
				if (po.getSupplier().equals(supplier)) {
					ret.add(po);
				}
			}
		}
		
		return ret;
	}
	
	public List<Order> parseOrderList() {
	   	List<Order> ret = new ArrayList<>();
	   	List<Supplier> suppliers = parseSupplierList();

	   	// Ord
	   	Map<ProductOffer, Integer> productOffersOrdered1 = new HashMap<>();
	   	List<ProductOffer> productOffers1 = parseProductOfferList(new Supplier("National Bookstore", "332-1206"));
	   	productOffersOrdered1.put(productOffers1.get(0), 10);
	   	productOffersOrdered1.put(productOffers1.get(1), 2);
	   	productOffersOrdered1.put(productOffers1.get(2), 4);
    	ret.add(new Order(LocalDateTime.now(), suppliers.get(0),
    			productOffersOrdered1));
    	
    	Map<ProductOffer, Integer> deliveryMap1_1 = new HashMap<>();
    	deliveryMap1_1.put(productOffers1.get(0), 5);
    	deliveryMap1_1.put(productOffers1.get(1), 0);
    	deliveryMap1_1.put(productOffers1.get(2), 1);
    	
    	
    	Map<ProductOffer, Integer> deliveryMap1_2 = new HashMap<>();
    	deliveryMap1_2.put(productOffers1.get(0), 5);
    	deliveryMap1_2.put(productOffers1.get(1), 2);
    	deliveryMap1_2.put(productOffers1.get(2), 0);
    	
    	ret.get(0).addDelivery(new Delivery(LocalDateTime.now(), deliveryMap1_1));
    	ret.get(0).addDelivery(new Delivery(LocalDateTime.now(), deliveryMap1_2));
    	ret.get(0).addPayment(new Payment(LocalDateTime.now(), 5));
    	ret.get(0).addPayment(new Payment(LocalDateTime.now(), 15));
    	
    	return ret;
	}
}
