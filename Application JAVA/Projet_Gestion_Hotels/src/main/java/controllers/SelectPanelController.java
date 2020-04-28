package controllers;

import views.SelectPanel;

public class SelectPanelController {
    /**
     * Fenêtre de sélection
     */
    private SelectPanel selectPanel;

    /**
     * Constructeur
     */
    public SelectPanelController() {
        selectPanel = new SelectPanel();
        //Initialisation des boutons
        initPanelButtons();
    }

    /**
     * Initialiser les boutons de la fenêtre
     */
    private void initPanelButtons() {
        //Lorsque l'utilisateur clique sur le bouton service client
        selectPanel.getService_client().setOnAction(e -> {
            System.out.println("Service client");
        });
        //Lorsque l'utilisateur clique sur le bouton service réservation
        selectPanel.getService_reservation().setOnAction(e -> {
            System.out.println("Service réservation");
        });
        //Lorsque l'utilisateur clique sur le bouton service facturation
        selectPanel.getService_facturation().setOnAction(e -> {
            System.out.println("Service facturation");
        });
        //Lorsque l'utilisateur clique sur le bouton administration
        selectPanel.getAdministration().setOnAction(e -> {
            System.out.println("Administration");
        });
    }

    //************* GETTERS & SETTERS ***************//

    public SelectPanel getSelectPanel() { return selectPanel; }
}
