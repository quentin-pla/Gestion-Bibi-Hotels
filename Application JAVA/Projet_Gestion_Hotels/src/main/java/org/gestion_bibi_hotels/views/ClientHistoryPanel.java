package org.gestion_bibi_hotels.views;

import org.gestion_bibi_hotels.controllers.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.gestion_bibi_hotels.models.Reservation;

import java.text.SimpleDateFormat;

/**
 * Fenêtre du service réservation
 */
public class ClientHistoryPanel extends BorderPane {
    private Button back = new Button("←");
    private Label title = new Label();
    private Button regularClientButton = new Button("Client régulier");
    private Button sendAdvertisingButton = new Button("Envoyer publicité");
    private TableView<Reservation> reservations = new TableView<>();

    /**
     * Constructeur
     */
    public ClientHistoryPanel() {
        setMinSize(MainController.width, MainController.height);
        back.getStyleClass().add("back");
        title.getStyleClass().add("h3");
        regularClientButton.getStyleClass().add("ref-button");
        sendAdvertisingButton.getStyleClass().add("ref-button");
        initReservationsTable();
        BorderPane topContent = new BorderPane();
        topContent.setLeft(back);
        topContent.setCenter(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        HBox buttons = new HBox(sendAdvertisingButton, regularClientButton);
        buttons.setSpacing(10);
        VBox centerContent = new VBox(reservations, buttons);
        centerContent.setSpacing(20);
        BorderPane.setMargin(centerContent, new Insets(10,50,0,50));
        setTop(topContent);
        setCenter(centerContent);
    }

    /**
     * Initialiser la table contenant les données
     */
    private void initReservationsTable() {
        //Taille maximale
        reservations.setMinSize(MainController.width-100, MainController.height-140);
        //Interdire la modification
        reservations.setEditable(false);
        //Colonne ID
        TableColumn<Reservation, String> id_col = new TableColumn<>("ID");
        id_col.setCellValueFactory(
            param -> new SimpleStringProperty(String.valueOf(param.getValue().getID())));
        //Colonne Type de chambre
        TableColumn<Reservation, String> room_type_col = new TableColumn<>("Type chambre");
        room_type_col.setCellValueFactory(
            param -> new SimpleStringProperty(String.valueOf(param.getValue().getRoomType().getNAME())));
        //Colonne Date d'arrivée
        TableColumn<Reservation, String> arrival_date_col = new TableColumn<>("Arrivée");
        arrival_date_col.setCellValueFactory(
            param -> new SimpleStringProperty(new SimpleDateFormat("yyyy-MM-dd").format(param.getValue().getARRIVAL_DATE())));
        //Colonne Date de départ
        TableColumn<Reservation, String> exit_date_col = new TableColumn<>("Départ");
        exit_date_col.setCellValueFactory(
            param -> new SimpleStringProperty(new SimpleDateFormat("yyyy-MM-dd").format(param.getValue().getEXIT_DATE())));
        //Colonne Nombre de chambres
        TableColumn<Reservation, String> rooms_count_col = new TableColumn<>("Chambres");
        rooms_count_col.setCellValueFactory(
            param -> new SimpleStringProperty(String.valueOf(param.getValue().getROOM_COUNT())));
        //Colonne nombre de personnes
        TableColumn<Reservation, String> people_count_col = new TableColumn<>("Personnes");
        people_count_col.setCellValueFactory(
            param -> new SimpleStringProperty(String.valueOf(param.getValue().getPEOPLE_COUNT())));
        //Ajout et dimensionnement des colonnes
        id_col.setMinWidth(10);
        reservations.getColumns().add(id_col);
        reservations.getColumns().add(room_type_col);
        reservations.getColumns().add(arrival_date_col);
        reservations.getColumns().add(exit_date_col);
        reservations.getColumns().add(rooms_count_col);
        reservations.getColumns().add(people_count_col);
    }

    //************* GETTERS & SETTERS ***************//

    public Button getBack() { return back; }

    public Label getTitle() { return title; }

    public void setPanelTitle(String value) { title.setText(value); }

    public Button getRegularClientButton() { return regularClientButton; }

    public Button getSendAdvertisingButton() { return sendAdvertisingButton; }

    public TableView<Reservation> getClientReservations() { return reservations; }
}
