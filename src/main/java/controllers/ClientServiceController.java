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
        initPanel();
    }

    /**
     * Initialiser les boutons de la fenêtre
     */
    private void initPanel() {
        panel.getBack().setOnAction(e -> MainController.getInstance().switchToSelect());
    }

    //************* GETTERS & SETTERS ***************//

    public ClientServicePanel getPanel() { return panel; }
}
