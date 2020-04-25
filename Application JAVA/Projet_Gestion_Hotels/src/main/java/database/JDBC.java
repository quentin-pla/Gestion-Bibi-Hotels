package database;
import models.*;

import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.ArrayList;

/**
 * Liaison avec la base de données
 */
public class JDBC {
    //URL de connexion
    private static final String CONNECT_URL = "jdbc:mysql://mysql-gestion-hotel.alwaysdata.net/gestion-hotel_projet";
    //Identifiant
    private static final String LOGIN = "***REMOVED***";
    //Mot de passe
    private static final String PASSWORD = "***REMOVED***";
    //Connexion à la base de données
    private Connection connection;

    //Liaison des classes aux modèles
    public static enum Models {
        //Liste des modèles avec en paramètre une nouvelle instance de la classe à laquelle ils sont liés
        CLIENTS(new Client()),
        BILLS(new Bill()),
        HOTELS(new Hotel()),
        OCCUPANTS(new Occupant()),
        RESERVATIONS(new Reservation()),
        OCCUPATIONS(new Occupation()),
        ROOMS(new Room()),
        ROOMTYPES(new RoomType());

        //Modèle
        private DatabaseModel model;

        //Définir un attibut du modèle
        public void setColumn(String column,String value) {
            this.model.setColumn(column,value);
        }

        //Générer une nouvelle instance de la classe liée
        public void createNewInstance() {
            this.model = this.model.newInstance();
        }

        //Récupérer la classe générée
        public DatabaseModel getModel() {
            return this.model;
        }

        //Constructeur
        private Models(DatabaseModel model) {
            this.model = model;
        }
    }

    /**
     * Constructeur JDBC
     */
    private JDBC() {
        try {
            //Connexion à la base de données
            connection = DriverManager.getConnection(CONNECT_URL,LOGIN,PASSWORD);
        } catch (SQLException e) {
            //Message d'erreur
            e.printStackTrace();
        }
    }

    /**
     * Classe responsable de l'instanciation de l'instance unique JDBC
     */
    private static class JDBCHolder {
        private final static JDBC instance = new JDBC();
    }

    /**
     * Instance JDBC
     * @return instance
     */
    private static JDBC getInstance() {
        return JDBCHolder.instance;
    }

    /**
     * Exécuter une requête SQL SELECT
     * @param columns requête
     * @return résultat
     */
    public static ArrayList<DatabaseModel> selectQuery(String columns, Models model) {
        //Liste des résultats
        ArrayList<DatabaseModel> results = new ArrayList<>();
        //Récupération de l'instance
        JDBC jdbc = getInstance();
        try {
            //Creation d'une instruction SQL
            Statement instruction = jdbc.connection.createStatement();
            //Execution de la requête
            ResultSet resultSet = instruction.executeQuery("SELECT " + columns + " FROM " + model.name());
            //Récupération des paramètres de la requête
            ResultSetMetaData columnSet = resultSet.getMetaData();
            //Définition de variables utilisée pour le nom de la colonne et la valeur associée
            String columnName, value;
            //Pour chaque tuple
            while (resultSet.next()) {
                //Pour chaque colonne
                for (int index = 1; index < columnSet.getColumnCount(); index++) {
                    columnName = columnSet.getColumnLabel(index);
                    value = resultSet.getString(columnName);
                    //Définition d'une colonne
                    model.setColumn(columnName, value);
                }
                //Ajout de la classe à la liste des résultats
                results.add(model.getModel());
                //Initialisation
                model.createNewInstance();
            }
            //Fermeture de l'instruction (liberation des ressources)
            instruction.close();
        } catch (SQLException e) {
            //Message d'erreur
            e.printStackTrace();
        }
        //Retour des résultats
        return results;
    }
}