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
 * Fenêtre de sélection des services lors de la facturation d'un service
 */
public class SelectServicePanel extends BorderPane {
    private Button back = new Button("←");
    private Label title = new Label();
    private Label subtitle = new Label("Sélection du service");
    private ComboBox<String> services = new ComboBox<>();
    private Button validate = new Button("Valider");

    public SelectServicePanel() {
        setMinSize(MainController.width, MainController.height);
        title.getStyleClass().add("h3");
        subtitle.getStyleClass().add("h2");
        back.getStyleClass().add("back");
        services.setMinWidth(230);
        validate.setMinWidth(230);
        BorderPane topContent = new BorderPane();
        topContent.setLeft(back);
        topContent.setCenter(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        VBox centerContent = new VBox(subtitle, services, validate);
        VBox.setMargin(title, new Insets(0,0,10,0));
        centerContent.setSpacing(15);
        centerContent.setAlignment(Pos.CENTER);
        setTop(topContent);
        setCenter(centerContent);
    }

    //************* GETTERS & SETTERS ***************//

    public ComboBox<String> getServices() {
        return services;
    }

    public Button getBack() { return back; }

    public Button getValidate() {
        return validate;
    }

    public void setPanelTitle(String value) { title.setText(value); }
}
