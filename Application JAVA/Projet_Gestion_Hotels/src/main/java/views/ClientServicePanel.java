package views;

import controllers.MainController;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 * Page du service client
 */
public class ClientServicePanel extends BorderPane {
    private Button back = new Button("←");

    public ClientServicePanel() {
        setMinSize(MainController.width, MainController.height);
        back.getStyleClass().add("back");
        setTop(back);
    }

    //************* GETTERS & SETTERS ***************//

    public Button getBack() {
        return back;
    }
}
