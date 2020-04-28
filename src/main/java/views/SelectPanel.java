package views;

import controllers.MainController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Page d'accueil
 */
public class SelectPanel extends BorderPane {
    private Label brand                 = new Label("BestHotels");
    private Button service_client       = new Button("Service\nClient");
    private Button service_reservation  = new Button("Service\nRéservation");
    private Button service_facturation  = new Button("Service\nFacturation");
    private Button administration       = new Button("Administration");
    private ImageView icon              = new ImageView(new Image("/hotels.png"));

    public SelectPanel() {
        setMinSize(MainController.width, MainController.height);
        brand.getStyleClass().add("title");
        service_client.getStyleClass().add("select-button");
        service_reservation.getStyleClass().add("select-button");
        service_facturation.getStyleClass().add("select-button");
        administration.getStyleClass().add("select-button");
        icon.setFitHeight(200);
        icon.setFitWidth(200);
        HBox selectButtons = new HBox(service_client,service_reservation,service_facturation,administration);
        selectButtons.setSpacing(30);
        selectButtons.setAlignment(Pos.CENTER);
        VBox content = new VBox(icon,brand,selectButtons);
        VBox.setMargin(brand, new Insets(0,0,40,0));
        content.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(content, Pos.CENTER);
        setCenter(content);
    }

    //************* GETTERS & SETTERS ***************//

    public Button getService_client() {
        return service_client;
    }

    public Button getService_reservation() { return service_reservation; }

    public Button getService_facturation() {
        return service_facturation;
    }

    public Button getAdministration() {
        return administration;
    }
}
