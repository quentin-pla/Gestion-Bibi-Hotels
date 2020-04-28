package controllers;

import views.ClientServicePanel;

public class ClientServiceController {
    /**
     * Fenêtre du service client
     */
    private ClientServicePanel panel;

    /**
     * Constructeur
     */
    public ClientServiceController() {
        panel = new ClientServicePanel();
        //Initialisation des boutons
        initPanelButtons();
    }

    /**
     * Initialiser les boutons de la fenêtre
     */
    private void initPanelButtons() {
        //
    }

    //************* GETTERS & SETTERS ***************//

    public ClientServicePanel getPanel() { return panel; }
}
