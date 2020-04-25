package models;

import database.DatabaseModel;

public class RoomType implements DatabaseModel {
    //ID
    private int ID;
    //Nom
    private String NAME;
    //Prix
    private int PRICE;

    //Liste des colonnes
    private enum columns {
        ID,NAME,PRICE
    }

    @Override
    public DatabaseModel newInstance() {
        return new RoomType();
    }

    @Override
    public void setColumn(String columnItem, String value) {
        RoomType.columns column = RoomType.columns.valueOf(columnItem);
        switch (column) {
            case ID:
                this.setID(Integer.parseInt(value));
                break;
            case NAME:
                this.setNAME(value);
                break;
            case PRICE:
                this.setPRICE(Integer.parseInt(value));
                break;
            default:
                break;
        }
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public void setPRICE(int PRICE) {
        this.PRICE = PRICE;
    }
}
