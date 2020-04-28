package controllers;

import views.ReservationServicePanel;

public class ReservationServiceController {
    /**
     * Fenêtre du service réservation
     */
    private ReservationServicePanel panel;

    /**
     * Constructeur
     */
    public ReservationServiceController() {
        panel = new ReservationServicePanel();
        //Initialisation des boutons
        initPanelButtons();
    }

    /**
     * Initialiser les boutons de la fenêtre
     */
    private void initPanelButtons() {
        panel.getBack().setOnAction(e -> MainController.getInstance().switchToSelectPanel());
    }

    //************* GETTERS & SETTERS ***************//

    public ReservationServicePanel getPanel() { return panel; }
}
