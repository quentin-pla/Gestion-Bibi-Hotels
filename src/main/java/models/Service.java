package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class Service extends DatabaseModel {
    //ID de l'hotel lié
    private int HOTEL_ID;
    //Nom
    private String NAME;
    //Prix
    private int PRICE;

    //Liste des colonnes
    private enum Columns implements DatabaseColumns {
        HOTEL_ID,NAME,PRICE
    }

    //Constructeur
    public Service() {
        this.table = Tables.SERVICES;
    }

    @Override
    public DatabaseColumns[] getModelColumns() {
        return Columns.values();
    }

    public int getHOTEL_ID() { return HOTEL_ID; }

    public void setHOTEL_ID(String HOTEL_ID) { this.HOTEL_ID = Integer.parseInt(HOTEL_ID); }

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
