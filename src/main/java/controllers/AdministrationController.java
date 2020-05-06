package controllers;

import javafx.scene.layout.BorderPane;
import models.Administration;
import views.AdministrationPanel;

public class AdministrationController {
    /**
     * Fenêtre d'administration
     */
    private AdministrationPanel panel;

    /**
     * Constructeur
     */
    public AdministrationController() {
        panel = new AdministrationPanel();
    }

    /**
     * Initialiser la fenêtre
     */
    public BorderPane initPanel() {
        //Initialisation des hotels
        Administration.getInstance().initHotels();
        //Définition du bouton de retour
        panel.getBack().setOnAction(e -> MainController.getInstance().switchToSelect());
        //Initialisation des ratios pour chaque type de chambre
        panel.initRoomTypesRatios(Administration.getInstance().getAvailableBilledRoomRatio());
        //Initialisation des bénéfices effectués par chaque hotel
        panel.initTotalBilledAmounts(Administration.getInstance().getBilledAmounts());
        //Initialisation du nombre total de services facturés
        panel.initBilledServicesAmounts(Administration.getInstance().getBilledServicesAmount());
        //Retour de la fenêtre
        return panel;
    }
}
