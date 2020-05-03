package database;

import models.Reservation;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Liaison avec la base de données
 */
public class DatabaseConnection {
    /**
     * URL de connexion
     */
    private final String CONNECT_URL = "jdbc:mysql://mysql-gestion-hotel.alwaysdata.net/gestion-hotel_projet";

    /**
     * Identifiant
     */
    private final String LOGIN = "***REMOVED***";

    /**
     * Mot de passe
     */
    private final String PASSWORD = "***REMOVED***";

    /**
     * Connexion à la base de données
     */
    private Connection connection;

    /**
     * Constructeur JDBC
     */
    private DatabaseConnection() {
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
    private static class DatabaseConnectionHolder {
        private final static DatabaseConnection instance = new DatabaseConnection();
    }

    /**
     * Instance JDBC
     * @return instance
     */
    private static DatabaseConnection getInstance() {
        return DatabaseConnectionHolder.instance;
    }

    /**
     * Exécuter une requête SQL SELECT COUNT(*) sur une table
     * @param table table
     * @return nombre d'éléments
     */
    public static int selectCountQuery(DatabaseModel.Tables table) {
        try {
            //Exécution de la requête
            ResultSet rs = getInstance().connection.prepareStatement("SELECT COUNT(*) FROM " + table).executeQuery();
            //Retour du résultat
            return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Retour 0 en cas d'erreur
        return 0;
    }

    /**
     * Exécuter une requête SQL SELECT sur une table en prenant toutes les colonnes
     * @param table table
     * @return classes issues du modèle
     */
    public static ArrayList<DatabaseModel> selectQuery(DatabaseModel.Tables table) {
        return selectQuery(table, "*", "");
    }

    /**
     * Exécuter une requête SQL SELECT sur une table pour récupérer un tuple spécifique
     * @param table table
     * @param ID id de l'élément
     * @return classes issues du modèle
     */
    public static DatabaseModel selectQuery(DatabaseModel.Tables table, int ID) {
        return selectQuery(table, "*", "WHERE ID=" + ID).get(0);
    }

    /**
     * Exécuter une requête SQL SELECT avec condition
     * @param columns colonnes sélectionnées
     * @param table table
     * @return classes issues du modèle
     */
    public static ArrayList<DatabaseModel> selectQuery(DatabaseModel.Tables table, String columns, String where) {
        DatabaseModel model = DatabaseModel.newModelInstance(table);
        //Liste des résultats
        ArrayList<DatabaseModel> results = new ArrayList<>();
        //Récupération de l'instance
        DatabaseConnection databaseConnection = getInstance();
        try {
            //Creation d'une instruction SQL
            Statement instruction = databaseConnection.connection.createStatement();
            //Execution de la requête
            ResultSet resultSet = instruction.executeQuery("SELECT " + columns + " FROM " + table + " " + where);
            //Récupération des paramètres de la requête
            ResultSetMetaData columnSet = resultSet.getMetaData();
            //Définition de variables utilisée pour le nom de la colonne et la valeur associée
            String columnName, value;
            //Pour chaque tuple
            while (resultSet.next()) {
                //Pour chaque colonne
                for (int index = 1; index <= columnSet.getColumnCount(); index++) {
                    //Récupération du nom de la colonne
                    columnName = columnSet.getColumnLabel(index);
                    //Récupération de la valeur de la colonne
                    value = resultSet.getString(columnName);
                    //Définition d'une colonne
                    if (model != null) model.setColumnAttribute(columnName, value);
                }
                //Ajout de la classe à la liste des résultats
                results.add(model);
                //Initialisation
                model = DatabaseModel.newModelInstance(table);
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
     * @param model modèle à insérer
     */
    public static void insertQuery(DatabaseModel model) {
        //Récupération des données de la classe à insérer
        Map<String,String> modelData = model.getAttributesData();
        //Requête SQL
        String query = (
            "INSERT INTO " + model.getTable() +
            " (" + String.join(",",modelData.keySet()) + ")" +
            " VALUES" +
            " ('" + String.join("','",modelData.values()) + "')");
        //Récupération de l'instance
        DatabaseConnection databaseConnection = getInstance();
        try {
            //Creation d'une instruction SQL
            Statement instruction = databaseConnection.connection.createStatement();
            //Execution de la requête
            instruction.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            //Récupération de l'ID généré lors de l'insertion
            ResultSet backId = instruction.getGeneratedKeys();
            //Si un id a été retourné
            if (backId.next())
                //Assignation de l'ID à l'objet
                model.setID(backId.getInt(1));
            //Fermeture de l'instruction (liberation des ressources)
            instruction.close();
        } catch (SQLException e) {
            //Message d'erreur
            System.err.println(e.getMessage());
        }
    }

    /**
     * Modifier un tuple dans la base de données
     * @param model modèle à mettre à jour
     */
    public static void updateQuery(DatabaseModel model, String columns) {
        //Récupération des données de la classe à insérer
        Map<String,String> modelData = model.getAttributesData(columns);
        //Requête SQL
        String query = "UPDATE " + model.getTable() + " SET ";
        //Pour chaque colonne à modifier
        for (Map.Entry<String, String> entry : modelData.entrySet())
            //Concaténation à la requête
            query = query.concat(entry.getKey() + "='" + entry.getValue() + "',");
        //Suppression de la virgule à la fin de la chaine
        query = query.substring(0,query.length()-1);
        //Condition WHERE pour modifier seulement le tuple concerné
        query = query.concat(" WHERE ID=" + model.getID());
        //Récupération de l'instance
        DatabaseConnection databaseConnection = getInstance();
        try {
            //Creation d'une instruction SQL
            Statement instruction = databaseConnection.connection.createStatement();
            //Execution de la requête
            instruction.executeUpdate(query);
            //Fermeture de l'instruction (liberation des ressources)
            instruction.close();
        } catch (SQLException e) {
            //Message d'erreur
            System.err.println(e.getMessage());
        }
    }

    /**
     * Appeler la procédure getAvailableRooms(...)
     * @param reservation réservation
     * @return IDs des chambres disponibles
     */
    public static List<Integer> getAvailableRoomsQuery(Reservation reservation) {
        List<Integer> results = new ArrayList<>();
        //Récupération de l'instance
        DatabaseConnection databaseConnection = getInstance();
        try {
            //Creation d'une instruction SQL
            Statement instruction = databaseConnection.connection.createStatement();
            //Formatter les dates avec une syntaxe spécifique
            SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
            //Execution de la requête
            ResultSet resultSet = instruction.executeQuery(
            "call getAvailableRooms(" + reservation.getHOTEL_ID() + ",'" +
                formater.format(reservation.getARRIVAL_DATE()) + "','" +
                formater.format(reservation.getEXIT_DATE()) + "'," +
                reservation.getROOMTYPE_ID() + ")"
            );
            //Pour chaque tuple
            while (resultSet.next())
                //Récupération de l'ID
                results.add(resultSet.getInt("ID"));
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