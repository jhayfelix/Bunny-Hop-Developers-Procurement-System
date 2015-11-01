package procurementsys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Jan Tristan Milan
 */

public class ProcurementSystem extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("view/main_menu_view.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Kimson Trading Procurement System");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();	
	}

	public static void main(String[] args) {
		launch(args);
//		 String url = "jdbc:mysql://localhost:3306/";
//		  String dbName = "procurementdb";
//		  String driver = "com.mysql.jdbc.Driver";
//		  String userName = "root"; 
//		  String password = "pagtalunan";
//		  
//		  Connection con = null;
//	        PreparedStatement pst = null;
//		  try {
//		  Class.forName(driver).newInstance();
//		  Connection conn = DriverManager.getConnection(url+dbName,userName,password);
//		  
//		  conn.close();
//		  } catch (Exception e) {
//		  e.printStackTrace();
//		  }
//	



	}
	
}
