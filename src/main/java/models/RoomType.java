package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class RoomType extends DatabaseModel {
    /**
     * Table liée à la base de données
     */
    private Tables table = Tables.ROOMTYPES;

    /**
     * Nom
     */
    private String NAME;

    /**
     * Prix
     */
    private int PRICE;

    /**
     * Liste des colonnes
     */
    private enum Columns implements DatabaseColumns {
        NAME,PRICE
    }

    /**
     * Constructeur
     */
    public RoomType() {}

    /**
     * Constructeur surchargé
     * @param NAME nom du type de chambre
     * @param PRICE prix du type de chambre
     */
    public RoomType(String NAME, int PRICE) {
        this.NAME = NAME;
        this.PRICE = PRICE;
        this.save();
    }

    //************* GETTERS & SETTERS ***************//

    @Override
    public DatabaseColumns[] getModelColumns() {
        return Columns.values();
    }

    public static Tables getTableName() { return Tables.ROOMTYPES; }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public int getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = Integer.parseInt(PRICE);
    }
}
