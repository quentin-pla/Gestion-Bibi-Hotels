package database;

import models.Client;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class DatabaseModel {
    //Liste des colonnes du modèle
    public abstract DatabaseColumns[] getModelColumns();

    //Générer une nouvelle instance
    public abstract DatabaseModel newInstance();

    //Définir un attribut à partir de la valeur d'une colonne de la base de données
    public void setColumnAttribute(String columnItem, String value) {
        try {
            Method setColumn = getClass().getMethod("set" + columnItem, String.class);
            setColumn.invoke(this, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //Récupérer les valeurs de chaque colonne
    public Map<String, String> getAttributesData() {
        //Résultats
        Map<String,String> data = new HashMap<>();
        try {
            //Pour chaque attributs de la classe
            for (DatabaseColumns column : getModelColumns()) {
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
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return data;
    }
}
