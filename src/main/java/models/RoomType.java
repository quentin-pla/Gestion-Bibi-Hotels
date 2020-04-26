package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class RoomType extends DatabaseModel {
    //Nom
    private String NAME;
    //Prix
    private int PRICE;

    //Liste des colonnes
    private enum Columns implements DatabaseColumns {
        NAME,PRICE
    }

    //Constructeur
    public RoomType() {
        this.table = Tables.ROOMTYPES;
    }

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
