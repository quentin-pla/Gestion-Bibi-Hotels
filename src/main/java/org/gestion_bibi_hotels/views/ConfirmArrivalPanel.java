package org.gestion_bibi_hotels.views;

import org.gestion_bibi_hotels.controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Fenêtre de confirmation de l'arrivée d'un client
 */
public class ConfirmArrivalPanel extends BorderPane {
    private Map<Integer,ObservableList<Control>> fieldsLinks = new HashMap<>();
    private Button back = new Button("←");
    private Label title = new Label();
    private Button validate = new Button("Valider");
    private VBox fields = new VBox();

    /**
     * Constructeur
     */
    public ConfirmArrivalPanel() {
        setMinSize(MainController.width, MainController.height);
        back.getStyleClass().add("back");
        title.getStyleClass().add("h3");
        BorderPane topContent = new BorderPane();
        topContent.setLeft(back);
        topContent.setCenter(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        fields.setSpacing(20);
        BorderPane.setMargin(fields, new Insets(10,40,0,40));
        setTop(topContent);
        setCenter(fields);
    }

    /**
     * Initialiser les champs
     * @param people_count nombre de personnes
     * @param client_firstname nom du client
     * @param client_lastname prénom du client
     */
    public void initFields(int people_count, String client_firstname, String client_lastname) {
        fields.getChildren().clear();
        for (int index = 1; index <= people_count; index++) {
            HBox occupantData = new HBox();
            occupantData.setAlignment(Pos.CENTER_LEFT);
            occupantData.setSpacing(20);
            Label peopleLabel = new Label("Personne n°" + index);
            peopleLabel.setMinWidth(50);
            TextField firstname = new TextField();
            firstname.setPromptText("Nom");
            firstname.setMinWidth(50);
            if (index == 1) firstname.setText(client_firstname);
            TextField lastname = new TextField();
            lastname.setPromptText("Prénom");
            lastname.setMinWidth(50);
            if (index == 1) lastname.setText(client_lastname);
            occupantData.getChildren().addAll(peopleLabel,firstname,lastname);
            ObservableList<Control> links = FXCollections.observableArrayList();
            links.addAll(firstname,lastname);
            fieldsLinks.put(index,links);
            fields.getChildren().add(occupantData);
        }
        fields.getChildren().add(validate);
    }

    /**
     * Récupérer les données saisies dans les champs
     * @return données saisies
     */
    public ArrayList<String[]> retrieveFieldsData() {
        ArrayList<String[]> data = new ArrayList<>();
        for (ObservableList<Control> links : fieldsLinks.values()) {
            TextField firstname = (TextField) links.get(0);
            TextField lastname = (TextField) links.get(1);
            data.add(new String[]{firstname.getText(),lastname.getText()});
        }
        return data;
    }

    //************* GETTERS & SETTERS ***************//

    public Button getBack() { return back; }

    public Button getValidate() { return validate; }

    public void setPanelTitle(String value) { title.setText(value); }
}
