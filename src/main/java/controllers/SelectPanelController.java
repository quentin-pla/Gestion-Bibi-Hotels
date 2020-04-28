package controllers;

import views.SelectPanel;

public class SelectPanelController {
    /**
     * Fenêtre de sélection
     */
    private SelectPanel panel;

    /**
     * Constructeur
     */
    public SelectPanelController() {
        panel = new SelectPanel();
        //Initialisation des boutons
        initPanelButtons();
    }

    /**
     * Initialiser les boutons de la fenêtre
     */
    private void initPanelButtons() {
        //Lorsque l'utilisateur clique sur le bouton service client
        panel.getService_client().setOnAction(e -> MainController.getInstance().switchToClientService());
        //Lorsque l'utilisateur clique sur le bouton service réservation
        panel.getService_reservation().setOnAction(e -> MainController.getInstance().switchToReservationService());
        //Lorsque l'utilisateur clique sur le bouton service facturation
        panel.getService_facturation().setOnAction(e -> MainController.getInstance().switchToBillingService());
        //Lorsque l'utilisateur clique sur le bouton administration
        panel.getAdministration().setOnAction(e -> MainController.getInstance().switchToAdministration());
    }

    //************* GETTERS & SETTERS ***************//

    public SelectPanel getPanel() { return panel; }
}
