package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class RoomType extends DatabaseModel {
    //ID
    private int ID;
    //Nom
    private String NAME;
    //Prix
    private int PRICE;

    //Liste des colonnes
    private enum Columns implements DatabaseColumns {
        ID,NAME,PRICE
    }

    @Override
    public DatabaseColumns[] getModelColumns() {
        return Columns.values();
    }

    @Override
    public DatabaseModel newInstance() {
        return new RoomType();
    }

    public int getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = Integer.parseInt(ID);
    }

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
