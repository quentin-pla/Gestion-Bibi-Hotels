package database;

import models.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class DatabaseModel {
    //ID
    protected int ID;

    //Table correspondante dans la base de données
    protected Tables table;

    //Liste des tables de la base de données
    public enum Tables {
        BILLS,CLIENTS,HOTELS,OCCUPANTS,OCCUPATIONS,RESERVATIONS,ROOMS,ROOMTYPES,SERVICES
    }

    //Liste des colonnes du modèle
    public abstract DatabaseColumns[] getModelColumns();

    //Générer une nouvelle instance
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

    //Définir un attribut à partir de la valeur d'une colonne de la base de données
    public void setColumnAttribute(String columnItem, String value) {
        try {
            Method setColumn = getClass().getMethod("set" + columnItem, String.class);
            setColumn.invoke(this, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //Récupérer toutes la valeur de chaque colonne
    public Map<String, String> getAttributesData() {
        return getAttributesData(null);
    }

    //Récupérer les valeurs de colonnes spécifiques
    public Map<String, String> getAttributesData(String columnsFilter) {
        //Résultats
        Map<String,String> data = new HashMap<>();
        try {
            //Pour chaque attributs de la classe
            for (DatabaseColumns column : getModelColumns()) {
                //Booléen pour trier les colonnes à récupérer
                boolean isContained = (columnsFilter == null) || columnsFilter.contains(column.toString());
                //Si la colonne est sélectionnée
                if (isContained) {
                    //Récupération du nom de la méthode
                    Method getColumn = getClass().getMethod("get" + column);
                    //Appel de la méthode et récupération du résultat
                    String value = getColumn.invoke(this).toString();
                    //En fonction du résultat
                    switch (value) {
                        //Booléen à faux
                        case "false":
                            //Valeur vaut 0
                            value = "0";
                            break;
                        //Booléen à vrai
                        case "true":
                            //Valeur vaut 1
                            value = "1";
                            break;
                        default:
                            break;
                    }
                    //Ajout de la colonne et de la valeur dans la map
                    data.put(column.toString(), value);
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return data;
    }

    public int getID() { return ID; }
    public void setID(String ID) { this.ID = Integer.parseInt(ID); }
    public Tables getTable() { return this.table; }
}
