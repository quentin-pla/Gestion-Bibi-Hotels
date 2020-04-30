package controllers;

import javafx.collections.FXCollections;
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
    private void initPanel() {
        //Définition du titre de la fenêtre
        panel.setPanelTitle("Service Réservation - " + hotel.getHOTEL_NAME());
        //Définition de l'action du bouton retour
        panel.getBack().setOnAction(e -> MainController.getInstance().switchToSelect());
        //Affichage des boutons liés à une réservation sélectionnée
        panel.getReservations().setOnMouseClicked(e -> refreshTableItems());
        //Définition de l'action du bouton de confirmation de l'arrivée
        panel.getArchive().setOnAction(e -> {
            //Récupération de la réservation sélctionnée
            Reservation reservation = panel.getReservations().getSelectionModel().getSelectedItem();
            //Si la réservation existe
            if (reservation != null)
                //Archivage de la réservation
                hotel.getReservationService().archiveReservation(reservation);
            //Suppression du focus sur la réservation
            panel.getReservations().getSelectionModel().clearSelection();
            //Suppression de la réservation du tableau
            panel.getReservations().getItems().remove(reservation);
            //Rafraichissement
            refreshTableItems();
        });
        //Définition de l'action du bouton de confirmation de l'arrivée du client
        panel.getConfirmArrival().setOnAction(e -> {
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
        panel.getMakePayment().setOnAction(e -> {
            //Récupération de la réservation sélctionnée
            Reservation reservation = panel.getReservations().getSelectionModel().getSelectedItem();
            //Si la réservation existe
            if (reservation != null)
                //Confirmation du paiement des arrhes
                hotel.getReservationService().confirmPayment(reservation);
            //Rafraichissement
            refreshTableItems();
        });
        //Liste des réservations
        ObservableList<Reservation> items = FXCollections.observableArrayList(hotel.getReservationService().getReservations());
        //Ajout des réservations à la table
        panel.getReservations().setItems(items);
    }

    public void refreshTableItems() {
        //Rafraichissement des éléments du tableau
        panel.getReservations().refresh();
        //Récupération de la réservation sélctionnée
        Reservation reservation = panel.getReservations().getSelectionModel().getSelectedItem();
        //Si la réservation existe
        if (reservation != null) {
            //Réservation non confirmée
            boolean notConfirmed = !reservation.getIS_COMFIRMED() && reservation.getIS_PAYED();
            //Réservation impayée
            boolean notPayed = !reservation.getIS_PAYED();
            //Réservation pas archivée
            boolean notArchived = !reservation.getIS_ARCHIVED() && reservation.getIS_COMFIRMED() && reservation.getIS_PAYED();
            //Définition visibilité bouton confirmation arrivée
            panel.getConfirmArrival().setManaged(notConfirmed);
            panel.getConfirmArrival().setVisible(notConfirmed);
            //Définition visibilité bouton confirmation paiement
            panel.getMakePayment().setManaged(notPayed);
            panel.getMakePayment().setVisible(notPayed);
            //Définition visibilité bouton archivage
            panel.getArchive().setManaged(notArchived);
            panel.getArchive().setVisible(notArchived);
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
            ArrayList<Occupation> occupations = ClientService.getInstance().getReservationOccupations(reservation.getID());
            //Pour chaque occupation
            for (Occupation occupation : occupations)
                //Pour chaque occupant
                for (String[] occupantData : confirmArrivalPanel.retrieveFieldsData()) {
                    //Ajout de l'occupant dans l'occupation si possible
                    if (!occupation.addOccupant(new Occupant(occupation.getID(), occupantData[0], occupantData[1])))
                        break;
                }
            //Confirmation de l'arrivée du client dans l'hotel
            hotel.getReservationService().confirmArrival(reservation);
            //Rafraichissement des éléments dans la table
            refreshTableItems();
            //Affichage de la liste des réservations
            MainController.getInstance().setWindow(panel);
        });
    }

    //************* GETTERS & SETTERS ***************//

    public ReservationServicePanel getPanel() { return panel; }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        initPanel();
    }
}
