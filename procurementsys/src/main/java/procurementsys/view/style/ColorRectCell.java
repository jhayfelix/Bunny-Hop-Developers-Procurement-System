package procurementsys.view.style;

import procurementsys.model.Tag;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ColorRectCell extends ListCell<Tag> {
    @Override 
    public void updateItem(Tag item, boolean empty) {
        super.updateItem(item, empty);
        Rectangle rect = new Rectangle(100, 20);
        if (item != null) {
            
        } else {
        	rect.setFill(Color.ROYALBLUE);
        }
       
    }
}
