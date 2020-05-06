package controllers;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import models.Bill;
import models.BillingService;
import models.Hotel;
import views.BillingServicePanel;

public class BillingServiceController {
    /**
     * Fenêtre du service facturation
     */
    private BillingServicePanel panel;

    private Hotel hotel;

    /**
     * Constructeur
     */
    public BillingServiceController() {
        panel = new BillingServicePanel();
    }

    /**
     * Initialiser les boutons de la fenêtre
     */
    public BillingServicePanel initPanel() {
        //Récupération de l'hotel sélectionné
        hotel = MainController.getInstance().getSelected_hotel();
        //Initialisation des factures
        BillingService.getInstance(hotel).initBills();
        //Définition du titre de la fenêtre
        panel.setPanelTitle("Liste des factures - Hôtel " + hotel.getHOTEL_NAME());
        //Définition de l'action du bouton retour
        panel.getBack().setOnAction(e -> MainController.getInstance().switchToSelect());
        //Affichage des boutons liés à une facture sélectionnée
        panel.getBills().setOnMouseClicked(e -> refreshPanel());
        //Définition de l'action du bouton de confirmation de l'arrivée
        panel.getArchiveButton().setOnAction(e -> {
            //Récupération de la facture sélectionnée
            Bill bill = panel.getBills().getSelectionModel().getSelectedItem();
            //Si la facture existe
            if (bill != null)
                //Archivage de la facture
                BillingService.getInstance(hotel).archiveBill(bill);
            //Suppression du focus sur la facture
            panel.getBills().getSelectionModel().clearSelection();
            //Suppression de la facture du tableau
            panel.getBills().getItems().remove(bill);
            //Rafraichissement
            refreshPanel();
        });
        //Définition de l'action du bouton de paiement
        panel.getMakePaymentButton().setOnAction(e -> {
            //Récupération de la facture sélctionnée
            Bill bill = panel.getBills().getSelectionModel().getSelectedItem();
            //Si la facture existe
            if (bill != null)
                //Confirmation du paiement
                BillingService.getInstance(hotel).confirmPayment(bill);
            //Rafraichissement
            refreshPanel();
        });
        //Définition de l'action du bouton de calcul du montant total
        panel.getCalculateButton().setOnAction(e -> {
            //Récupération de la facture sélctionnée
            Bill bill = panel.getBills().getSelectionModel().getSelectedItem();
            //Si la facture existe
            if (bill != null)
                //Calcul du montant total
                BillingService.getInstance(hotel).calculateTotalBillAmount(bill);
            //Rafraichissement
            refreshPanel();
        });
        //Récupération des factures
        ObservableList<Bill> items = BillingService.getInstance(hotel).getPending_bills();
        //Ajout d'un listener pour mettre à jour automatiquement le tableau
        items.addListener((ListChangeListener<Bill>) change -> {
            //Définition d'une variable contenant la position de l'élément sélectionné
            int selected = -1;
            //Si un élément est sélectionné
            if (!panel.getBills().getItems().isEmpty() && !panel.getBills().getSelectionModel().isEmpty())
                //Récupération de l'index de l'élément sélectionné
                selected = panel.getBills().getSelectionModel().getFocusedIndex();
            //Ajout des factures à la table
            refreshPanel();
            //Si l'index a été définit
            if (selected != -1)
                //Positionnement sur l'index
                panel.getBills().getSelectionModel().select(selected);
        });
        //Ajout des factures à la table
        panel.getBills().setItems(items);
        //Retour de la fenêtre
        return panel;
    }

    /**
     * Rafraichir le tableau et les boutons
     */
    public void refreshPanel() {
        //Rafraichissement des éléments du tableau
        panel.getBills().refresh();
        //Récupération de la facture sélctionnée
        Bill bill = panel.getBills().getSelectionModel().getSelectedItem();
        //Si la facture existe et que le nombre d'éléments du tableau est supérieur à zéro
        if (bill != null && panel.getBills().getItems().size() > 0) {
            //facture pas archivée
            boolean notArchived = !bill.getIS_ARCHIVED() && bill.getIS_PAYED();
            //montant total facture non calculé
            boolean notCalculated = (bill.getAMOUNT() == 0.0);
            //facture impayée
            boolean notPayed = !bill.getIS_PAYED() && !notCalculated;
            //Définition visibilité bouton confirmation paiement
            panel.getMakePaymentButton().setManaged(notPayed);
            panel.getMakePaymentButton().setVisible(notPayed);
            //Définition visibilité bouton archivage
            panel.getArchiveButton().setManaged(notArchived);
            panel.getArchiveButton().setVisible(notArchived);
            //Définition visibilité bouton archivage
            panel.getCalculateButton().setManaged(notCalculated);
            panel.getCalculateButton().setVisible(notCalculated);
            //Affichage de la liste contenant les boutons
            panel.getRefButtons().setVisible(true);
        }
        //Masquage de la liste contenant les boutons
        else panel.getRefButtons().setVisible(false);
    }
}
