package views;

import controllers.MainController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

/**
 * Page du service réservation
 */
public class ReservationServicePanel extends BorderPane {
    private Button back = new Button("←");
    private Label title = new Label();
    private ListView<String> reservations = new ListView<>();

    public ReservationServicePanel() {
        setMinSize(MainController.width, MainController.height);
        back.getStyleClass().add("back");
        reservations.getItems().add("test");
        title.getStyleClass().add("h3");
        BorderPane topContent = new BorderPane();
        topContent.setLeft(back);
        topContent.setCenter(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        setTop(topContent);
        setCenter(reservations);
    }

    //************* GETTERS & SETTERS ***************//

    public Button getBack() {
        return back;
    }

    public void setPanelTitle(String value) {
        title.setText(value);
    }
}
