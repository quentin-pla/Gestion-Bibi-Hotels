import controllers.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main
 */
public class Main extends Application {

    /**
     * Démarrage de l'application
     * @param primaryStage fenêtre de l'application
     */
    @Override
    public void start(Stage primaryStage) {
        //Titre de la fenêtre
        primaryStage.setTitle("Gestion Hôtels");
        //Fenêtre non redimensionable
        primaryStage.setResizable(false);
        //Définition de la scène avec la scène du MainController
        primaryStage.setScene(MainController.getInstance().getScene());
        //Ajout du style
        primaryStage.getScene().getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        //Affichage de la fenêtre
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}