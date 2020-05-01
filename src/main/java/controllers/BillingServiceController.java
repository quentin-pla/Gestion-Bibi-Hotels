package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Bill;
import models.BillingService;
import views.BillingServicePanel;

public class BillingServiceController {
    /**
     * Fenêtre du service facturation
     */
    private BillingServicePanel panel;

    /**
     * Hotel lié
     */
    private BillingService billingService;

    /**
     * Constructeur
     */
    public BillingServiceController() {
        panel = new BillingServicePanel();
    }

    /**
     * Initialiser les boutons de la fenêtre
     */
    private void initPanel() {
        //Définition du titre de la fenêtre
        panel.setPanelTitle("Service Facturation - " + billingService.getHotel().getHOTEL_NAME());
        //Définition de l'action du bouton retour
        panel.getBack().setOnAction(e -> MainController.getInstance().switchToSelect());
        //Affichage des boutons liés à une facture sélectionnée
        panel.getBills().setOnMouseClicked(e -> refreshTableItems());
        //Définition de l'action du bouton de confirmation de l'arrivée
        panel.getArchive().setOnAction(e -> {
            //Récupération de la facture sélectionnée
            Bill bill = panel.getBills().getSelectionModel().getSelectedItem();
            //Si la facture existe
            if (bill != null)
                //Archivage de la facture
                billingService.archiveBill(bill);
            //Suppression du focus sur la facture
            panel.getBills().getSelectionModel().clearSelection();
            //Suppression de la facture du tableau
            panel.getBills().getItems().remove(bill);
            //Rafraichissement
            refreshTableItems();
        });
        //Définition de l'action du bouton de paiement
        panel.getMakePayment().setOnAction(e -> {
            //Récupération de la facture sélctionnée
            Bill bill = panel.getBills().getSelectionModel().getSelectedItem();
            //Si la facture existe
            if (bill != null)
                //Confirmation du paiement
                billingService.confirmPayment(bill);
            //Rafraichissement
            refreshTableItems();
        });
        //Définition de l'action du bouton de calcul du montant total
        panel.getCalculate().setOnAction(e -> {
            //Récupération de la facture sélctionnée
            Bill bill = panel.getBills().getSelectionModel().getSelectedItem();
            //Si la facture existe
            if (bill != null)
                //Calcul du montant total
                billingService.calculateTotalBillAmount(bill);
            //Rafraichissement
            refreshTableItems();
        });
        //Liste des factures
        ObservableList<Bill> items = FXCollections.observableArrayList(billingService.getPending_bills());
        //Ajout des factures à la table
        panel.getBills().setItems(items);
    }

    /**
     * Rafraichir les éléments du tableau
     */
    public void refreshTableItems() {
        //Rafraichissement des éléments du tableau
        panel.getBills().refresh();
        //Récupération de la facture sélctionnée
        Bill bill = panel.getBills().getSelectionModel().getSelectedItem();
        //Si la facture existe
        if (bill != null) {
            //facture pas archivée
            boolean notArchived = !bill.getIS_ARCHIVED() && bill.getIS_PAYED();
            //montant total facture non calculé
            boolean notCalculated = (bill.getAMOUNT() == 0.0);
            //facture impayée
            boolean notPayed = !bill.getIS_PAYED() && !notCalculated;
            //Définition visibilité bouton confirmation paiement
            panel.getMakePayment().setManaged(notPayed);
            panel.getMakePayment().setVisible(notPayed);
            //Définition visibilité bouton archivage
            panel.getArchive().setManaged(notArchived);
            panel.getArchive().setVisible(notArchived);
            //Définition visibilité bouton archivage
            panel.getCalculate().setManaged(notCalculated);
            panel.getCalculate().setVisible(notCalculated);
            //Affichage de la liste contenant les boutons
            panel.getRefButtons().setVisible(true);
        }
        //Masquage de la liste contenant les boutons
        else panel.getRefButtons().setVisible(false);
    }

    //************* GETTERS & SETTERS ***************//

    public BillingServicePanel getPanel() { return panel; }

    public BillingService getBillingService() {
        return billingService;
    }

    public void setBillingService(BillingService billingService) {
        this.billingService = billingService;
        initPanel();
    }
}
