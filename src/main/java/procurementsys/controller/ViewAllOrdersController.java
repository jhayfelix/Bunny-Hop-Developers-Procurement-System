package procurementsys.controller;

import java.time.LocalDateTime;

import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.controlsfx.control.MasterDetailPane;

import procurementsys.model.Order;
import procurementsys.model.OrderDeliveryStatus;
import procurementsys.model.Supplier;
import procurementsys.model.database.MySQLOrderDAO;
import procurementsys.model.database.OrderDAO;
import procurementsys.view.DeliveryProgressView;

public class ViewAllOrdersController {
	private static MasterDetailPane masterDetailPane;
	private static Text dateTimeInfo;
	private static Text supplierInfo;
	private static DeliveryProgressView deliveryStatsInfo;
	
	public static void run() {
		Stage stage = new Stage();
		stage.setWidth(455);
		masterDetailPane = new MasterDetailPane();
		
		TableView<Order> tableView = createTable();
		tableView.setPrefWidth(455);
		tableView.setTooltip(new Tooltip("Double click a row to show details"));
		masterDetailPane.setMasterNode(tableView);
		
		Node detailNode = createDetailsNode();
		
		masterDetailPane.setDetailNode(detailNode);
		masterDetailPane.setDetailSide(Side.BOTTOM);
		masterDetailPane.showDetailNodeProperty().set(false);
		masterDetailPane.setDividerPosition(.4);
		
		stage.setScene(new Scene(masterDetailPane));
		stage.setTitle("Orders");
		stage.show();
	}
	
	private static TableView<Order> createTable() {
		TableView<Order> orderTable = new TableView<>();
		
		initializeTableColumns(orderTable);
		
		orderTable.setRowFactory( tableView -> {
		    TableRow<Order> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
		        	masterDetailPane.showDetailNodeProperty().set(true);
		        	Order selectedOrder = row.getItem();
		        	dateTimeInfo.setText(selectedOrder.getOrderDateTime() + "");
		        	supplierInfo.setText(selectedOrder.getSupplier() + "");
		        	deliveryStatsInfo.setOrder(selectedOrder);
		        }
		    });
		    return row ;
		});
		
		OrderDAO orderDAO = new MySQLOrderDAO();
		orderTable.getItems().setAll(orderDAO.getAll());
		return orderTable;
	}
	
	@SuppressWarnings("unchecked")
	private static void initializeTableColumns(TableView<Order> table) {
		
		TableColumn<Order, LocalDateTime> dateTimeOrderedColumn = new TableColumn<>();
		dateTimeOrderedColumn.setText("DateTime Ordered");
		
		TableColumn<Order, Supplier> supplierColumn = new TableColumn<>();
		supplierColumn.setText("Supplier");
		
		TableColumn <Order, OrderDeliveryStatus> deliveryStatusColumn = new TableColumn<>();
		deliveryStatusColumn.setText("Delivery Status");
		
		dateTimeOrderedColumn.setCellValueFactory(new PropertyValueFactory<Order,
				LocalDateTime>("orderDateTime"));
		supplierColumn.setCellValueFactory(new PropertyValueFactory<Order,
				Supplier>("supplier"));
		deliveryStatusColumn.setCellValueFactory(new PropertyValueFactory<Order,
				OrderDeliveryStatus>("orderDeliveryStatus"));
		
		table.getColumns().addAll(dateTimeOrderedColumn, supplierColumn, deliveryStatusColumn);
		
	}
	
	public static Node createDetailsNode() {
		VBox vBox = new VBox();
		vBox.setSpacing(10);
		vBox.setPadding(new Insets(10,10,10,10));
		
		VBox dateTimeHBox = new VBox();
		Label dateTimeLbl = new Label("DateTime Ordered:");
		dateTimeLbl.setStyle("-fx-font-weight: bold");
		dateTimeInfo = new Text();
		dateTimeHBox.getChildren().addAll(dateTimeLbl, dateTimeInfo);
		
		VBox supplierHBox = new VBox();
		Label supplierLbl = new Label("Supplier:");
		supplierLbl.setStyle("-fx-font-weight: bold");
		supplierInfo = new Text();
		supplierHBox.getChildren().addAll(supplierLbl, supplierInfo);

		VBox deliveryStatsHBox = new VBox();
		Label deliveryStatsLbl = new Label("Delivery Status:");
		deliveryStatsLbl.setStyle("-fx-font-weight: bold");
		deliveryStatsInfo = new DeliveryProgressView();
		deliveryStatsHBox.getChildren().addAll(deliveryStatsLbl, deliveryStatsInfo);
		
		vBox.getChildren().addAll(dateTimeHBox, supplierHBox, deliveryStatsHBox);
		
	    vBox.setOnMouseClicked(event -> {
	        if (event.getClickCount() == 2) {
	        	masterDetailPane.showDetailNodeProperty().set(false);
	        }
	    });
		Tooltip.install(vBox, new Tooltip("Double click anywhere in the "
				+ "details pane to hide"));
		return vBox;
	}
}
