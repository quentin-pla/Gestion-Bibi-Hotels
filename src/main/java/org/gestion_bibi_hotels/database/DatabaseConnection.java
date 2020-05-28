package org.gestion_bibi_hotels.database;

import org.gestion_bibi_hotels.models.Bill;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Connexion avec la base de données
 */
public class DatabaseConnection {
    /**
     * URL de connexion
     */
    private final String CONNECT_URL = "jdbc:mysql://hote_mysql/base_de_donnees";

    /**
     * Identifiant
     */
    private final String LOGIN = "identifiant";

    /**
     * Mot de passe
     */
    private final String PASSWORD = "mot_de_passe";

    /**
     * Connexion
     */
    private Connection connection;

    /**
     * Constructeur
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
     * Classe responsable de l'instanciation de l'instance unique
     */
    private static class DatabaseConnectionHolder {
        /**
         * Instance unique
         */
        private final static DatabaseConnection instance = new DatabaseConnection();
    }

    /**
     * Récupérer l'instance
     * @return instance
     */
    private static DatabaseConnection getInstance() {
        return DatabaseConnectionHolder.instance;
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
        ArrayList<DatabaseModel> result = selectQuery(table, "*", "WHERE ID=" + ID);
        return !result.isEmpty() ? result.get(0) : null;
    }

    /**
     * Exécuter une requête SQL SELECT avec condition
     * @param columns colonnes sélectionnées
     * @param table table
     * @return classes issues du modèle
     */
    public static ArrayList<DatabaseModel> selectQuery(DatabaseModel.Tables table, String columns, String where) {
        //Récupération d'une nouvelle instance du modèle
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
                //Initialisation d'une nouvelle instance
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
     * Supprimer un tuple de la base de données
     * @param model modèle
     */
    public static void deleteQuery(DatabaseModel model) {
        //Requête SQL
        String query = "DELETE FROM " + model.getTable() + " WHERE ID=" + model.getID();
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
     * Mettre à jour un tuple dans la base de données
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
        query = query.concat(" WHERE ID=" + model.getID());//Récupération de l'instance
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
     * Récupérer le montant total d'une facture
     * @param bill facture
     */
    public static double retrieveTotalAmount(Bill bill) {
        String query = "CALL getTotalAmount(" + bill.getRESERVATION_ID() + ")";
        //Récupération de l'instance
        DatabaseConnection databaseConnection = getInstance();
        //Variable contenant le résultat
        double result = 0.0;
        try {
            //Creation d'une instruction SQL
            Statement instruction = databaseConnection.connection.createStatement();
            //Execution de la requête
            ResultSet resultSet = instruction.executeQuery(query);
            //Récupération du résultat
            resultSet.next();
            //Mise à jour du résultat
            result = resultSet.getDouble("total");
            //Fermeture de l'instruction (liberation des ressources)
            instruction.close();
        } catch (SQLException e) {
            //Message d'erreur
            System.err.println(e.getMessage());
        }
        //Retour du résultat
        return result;
    }

    /**
     * Récupérer la promotion pour les clients réguliers
     * @return montant
     */
    public static int retrieveRegularClientDiscount() {
        String query = "SELECT getRegularClientDiscount()";
        //Récupération de l'instance
        DatabaseConnection databaseConnection = getInstance();
        //Variable contenant le résultat
        int result = 0;
        try {
            //Creation d'une instruction SQL
            Statement instruction = databaseConnection.connection.createStatement();
            //Execution de la requête
            ResultSet resultSet = instruction.executeQuery(query);
            //Récupération du résultat
            resultSet.next();
            //Mise à jour du résultat
            result = resultSet.getInt("getRegularClientDiscount()");
            //Fermeture de l'instruction (liberation des ressources)
            instruction.close();
        } catch (SQLException e) {
            //Message d'erreur
            System.err.println(e.getMessage());
        }
        //Retour du résultat
        return result;
    }

    /**
     * Récupérer la promotion pour les groupes
     * @return montant
     */
    public static int retrieveGroupDiscount() {
        String query = "SELECT getGroupDiscount()";
        //Récupération de l'instance
        DatabaseConnection databaseConnection = getInstance();
        //Variable contenant le résultat
        int result = 0;
        try {
            //Creation d'une instruction SQL
            Statement instruction = databaseConnection.connection.createStatement();
            //Execution de la requête
            ResultSet resultSet = instruction.executeQuery(query);
            //Récupération du résultat
            resultSet.next();
            //Mise à jour du résultat
            result = resultSet.getInt("getGroupDiscount()");
            //Fermeture de l'instruction (liberation des ressources)
            instruction.close();
        } catch (SQLException e) {
            //Message d'erreur
            System.err.println(e.getMessage());
        }
        //Retour du résultat
        return result;
    }
}
