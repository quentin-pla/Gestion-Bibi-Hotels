package org.gestion_bibi_hotels;

import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.gestion_bibi_hotels.controllers.MainController;

/**
 * Main
 */
public class Application extends javafx.application.Application {

    /**
     * Démarrage de l'application
     * @param primaryStage fenêtre de l'application
     */
    @Override
    public void start(Stage primaryStage) {
        //Titre de la fenêtre
        primaryStage.setTitle("Gestion Bibi hôtels");
        //Fenêtre non redimensionable
        primaryStage.setResizable(false);
        //Définition de la scène avec la scène du MainController
        primaryStage.setScene(MainController.getInstance().getScene());
        //Ajout du style
        primaryStage.getScene().getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        //Initialisation de la police Avenir
        Font.loadFont(getClass().getResource("Avenir.otf").toExternalForm(),13);
        //Affichage de la fenêtre
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}