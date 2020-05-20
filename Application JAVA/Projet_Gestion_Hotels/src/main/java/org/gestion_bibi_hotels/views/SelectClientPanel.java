package org.gestion_bibi_hotels.views;

import org.gestion_bibi_hotels.controllers.MainController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Fenêtre de sélection du client
 */
public class SelectClientPanel extends BorderPane {
    private Button back = new Button("←");
    private Label title = new Label("Sélection du client");
    private ComboBox<String> clients = new ComboBox<>();
    private Button validate = new Button("Valider");

    /**
     * Constructeur
     */
    public SelectClientPanel() {
        setMinSize(MainController.width, MainController.height);
        title.getStyleClass().add("h2");
        back.getStyleClass().add("back");
        clients.setMinWidth(230);
        validate.setMinWidth(230);
        VBox content = new VBox(title, clients, validate);
        VBox.setMargin(title, new Insets(0,0,10,0));
        content.setSpacing(15);
        content.setAlignment(Pos.CENTER);
        setCenter(content);
        setTop(back);
    }

    //************* GETTERS & SETTERS ***************//

    public ComboBox<String> getClients() {
        return clients;
    }

    public Button getBack() { return back; }

    public Button getValidate() {
        return validate;
    }
}
