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

/**
 * Modèle provenant de la base de données
 */
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
        BILLS,BILLEDSERVICES,CLIENTS,HOTELS,OCCUPANTS,OCCUPATIONS,RESERVATIONS,ROOMS,ROOMTYPES,SERVICES
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
            case BILLS:             return new Bill();
            case BILLEDSERVICES:    return new BilledService();
            case CLIENTS:           return new Client();
            case HOTELS:            return new Hotel();
            case OCCUPANTS:         return new Occupant();
            case OCCUPATIONS:       return new Occupation();
            case RESERVATIONS:      return new Reservation();
            case ROOMS:             return new Room();
            case ROOMTYPES:         return new RoomType();
            case SERVICES:          return new Service();
            default:                return null;
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
        //Insertion de l'objet dans la base de données
        insertQuery(this);
        //Ajout de l'objet dans les données locales
        switch (this.getTable()) {
            case BILLS:
                DatabaseData.getInstance().getBills().put(ID,(Bill) this);
                break;
            case BILLEDSERVICES:
                DatabaseData.getInstance().getBilledServices().put(ID,(BilledService) this);
                break;
            case CLIENTS:
                DatabaseData.getInstance().getClients().put(ID,(Client) this);
                break;
            case HOTELS:
                DatabaseData.getInstance().getHotels().put(ID,(Hotel) this);
                break;
            case OCCUPANTS:
                DatabaseData.getInstance().getOccupants().put(ID,(Occupant) this);
                break;
            case OCCUPATIONS:
                DatabaseData.getInstance().getOccupations().put(ID,(Occupation) this);
                break;
            case RESERVATIONS:
                DatabaseData.getInstance().getReservations().put(ID,(Reservation) this);
                break;
            case ROOMS:
                DatabaseData.getInstance().getRooms().put(ID,(Room) this);
                break;
            case ROOMTYPES:
                DatabaseData.getInstance().getRoomTypes().put(ID,(RoomType) this);
                break;
            case SERVICES:
                DatabaseData.getInstance().getServices().put(ID,(Service) this);
                break;
            default:
                break;
        }
    }

    /**
     * Mettre à jour une colonne dans la base de données
     * @param column colonne
     */
    public void updateColumn(DatabaseColumns column) {
        //Exécution d'une requête SQL de mise à jour
        updateQuery(this, column.toString());
    }

    /**
     * Définir un attribut à partir de la valeur d'une colonne de la base de données
     * @param columnItem nom de la colonne
     * @param value valeur à attribuer
     */
    public void setColumnAttribute(String columnItem, String value) {
        //Récupération du type de l'attribut
        Class<?> parameterType = getColumnType(columnItem);
        try {
            //Récupération du setter à partir du nom de l'attribut
            Method setColumn = getClass().getMethod("set" + columnItem, parameterType);
            //Selon le type
            switch (parameterType.getSimpleName()) {
                case "boolean":
                    //Exécution de la méthode
                    setColumn.invoke(this, value.equals("1"));
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
            //Message d'erreur
            e.printStackTrace();
        }
    }

    /**
     * Récupérer la valeur de chaque colonne
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
                    //Récupération du getter de la méthode
                    Method getColumn = getClass().getMethod("get" + column);
                    //Si le type de retour est une date
                    if (getColumn.getReturnType() == Date.class)
                        //Formattage de la date
                        value = new SimpleDateFormat("yyyy-MM-dd").format(getColumn.invoke(this));
                    //Si le type de retour est un booléen
                    else if (getColumn.getReturnType() == boolean.class)
                        //Formattage du booléen
                        value = (Boolean) getColumn.invoke(this) ? "1" : "0";
                    //Appel de la méthode et récupération du résultat
                    else value = getColumn.invoke(this).toString();
                    //Ajout de la colonne et de la valeur dans la map
                    data.put(column.toString(), value);
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            //Message d'erreur
            e.printStackTrace();
        }
        //Retour des données
        return data;
    }

    /**
     * Récupérer le type d'une colonne
     * @param column colonne
     * @return type
     */
    public Class<?> getColumnType(String column) {
        try {
            //Récupération du getter de la colonne et renvoi du type
            return getClass().getMethod("get" + column).getReturnType();
        } catch (NoSuchMethodException e) { e.printStackTrace(); }
        return null;
    }

    /**
     * Mettre à jour les attributs d'un modèle à partir d'un second modèle
     * @param updatedModel second modèle
     */
    public void updateFromModel(DatabaseModel updatedModel) {
        //Récupération des attributs du modèle
        Map<String,String> attributes = this.getAttributesData();
        //Récupération des attributs du modèle
        Map<String,String> updateAttributes = updatedModel.getAttributesData();
        //Pour chaque attribut du modèle
        for (Map.Entry<String,String> attribute : attributes.entrySet())
            //Si la valeur de l'attribut du modèle à jour est différente de celle actuelle
            if (!attribute.getValue().equals(updateAttributes.get(attribute.getKey())))
                //Mise à jour de l'attribut du modèle
                this.setColumnAttribute(attribute.getKey(),updateAttributes.get(attribute.getKey()));
    }

    //************* GETTERS & SETTERS ***************//

    public int getID() { return ID; }

    public void setID(int ID) { this.ID = ID; }

    public Tables getTable() { return this.table; }
}
