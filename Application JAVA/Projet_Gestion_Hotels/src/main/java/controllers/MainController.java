package controllers;

import database.DatabaseData;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class MainController {
    /**
     * Fenêtre affichée à l'écran
     */
    private Pane windowContent;

    /**
     * Hauteur de la fenêtre
     */
    public static final int height = 600;

    /**
     * Largeur de la fenêtre
     */
    public static final int width = 1000;

    /**
     * Controleur du panneau de sélection
     */
    private SelectPanelController selectPanelController;

    /**
     * Controleur du panneau de sélection
     */
    private AdministrationController administrationController;

    /**
     * Controleur du panneau de sélection
     */
    private ClientServiceController clientServiceController;

    /**
     * Controleur du panneau de sélection
     */
    private ReservationServiceController reservationServiceController;

    /**
     * Controleur du panneau de sélection
     */
    private BillingServiceController billingServiceController;

    /**
     * Constructeur
     */
    public MainController() {
        //Initialisation des controleurs
        initControllers();
        //Initialisation de la fenêtre
        windowContent = new Pane();
        //Définition de la fenêtre sur le panneau de sélection
        switchToSelectPanel();
        //Récupération des données de la base de données de manière asynchrone
        new Thread(DatabaseData::getInstance).start();
    }

    /**
     * Initialisation des controleurs
     */
    private void initControllers() {
        this.selectPanelController = new SelectPanelController();
        this.administrationController = new AdministrationController();
        this.clientServiceController = new ClientServiceController();
        this.reservationServiceController = new ReservationServiceController();
        this.billingServiceController = new BillingServiceController();
    }

    /**
     * Récupérer l'instance unique
     */
    public static MainController getInstance() {
        //Si l'instance n'est pas initialisée
        if (instance == null)
            //Initialisation de l'instance
            instance = new MainController();
        //Retour de l'instance
        return instance;
    }

    /**
     * Instance unique
     */
    private static MainController instance = null;

    /**
     * Changement de fenêtre sur la sélection
     */
    public void switchToSelectPanel() { setWindow(selectPanelController.getPanel()); }

    /**
     * Changement de fenêtre sur le panneau d'administration
     */
    public void switchToAdministration() {
        setWindow(administrationController.getPanel());
    }

    /**
     * Changement de fenêtre sur le panneau d'administration
     */
    public void switchToClientService() {
        setWindow(clientServiceController.getPanel());
    }

    /**
     * Changement de fenêtre sur le panneau d'administration
     */
    public void switchToReservationService() {
        setWindow(reservationServiceController.getPanel());
    }

    /**
     * Changement de fenêtre sur le panneau d'administration
     */
    public void switchToBillingService() {
        setWindow(billingServiceController.getPanel());
    }

    /**
     * Afficher une fenêtre à l'écran
     * @param window fenêtre
     */
    private void setWindow(BorderPane window) {
        //Suppression des enfants de la fenêtre globale
        windowContent.getChildren().clear();
        //Ajout de la fenêtre dans la fenêtre globale
        windowContent.getChildren().add(window);
    }

    /**
     * Retour de la fenêtre affichée
     * @return fenêtre
     */
    public Scene getScene() {
        //Retour de la scène
        return new Scene(windowContent, width, height);
    }
}
