package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class Service extends DatabaseModel {
    //ID
    private int ID;
    //ID de l'hotel lié
    private int HOTEL_ID;
    //Nom
    private String NAME;
    //Prix
    private int PRICE;

    //Liste des colonnes
    private enum Columns implements DatabaseColumns {
        ID,HOTEL_ID,NAME,PRICE
    }

    @Override
    public DatabaseColumns[] getModelColumns() {
        return Columns.values();
    }

    @Override
    public DatabaseModel newInstance() {
        return new Service();
    }

    public int getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = Integer.parseInt(ID);
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
