package controllers;

import database.DatabaseData;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import models.Hotel;

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
    private SelectController selectController;

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
     * Hotel sélectionné
     */
    private Hotel selected_hotel;

    /**
     * Service sélectionné
     */
    private ServicePanel selected_service;

    /**
     * Liste des services disponibles
     */
    public enum ServicePanel {
        RESERVATION,BILLING,CLIENT,ADMINISTRATION
    }

    /**
     * Constructeur
     */
    public MainController() {
        //Initialisation des controleurs
        initControllers();
        //Initialisation de la fenêtre
        windowContent = new Pane();
        //Définition de la fenêtre sur le panneau de sélection
        switchToSelect();
    }

    /**
     * Initialisation des controleurs
     */
    private void initControllers() {
        this.selectController = new SelectController();
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
        if (instance == null) {
            //Initialisation de l'instance
            instance = new MainController();
            //Récupération des données de la base de données
            DatabaseData.getInstance();
        }
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
    public void switchToSelect() {
        //Affichage du panneau de sélection
        setWindow(selectController.initPanel());
    }

    /**
     * Changement de fenêtre sur le panneau d'administration
     */
    public void switchToAdministration() {
        //Service sélectionné Administration
        selected_service = ServicePanel.ADMINISTRATION;
        //Affichage du panneau d'administration
        setWindow(administrationController.getPanel());
    }

    /**
     * Changement de fenêtre sur le service client
     */
    public void switchToClientService() {
        //Service sélectionné Client
        selected_service = ServicePanel.CLIENT;
        //Affichage du service client
        setWindow(clientServiceController.initPanel());
    }

    /**
     * Changement de fenêtre sur le service réservation
     */
    public void switchToReservationService() {
        //Service sélectionné Réservation
        selected_service = ServicePanel.RESERVATION;
        //Affichage du service réservation
        setWindow(reservationServiceController.initPanel());
    }

    /**
     * Changement de fenêtre sur le service facturation
     */
    public void switchToBillingService() {
        //Service sélectionné Facturation
        selected_service = ServicePanel.BILLING;
        //Affichage de la fenêtre
        setWindow(billingServiceController.initPanel());
    }

    /**
     * Afficher une fenêtre à l'écran
     * @param window fenêtre
     */
    public void setWindow(BorderPane window) {
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
        return new Scene(windowContent, width, height, Color.BLUE);
    }

    public Hotel getSelected_hotel() {
        return selected_hotel;
    }

    public void setSelected_hotel(Hotel selected_hotel) {
        this.selected_hotel = selected_hotel;
    }

    public ServicePanel getSelected_service() {
        return selected_service;
    }
}
