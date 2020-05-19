package views;

import controllers.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import models.Occupation;

import java.util.Map;

/**
 * Fenêtre du service client
 */
public class ClientServicePanel extends BorderPane {
    private Button back = new Button("←");
    private Label title = new Label("Liste des occupations en cours");
    private TableView<Occupation> occupations = new TableView<>();
    private Button clientHistoryButton = new Button("Historique Client");
    private Button presenceButton = new Button("Client présent");
    private Button billServiceButton = new Button("Facturer service");
    private HBox refButtons = new HBox(clientHistoryButton,billServiceButton, presenceButton);

    /**
     * Constructeur
     */
    public ClientServicePanel() {
        setMinSize(MainController.width, MainController.height);
        back.getStyleClass().add("back");
        title.getStyleClass().add("h3");
        presenceButton.getStyleClass().add("ref-button");
        billServiceButton.getStyleClass().addAll("ref-button");
        initOccupationsTable();
        BorderPane topContent = new BorderPane();
        topContent.setLeft(back);
        topContent.setCenter(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        refButtons.setSpacing(10);
        VBox centerContent = new VBox(occupations,refButtons);
        centerContent.setSpacing(20);
        BorderPane.setMargin(centerContent, new Insets(10,50,0,50));
        setTop(topContent);
        setCenter(centerContent);
    }

    /**
     * Initialiser le tableau contenant la liste des occupations
     */
    private void initOccupationsTable() {
        //Taille maximale
        occupations.setMinSize(MainController.width-100, MainController.height-140);
        //Interdire la modification
        occupations.setEditable(false);
        //Colonne ID
        TableColumn<Occupation, String> id_col = new TableColumn<>("ID");
        id_col.setCellValueFactory(
                param -> new SimpleStringProperty(String.valueOf(param.getValue().getID())));
        //Colonne Hotel
        TableColumn<Occupation, String> hotel_col = new TableColumn<>("Hôtel");
        hotel_col.setCellValueFactory(
                param -> new SimpleStringProperty(String.valueOf(param.getValue().getRoom().getHotel().getNAME())));
        //Colonne Réservation
        TableColumn<Occupation, String> reservation_col = new TableColumn<>("Réservation");
        reservation_col.setCellValueFactory(
                param -> new SimpleStringProperty(String.valueOf(param.getValue().getRESERVATION_ID())));
        //Colonne Chambre
        TableColumn<Occupation, String> room_col = new TableColumn<>("Chambre");
        room_col.setCellValueFactory(
                param -> new SimpleStringProperty(String.valueOf(param.getValue().getROOM_ID())));
        //Colonne Type de chambre
        TableColumn<Occupation, String> room_type_col = new TableColumn<>("Type de chambre");
        room_type_col.setCellValueFactory(
                param -> new SimpleStringProperty(String.valueOf(param.getValue().getRoom().getRoomType().getNAME())));
        //Colonne Client présent
        TableColumn<Occupation, String> presence_col = new TableColumn<>("Présence client");
        presence_col.setCellValueFactory(
                param -> {
                    boolean condition = param.getValue().getIS_CLIENT_PRESENT();
                    colorColumn(presence_col, Map.of("Oui",Color.GREEN,"Non",Color.RED));
                    return new SimpleStringProperty(condition ? "Oui" : "Non");
                });
        //Ajout et dimensionnement des colonnes
        id_col.setMinWidth(10);
        hotel_col.setMinWidth(100);
        reservation_col.setMinWidth(100);
        room_col.setMinWidth(100);
        room_type_col.setMinWidth(150);
        presence_col.setMinWidth(140);
        occupations.getColumns().add(id_col);
        occupations.getColumns().add(hotel_col);
        occupations.getColumns().add(reservation_col);
        occupations.getColumns().add(room_col);
        occupations.getColumns().add(room_type_col);
        occupations.getColumns().add(presence_col);
    }

    /**
     * Colorer une colonne en fonction de la valeur booléenne
     * @param column colonne
     * @param conditionsColors couleur pour chaque condition
     */
    private void colorColumn(TableColumn<Occupation, String> column, Map<String,Color> conditionsColors) {
        column.setCellFactory(new Callback<>() {
            public TableCell<Occupation, String> call(TableColumn param) {
                return new TableCell<>() {
                    @Override
                    public void updateItem(String item, boolean value) {
                        super.updateItem(item, value);
                        if (!isEmpty()) {
                            setTextFill(conditionsColors.get(item));
                            setText(item);
                        }
                    }
                };
            }
        });
    }

    //************* GETTERS & SETTERS ***************//

    public Button getBack() {
        return back;
    }

    public TableView<Occupation> getOccupations() {
        return occupations;
    }

    public Button getClientHistoryButton() { return clientHistoryButton; }

    public Button getPresenceButton() {
        return presenceButton;
    }

    public Button getBillServiceButton() {
        return billServiceButton;
    }

    public HBox getRefButtons() {
        return refButtons;
    }
}
