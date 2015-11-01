package procurementsys.controller;

import java.io.IOException;
import java.util.List;

import procurementsys.model.Tag;
import procurementsys.model.database.MySQLTagDAO;
import procurementsys.model.database.TagDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ViewAllTagsController {
	private static TagListController controller;
	
	public static void run() {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(ViewAllProductsController
					.class.getResource("/procurementsys/view/tag_list.fxml"));
			Parent root = loader.load();
			controller = loader.getController();
			
			initializeTagList();
			
			stage.setScene(new Scene(root));
			stage.setTitle("Tags");
			stage.show();
			
			showTags();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void initializeTagList() {
		TextField tagFilter = controller.getFilterTextField();
		
		tagFilter.textProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				showTags();	
			}
			
		});
	}
	
	public static void showTags() {
		ListView<Tag> tagListView = controller.getListView();
		TextField tagFilter = controller.getFilterTextField();
		
		TagDAO tagDAO = new MySQLTagDAO();
		List<Tag> tags = tagDAO.getAll(tagFilter.getText());
		tagListView.getItems().clear();
		tagListView.getItems().addAll(tags);
		
		if (tagListView.getItems().size() > 0) {
			tagListView.getSelectionModel().select(0);
		}
		
	}
}
