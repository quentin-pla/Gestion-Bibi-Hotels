import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        //Titre de la fenêtre
        primaryStage.setTitle("Gestion Hôtels");
        //Fenêtre non redimensionable
        primaryStage.setResizable(false);
        //Définition de la scène
        primaryStage.setScene(new Scene(new Pane()));
        //Ajout du style
        primaryStage.getScene().getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        //Affichage de la fenêtre
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}