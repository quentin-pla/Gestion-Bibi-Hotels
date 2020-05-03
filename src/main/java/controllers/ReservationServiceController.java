package controllers;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import models.*;
import views.ConfirmArrivalPanel;
import views.ReservationServicePanel;

import java.util.ArrayList;

public class ReservationServiceController {
    /**
     * Fenêtre du service réservation
     */
    private ReservationServicePanel panel;

    /**
     * Fenêtre de confirmation de l'arrivée d'un client
     */
    private ConfirmArrivalPanel confirmArrivalPanel;

    /**
     * Hotel lié
     */
    private Hotel hotel;

    /**
     * Constructeur
     */
    public ReservationServiceController() {
        panel = new ReservationServicePanel();
        confirmArrivalPanel = new ConfirmArrivalPanel();
    }

    /**
     * Initialiser la fenêtre
     */
    public ReservationServicePanel initPanel() {
        //Récupération de l'hotel sélectionné
        hotel = MainController.getInstance().getSelected_hotel();
        //Définition du titre de la fenêtre
        panel.setPanelTitle("Service Réservation - " + hotel.getHOTEL_NAME());
        //Définition de l'action du bouton retour
        panel.getBack().setOnAction(e -> MainController.getInstance().switchToSelect());
        //Affichage des boutons liés à une réservation sélectionnée
        panel.getReservations().setOnMouseClicked(e -> refreshPanel());
        //Définition de l'action du bouton de confirmation de l'arrivée
        panel.getArchiveButton().setOnAction(e -> {
            //Récupération de la réservation sélctionnée
            Reservation reservation = panel.getReservations().getSelectionModel().getSelectedItem();
            //Si la réservation existe
            if (reservation != null)
                //Archivage de la réservation
                ReservationService.getInstance(hotel).archiveReservation(reservation);
            //Suppression du focus sur la réservation
            panel.getReservations().getSelectionModel().clearSelection();
            //Suppression de la réservation du tableau
            panel.getReservations().getItems().remove(reservation);
            //Rafraichissement
            refreshPanel();
        });
        //Définition de l'action du bouton de confirmation de l'arrivée du client
        panel.getConfirmArrivalButton().setOnAction(e -> {
            //Récupération de la réservation sélctionnée
            Reservation reservation = panel.getReservations().getSelectionModel().getSelectedItem();
            //Si la réservation existe
            if (reservation != null) {
                //Initialisation de la fenêtre de confirmation d'arrivée
                initConfirmArrivalPanel(reservation);
                //Affichage de la fenêtre
                MainController.getInstance().setWindow(confirmArrivalPanel);
            }
        });
        //Définition de l'action du bouton de paiement des arrhes
        panel.getMakePaymentButton().setOnAction(e -> {
            //Récupération de la réservation sélctionnée
            Reservation reservation = panel.getReservations().getSelectionModel().getSelectedItem();
            //Si la réservation existe
            if (reservation != null)
                //Confirmation du paiement des arrhes
                ReservationService.getInstance(hotel).confirmPayment(reservation);
            //Rafraichissement
            refreshPanel();
        });
        //Récupération des réservations
        ObservableList<Reservation> items = ReservationService.getInstance(hotel).getReservations();
        //Ajout d'un listener pour mettre à jour automatiquement le tableau
        items.addListener((ListChangeListener<Reservation>) change -> {
            //Définition d'une variable contenant la position de l'élément sélectionné
            int selected = -1;
            //Si un élément est sélectionné
            if (!panel.getReservations().getSelectionModel().isEmpty())
                //Récupération de l'index de l'élément sélectionné
                selected = panel.getReservations().getSelectionModel().getFocusedIndex();
            //Ajout des réservations à la table
            refreshPanel();
            //Si l'index a été définit
            if (selected != -1)
                //Positionnement sur l'index
                panel.getReservations().getSelectionModel().select(selected);
        });
        //Ajout des réservations à la table
        panel.getReservations().getItems().setAll(items);
        //Retour de la fenêtre
        return panel;
    }

    /**
     * Rafraichir les éléments du tableau et les boutons
     */
    public void refreshPanel() {
        //Rafraichissement des éléments du tableau
        panel.getReservations().refresh();
        //Récupération de la réservation sélctionnée
        Reservation reservation = panel.getReservations().getSelectionModel().getSelectedItem();
        //Si la réservation existe et que le nombre d'éléments du tableau est supérieur à zéro
        if (reservation != null && panel.getReservations().getItems().size() > 0) {
            //Réservation non confirmée
            boolean notConfirmed = !reservation.getIS_COMFIRMED() && reservation.getIS_PAYED();
            //Réservation impayée
            boolean notPayed = !reservation.getIS_PAYED();
            //Réservation pas archivée
            boolean notArchived = !reservation.getIS_ARCHIVED() && reservation.getIS_COMFIRMED() && reservation.getIS_PAYED();
            //Définition visibilité bouton confirmation arrivée
            panel.getConfirmArrivalButton().setManaged(notConfirmed);
            panel.getConfirmArrivalButton().setVisible(notConfirmed);
            //Définition visibilité bouton confirmation paiement
            panel.getMakePaymentButton().setManaged(notPayed);
            panel.getMakePaymentButton().setVisible(notPayed);
            //Définition visibilité bouton archivage
            panel.getArchiveButton().setManaged(notArchived);
            panel.getArchiveButton().setVisible(notArchived);
            //Affichage de la liste contenant les boutons
            panel.getRefButtons().setVisible(true);
        }
        //Masquage de la liste contenant les boutons
        else panel.getRefButtons().setVisible(false);
    }

    public void initConfirmArrivalPanel(Reservation reservation) {
        //Définition du titre de la fenêtre
        confirmArrivalPanel.setPanelTitle("Confirmation arrivée - Réservation n°" + reservation.getID());
        //Définition de l'action du bouton retour
        confirmArrivalPanel.getBack().setOnAction(e -> MainController.getInstance().setWindow(panel));
        //Initialisation des champs de la fenêtre
        confirmArrivalPanel.initFields(reservation.getPEOPLE_COUNT(),reservation.getClient().getFIRSTNAME(),reservation.getClient().getLASTNAME());
        //Définition de l'action du bouton valider
        confirmArrivalPanel.getValidate().setOnAction(e -> {
            //Récupération des occupations liées à la réservation
            ArrayList<Occupation> occupations = ReservationService.getInstance(hotel).getReservationOccupations(reservation.getID());
            //Pour chaque occupation
            for (Occupation occupation : occupations)
                //Pour chaque occupant
                for (String[] occupantData : confirmArrivalPanel.retrieveFieldsData()) {
                    //Ajout de l'occupant dans l'occupation si possible
                    if (!occupation.addOccupant(new Occupant(occupation.getID(), occupantData[0], occupantData[1])))
                        break;
                }
            //Confirmation de l'arrivée du client dans l'hotel
            ReservationService.getInstance(hotel).confirmArrival(reservation);
            //Rafraichissement des éléments dans la table
            refreshPanel();
            //Affichage de la liste des réservations
            MainController.getInstance().setWindow(panel);
        });
    }
}
