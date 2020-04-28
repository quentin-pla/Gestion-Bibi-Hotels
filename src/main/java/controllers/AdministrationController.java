package controllers;

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

    public AdministrationPanel getPanel() { return panel; }
}
