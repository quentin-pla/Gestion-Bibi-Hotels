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
     * Controleur du panneau de sélection
     */
    private SelectPanelController selectPanelController;

    /**
     * Constructeur
     */
    public MainController() {
        this.selectPanelController = new SelectPanelController();
        //Définition de la fenêtre sur le panneau de connexion
        windowContent = selectPanelController.getSelectPanel();
        //Récupération des données de la base de données
        DatabaseData.getInstance();
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
        return new Scene(windowContent, 1000, 600);
    }
}
