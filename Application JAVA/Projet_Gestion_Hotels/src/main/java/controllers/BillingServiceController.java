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
        //Initialisation des boutons
        initPanel();
    }

    /**
     * Initialiser les boutons de la fenêtre
     */
    private void initPanel() {
        //Définition de l'action du bouton retour
        panel.getBack().setOnAction(e -> MainController.getInstance().switchToSelect());
    }

    /**
     * //Définition du titre de la fenêtre
     */
    private void setPanelTitle() {
        panel.setPanelTitle("Service Facturation - " + hotel.getHOTEL_NAME());
    }

    //************* GETTERS & SETTERS ***************//

    public BillingServicePanel getPanel() { return panel; }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        setPanelTitle();
    }
}
