package controllers;

import views.BillingServicePanel;

public class BillingServiceController {
    /**
     * Fenêtre du service facturation
     */
    private BillingServicePanel panel;

    /**
     * Constructeur
     */
    public BillingServiceController() {
        panel = new BillingServicePanel();
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

    public BillingServicePanel getPanel() { return panel; }
}
