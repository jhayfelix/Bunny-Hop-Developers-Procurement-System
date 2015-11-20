package procurementsys.controller;

import java.util.Optional;

import org.controlsfx.control.Notifications;

import procurementsys.model.Tag;
import procurementsys.model.database.MySQLTagDAO;
import procurementsys.model.database.TagDAO;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

public class AddTagController {
	public static void run() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add Tag");
		dialog.setHeaderText("Enter tag details");
		dialog.setContentText("Tag Name:");
        dialog.setGraphic(null);
        
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            if (!isValidName(dialog.getEditor().getText())) {
    			String errorMsg = "Tag name cannot be empty."
    					+ " Please enter a tag name.";
    			
    			Notifications.create().title("Error").text(errorMsg)
    				.position(Pos.TOP_RIGHT).showError();
                event.consume();
            }
        });
         
        Optional<String> name = dialog.showAndWait();
        if (name.isPresent()){
        	TagDAO tagDAO = new MySQLTagDAO();
        	tagDAO.add(new Tag(name.get()));
        }
	}
	
	
	private static boolean isValidName(String name) {
		if (name == null || name.length() == 0) {
			return false;
		}
		return true;
	}
}
