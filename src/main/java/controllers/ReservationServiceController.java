package controllers;

import models.Hotel;
import views.ReservationServicePanel;

public class ReservationServiceController {
    /**
     * Fenêtre du service réservation
     */
    private ReservationServicePanel panel;

    /**
     * Hotel lié
     */
    private Hotel hotel;

    /**
     * Constructeur
     */
    public ReservationServiceController() {
        panel = new ReservationServicePanel();
        //Initialisation des boutons
        initPanel();
    }

    /**
     * Initialiser la fenêtre
     */
    private void initPanel() {
        //Définition de l'action du bouton retour
        panel.getBack().setOnAction(e -> MainController.getInstance().switchToSelect());
    }

    /**
     * //Définition du titre de la fenêtre
     */
    private void setPanelTitle() {
        panel.setPanelTitle("Service Réservation - " + hotel.getHOTEL_NAME());
    }

    //************* GETTERS & SETTERS ***************//

    public ReservationServicePanel getPanel() { return panel; }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        setPanelTitle();
    }
}
