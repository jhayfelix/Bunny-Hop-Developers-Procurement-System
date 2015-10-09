package procurementsys;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SuccessDialogController extends Controller {
	@FXML protected void handleConfirmSuccessBtnPress(ActionEvent event) {
		getStage(event).close();
	}
	
	@FXML protected void handleCancelBtnPress(ActionEvent event) {
		getStage(event).close();
	}
}
