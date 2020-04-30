package controllers;

import models.Hotel;
import views.BillingServicePanel;

public class BillingServiceController {
    /**
     * Fenêtre du service facturation
     */
    private BillingServicePanel panel;

    /**
     * Hotel lié
     */
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
    private void initPanel() {
        //Définition du titre de la fenêtre
        panel.setPanelTitle("Service Facturation - " + hotel.getHOTEL_NAME());
        //Définition de l'action du bouton retour
        panel.getBack().setOnAction(e -> MainController.getInstance().switchToSelect());
    }

    //************* GETTERS & SETTERS ***************//

    public BillingServicePanel getPanel() { return panel; }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        initPanel();
    }
}
