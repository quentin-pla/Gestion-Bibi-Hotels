package views;

import controllers.MainController;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 * Page du service réservation
 */
public class ReservationServicePanel extends BorderPane {
    Button back = new Button("←");

    public ReservationServicePanel() {
        setMinSize(MainController.width, MainController.height);
        back.getStyleClass().add("back");
        setCenter(back);
    }

    //************* GETTERS & SETTERS ***************//

    public Button getBack() {
        return back;
    }
}
