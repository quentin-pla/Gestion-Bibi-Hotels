package database;

import models.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static database.DatabaseConnection.insertQuery;
import static database.DatabaseConnection.updateQuery;

public abstract class DatabaseModel {
    /**
     * ID
     */
    protected int ID;

    /**
     * Table correspondante dans la base de données
     */
    protected Tables table;

    /**
     * Liste des tables de la base de données
     */
    public enum Tables {
        BILLS,CLIENTS,HOTELS,OCCUPANTS,OCCUPATIONS,RESERVATIONS,ROOMS,ROOMTYPES,SERVICES
    }

    /**
     * Liste des colonnes du modèle
     * @return colonnes
     */
    public abstract DatabaseColumns[] getColumns();

    /**
     * Générer une nouvelle instance
     * @param table table
     * @return nouvelle instance
     */
    public static DatabaseModel newModelInstance(Tables table) {
        switch (table) {
            case BILLS:         return new Bill();
            case CLIENTS:       return new Client();
            case HOTELS:        return new Hotel();
            case OCCUPANTS:     return new Occupant();
            case OCCUPATIONS:   return new Occupation();
            case RESERVATIONS:  return new Reservation();
            case ROOMS:         return new Room();
            case ROOMTYPES:     return new RoomType();
            case SERVICES:      return new Service();
            default:            return null;
        }
    }

    /**
     * Constructeur
     * @param table table liée
     */
    protected DatabaseModel(Tables table) {
        this.table = table;
    }

    /**
     * Sauvegarder l'objet dans la base de données
     */
    public void save() {
        insertQuery(this);
    }

    /**
     * Mettre à jour une colonne dans la base de données
     * @param column colonne
     */
    public void updateColumn(DatabaseColumns column) {
        updateQuery(this, column.toString());
    }

    /**
     * Définir un attribut à partir de la valeur d'une colonne de la base de données
     * @param columnItem nom de la colonne
     * @param value valeur à attribuer
     */
    public void setColumnAttribute(String columnItem, String value) {
        Class<?> parameterType = getColumnType(columnItem);
        try {
            Method setColumn = getClass().getMethod("set" + columnItem, parameterType);
            switch (parameterType.getSimpleName()) {
                case "boolean":
                    setColumn.invoke(this, Boolean.parseBoolean(parseBooleanFromString(value)));
                    break;
                case "int":
                    setColumn.invoke(this, Integer.parseInt(value));
                    break;
                case "double":
                    setColumn.invoke(this, Double.parseDouble(value));
                    break;
                case "String":
                    setColumn.invoke(this, value);
                    break;
                case "Date":
                    setColumn.invoke(this, new SimpleDateFormat("yyyy-MM-dd").parse(value));
                    break;
                default:
                    break;
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupérer toutes la valeur de chaque colonne
     * @return valeur de chaque colonne
     */
    public Map<String,String> getAttributesData() {
        return getAttributesData(null);
    }

    /**
     * Récupérer les valeurs de colonnes spécifiques
     * @param columnsFilter colonnes sélectionnées
     * @return valeur de chaque colonne
     */
    public Map<String,String> getAttributesData(String columnsFilter) {
        //Résultats
        Map<String,String> data = new HashMap<>();
        try {
            //Pour chaque colonne de la classe
            for (DatabaseColumns column : getColumns()) {
                //Booléen pour trier les colonnes à récupérer
                boolean isContained = (columnsFilter == null) || columnsFilter.contains(column.toString());
                //Si la colonne est sélectionnée
                if (isContained) {
                    //Valeur de la colonne
                    String value = "";
                    //Récupération du nom de la méthode
                    Method getColumn = getClass().getMethod("get" + column);
                    //Si le type de retour est une date
                    if (getColumn.getReturnType() == Date.class)
                        //Formattage de la date
                        value = new SimpleDateFormat("yyyy-MM-dd").format(getColumn.invoke(this));
                    //Appel de la méthode et récupération du résultat
                    else value = getColumn.invoke(this).toString();
                    //Si la colonne est un booléen
                    if (getColumn.getAnnotatedReturnType().toString().equals("boolean"))
                        //Conversion du booléen en entier
                        value = parseBooleanFromString(value);
                    //Ajout de la colonne et de la valeur dans la map
                    data.put(column.toString(), value);
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return data;
    }

    public Class<?> getColumnType(String column) {
        try {
            return getClass().getMethod("get" + column).getReturnType();
        } catch (NoSuchMethodException e) { e.printStackTrace(); }
        return null;
    }

    private String parseBooleanFromString(String string) {
        //En fonction du string
        switch (string) {
            //Booléen à faux
            case "false":
                //Valeur vaut 0
                return "0";
            //Booléen à vrai
            case "true":
                //Valeur vaut 1
                return "1";
            //Entier à 0
            case "0":
                //Valeur vaut faux
                return "false";
            //Entier à 1
            case "1":
                //Valeur vaut vrai
                return "true";
            default:
                return null;
        }
    }

    //************* GETTERS & SETTERS ***************//

    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }
    public Tables getTable() { return this.table; }
}
