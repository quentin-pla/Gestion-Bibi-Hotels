package org.gestion_bibi_hotels.views;

import org.gestion_bibi_hotels.controllers.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.gestion_bibi_hotels.models.Reservation;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Fenêtre du service réservation
 */
public class ReservationServicePanel extends BorderPane {
    private Button back = new Button("←");
    private Label title = new Label();
    private TableView<Reservation> reservations = new TableView<>();
    private Button confirmArrivalButton = new Button("Confirmer arrivée");
    private Button archiveButton = new Button("Archiver");
    private Button makePaymentButton = new Button("Encaisser Arrhes");
    private HBox refButtons = new HBox(confirmArrivalButton, archiveButton, makePaymentButton);

    /**
     * Constructeur
     */
    public ReservationServicePanel() {
        setMinSize(MainController.width, MainController.height);
        back.getStyleClass().add("back");
        title.getStyleClass().add("h3");
        confirmArrivalButton.getStyleClass().addAll("ref-button","green-text");
        archiveButton.getStyleClass().addAll("ref-button","red-text");
        makePaymentButton.getStyleClass().addAll("ref-button","green-text");
        initReservationsTable();
        BorderPane topContent = new BorderPane();
        topContent.setLeft(back);
        topContent.setCenter(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        refButtons.setVisible(false);
        refButtons.setSpacing(10);
        VBox centerContent = new VBox(reservations,refButtons);
        centerContent.setSpacing(20);
        BorderPane.setMargin(centerContent, new Insets(10,50,0,50));
        setTop(topContent);
        setCenter(centerContent);
    }

    /**
     * Initialiser le tableau contenant la liste des réservations
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
        //Colonne Client
        TableColumn<Reservation, String> client_col = new TableColumn<>("Client");
        client_col.setCellValueFactory(
            param -> new SimpleStringProperty(param.getValue().getClient().getFIRSTNAME() + " "
                    + param.getValue().getClient().getLASTNAME().substring(0,1).toUpperCase()
                    + param.getValue().getClient().getLASTNAME().substring(1).toLowerCase()));
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
        //Colonne est payée
        TableColumn<Reservation, String> is_payed_col = new TableColumn<>("Versement Arrhes");
        is_payed_col.setCellValueFactory(
            param -> {
                boolean condition = param.getValue().getIS_PAYED();
                colorColumn(is_payed_col, Map.of("Effectué",Color.GREEN,"En attente",Color.RED));
                return new SimpleStringProperty(condition ? "Effectué":"En attente");
            });
        //Colonne est confirmée
        TableColumn<Reservation, String> status_col = new TableColumn<>("État actuel");
        status_col.setCellValueFactory(
            param -> {
                Boolean condition = param.getValue().getIS_COMFIRMED();
                colorColumn(status_col,Map.of("Confirmée",Color.GREEN,"Enregistrée",Color.ORANGE));
                return new SimpleStringProperty(condition ? "Confirmée":"Enregistrée");
            });
        //Ajout et dimensionnement des colonnes
        id_col.setMinWidth(10);
        room_type_col.setMinWidth(130);
        is_payed_col.setMinWidth(130);
        reservations.getColumns().add(id_col);
        reservations.getColumns().add(client_col);
        reservations.getColumns().add(room_type_col);
        reservations.getColumns().add(arrival_date_col);
        reservations.getColumns().add(exit_date_col);
        reservations.getColumns().add(rooms_count_col);
        reservations.getColumns().add(people_count_col);
        reservations.getColumns().add(is_payed_col);
        reservations.getColumns().add(status_col);
    }

    /**
     * Colorer une colonne en fonction de la valeur booléenne
     * @param column colonne
     * @param conditionsColors couleur pour chaque condition
     */
    private void colorColumn(TableColumn<Reservation, String> column, Map<String,Color> conditionsColors) {
        column.setCellFactory(new Callback<>() {
            public TableCell<Reservation, String> call(TableColumn param) {
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

    public Button getBack() { return back; }

    public Button getConfirmArrivalButton() { return confirmArrivalButton; }

    public Button getArchiveButton() { return archiveButton; }

    public Button getMakePaymentButton() { return makePaymentButton; }

    public HBox getRefButtons() { return refButtons; }

    public void setPanelTitle(String value) { title.setText(value); }

    public TableView<Reservation> getReservations() { return reservations; }
}
