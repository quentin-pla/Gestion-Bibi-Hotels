package database;

import models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Liaison avec la base de données
 */
public class JDBC {
    //URL de connexion
    private final String CONNECT_URL = "jdbc:mysql://mysql-gestion-hotel.alwaysdata.net/gestion-hotel_projet";
    //Identifiant
    private final String LOGIN = "***REMOVED***";
    //Mot de passe
    private final String PASSWORD = "***REMOVED***";
    //Connexion à la base de données
    private Connection connection;

    //Liaison des classes aux modèles
    public enum Models {
        //Liste des modèles avec en paramètre une nouvelle instance de la classe à laquelle ils sont liés
        BILLS(new Bill()),
        CLIENTS(new Client()),
        HOTELS(new Hotel()),
        OCCUPANTS(new Occupant()),
        OCCUPATIONS(new Occupation()),
        RESERVATIONS(new Reservation()),
        ROOMS(new Room()),
        ROOMTYPES(new RoomType()),
        SERVICES(new Service());

        //Modèle
        private DatabaseModel model;

        //Définir un attibut du modèle
        public void setColumnAttribute(String column, String value) {
            this.model.setColumnAttribute(column,value);
        }

        //Générer une nouvelle instance de la classe liée
        public void createNewInstance() {
            this.model = this.model.newInstance();
        }

        //Récupérer les valeurs pour chaque attribut du modèle
        public Map<String,String> getData() {
            return this.model.getAttributesData();
        }

        //Récupérer la classe générée
        public DatabaseModel getModel() {
            return this.model;
        }

        //Mettre à jour le modèle
        public void setModel(DatabaseModel model) {
            if (model.getClass() == this.model.getClass()) {
                this.model = model;
            }
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
     * Exécuter une requête SQL SELECT simple
     * @param columns colonnes sélectionnées
     * @param model modèle
     * @return classes issues du modèle
     */
    public static ArrayList<DatabaseModel> selectQuery(String columns, Models model) {
        return selectQuery(columns, model, "");
    }

    /**
     * Exécuter une requête SQL SELECT avec condition
     * @param columns colonnes sélectionnées
     * @param model modèle
     * @return classes issues du modèle
     */
    public static ArrayList<DatabaseModel> selectQuery(String columns, Models model, String where) {
        //Liste des résultats
        ArrayList<DatabaseModel> results = new ArrayList<>();
        //Récupération de l'instance
        JDBC jdbc = getInstance();
        try {
            //Creation d'une instruction SQL
            Statement instruction = jdbc.connection.createStatement();
            //Execution de la requête
            ResultSet resultSet = instruction.executeQuery("SELECT " + columns + " FROM " + model.name() + " " + where);
            //Récupération des paramètres de la requête
            ResultSetMetaData columnSet = resultSet.getMetaData();
            //Définition de variables utilisée pour le nom de la colonne et la valeur associée
            String columnName, value;
            //Pour chaque tuple
            while (resultSet.next()) {
                //Pour chaque colonne
                for (int index = 1; index < columnSet.getColumnCount(); index++) {
                    //Récupération du nom de la colonne
                    columnName = columnSet.getColumnLabel(index);
                    //Récupération de la valeur de la colonne
                    value = resultSet.getString(columnName);
                    //Définition d'une colonne
                    model.setColumnAttribute(columnName, value);
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

    /**
     * Insérer un tuple dans la base de données
     * @param model type de modèle
     * @param modelToInsert modèle à insérer
     */
    public static void insertQuery(Models model, DatabaseModel modelToInsert) {
        //Définition du model
        model.setModel(modelToInsert);
        //Récupération des données de la classe à insérer
        Map<String,String> modelData = model.getData();
        //Valeur de l'ID définie sur NULL pour qu'elle soit attribuée automatiquement
        modelData.remove("ID");
        //Requête SQL
        String query = (
            "INSERT INTO " + model.name() +
            " (" + String.join(",",modelData.keySet()) + ")" +
            " VALUES" +
            " ('" + String.join("','",modelData.values()) + "')");
        //Récupération de l'instance
        JDBC jdbc = getInstance();
        try {
            //Creation d'une instruction SQL
            Statement instruction = jdbc.connection.createStatement();
            //Execution de la requête
            instruction.executeUpdate(query);
            //Fermeture de l'instruction (liberation des ressources)
            instruction.close();
        } catch (SQLException e) {
            //Message d'erreur
            System.err.println(e.getMessage());
        }
    }
}