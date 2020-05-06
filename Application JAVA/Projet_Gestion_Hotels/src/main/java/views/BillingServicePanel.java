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
import models.Bill;

import java.util.Map;

/**
 * Fenêtre du service facturation
 */
public class BillingServicePanel extends BorderPane {
    private Button back = new Button("←");
    private Label title = new Label();
    private TableView<Bill> bills = new TableView<>();
    private Button archiveButton = new Button("Archiver");
    private Button calculateButton = new Button("Calculer montant");
    private Button makePaymentButton = new Button("Confirmer paiement");
    private HBox refButtons = new HBox(archiveButton, makePaymentButton, calculateButton);

    /**
     * Constructeur
     */
    public BillingServicePanel() {
        setMinSize(MainController.width, MainController.height);
        back.getStyleClass().add("back");
        title.getStyleClass().add("h3");
        calculateButton.getStyleClass().add("ref-button");
        archiveButton.getStyleClass().addAll("ref-button","red-text");
        makePaymentButton.getStyleClass().addAll("ref-button","green-text");
        initBillsTable();
        BorderPane topContent = new BorderPane();
        topContent.setLeft(back);
        topContent.setCenter(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        refButtons.setVisible(false);
        refButtons.setSpacing(10);
        VBox centerContent = new VBox(bills,refButtons);
        centerContent.setSpacing(20);
        BorderPane.setMargin(centerContent, new Insets(10,50,0,50));
        setTop(topContent);
        setCenter(centerContent);
    }

    /**
     * Initialiser la table contenant les données
     */
    private void initBillsTable() {
        //Taille maximale
        bills.setMinSize(MainController.width-100, MainController.height-140);
        //Interdire la modification
        bills.setEditable(false);
        //Colonne ID
        TableColumn<Bill, String> id_col = new TableColumn<>("ID");
        id_col.setCellValueFactory(
                param -> new SimpleStringProperty(String.valueOf(param.getValue().getID())));
        //Colonne Réservation
        TableColumn<Bill, String> reservation_col = new TableColumn<>("Réservation");
        reservation_col.setCellValueFactory(
                param -> new SimpleStringProperty(String.valueOf(param.getValue().getRESERVATION_ID())));
        //Colonne Client
        TableColumn<Bill, String> client_col = new TableColumn<>("Client");
        client_col.setCellValueFactory(
                param -> new SimpleStringProperty(param.getValue().getClient().getFIRSTNAME() + " "
                        + param.getValue().getClient().getLASTNAME().substring(0,1).toUpperCase()
                        + param.getValue().getClient().getLASTNAME().substring(1).toLowerCase()));
        //Colonne montant
        TableColumn<Bill, String> amount_col = new TableColumn<>("Montant total");
        amount_col.setCellValueFactory(
                param -> {
                    boolean condition = !(param.getValue().getAMOUNT() == 0.0);
                    colorColumn(amount_col, Map.of(param.getValue().getAMOUNT() + "€",Color.BLACK,"À calculer",Color.RED));
                    return new SimpleStringProperty(condition ? param.getValue().getAMOUNT() + "€" : "À calculer");
                });
        //Colonne est payée
        TableColumn<Bill, String> is_payed_col = new TableColumn<>("Paiement");
        is_payed_col.setCellValueFactory(
                param -> {
                    boolean condition = param.getValue().getIS_PAYED();
                    colorColumn(is_payed_col, Map.of("Effectué",Color.GREEN,"En attente",Color.RED));
                    return new SimpleStringProperty(condition ? "Effectué":"En attente");
                });
        //Ajout et dimensionnement des colonnes
        id_col.setMinWidth(10);
        bills.getColumns().add(id_col);
        bills.getColumns().add(reservation_col);
        bills.getColumns().add(client_col);
        bills.getColumns().add(amount_col);
        bills.getColumns().add(is_payed_col);
    }

    /**
     * Colorer une colonne en fonction de la valeur booléenne
     * @param column colonne
     * @param conditionsColors couleur pour chaque condition
     */
    private void colorColumn(TableColumn<Bill, String> column, Map<String,Color> conditionsColors) {
        column.setCellFactory(new Callback<>() {
            public TableCell<Bill, String> call(TableColumn param) {
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

    public Button getArchiveButton() { return archiveButton; }

    public Button getMakePaymentButton() { return makePaymentButton; }

    public Button getCalculateButton() { return calculateButton; }

    public HBox getRefButtons() { return refButtons; }

    public void setPanelTitle(String value) { title.setText(value); }

    public TableView<Bill> getBills() { return bills; }
}
