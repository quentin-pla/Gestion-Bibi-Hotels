package views;

import controllers.MainController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * Page du service facturation
 */
public class BillingServicePanel extends BorderPane {
    private Button back = new Button("←");
    private Label title = new Label();

    public BillingServicePanel() {
        setMinSize(MainController.width, MainController.height);
        back.getStyleClass().add("back");
        title.getStyleClass().add("h3");
        BorderPane topContent = new BorderPane();
        topContent.setLeft(back);
        topContent.setCenter(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        setTop(topContent);
    }

    //************* GETTERS & SETTERS ***************//

    public Button getBack() {
        return back;
    }

    public void setPanelTitle(String value) {
        title.setText(value);
    }
}
