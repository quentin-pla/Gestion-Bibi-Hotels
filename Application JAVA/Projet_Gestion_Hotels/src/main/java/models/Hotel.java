package models;

import database.DatabaseModel;

public class Hotel implements DatabaseModel {
    //ID
    private int ID;
    //Nom
    private String NAME;
    //Adresse
    private String STREET;
    //Ville
    private String CITY;
    //Nombre d'étoiles
    private int STAR_RATING;

    //Liste des colonnes
    private enum columns {
        ID,NAME,STREET,CITY,STAR_RATING
    }

    @Override
    public DatabaseModel newInstance() {
        return new Hotel();
    }

    @Override
    public void setColumn(String columnItem, String value) {
        Hotel.columns column = Hotel.columns.valueOf(columnItem);
        switch (column) {
            case ID:
                this.setID(Integer.parseInt(value));
                break;
            case NAME:
                this.setNAME(value);
                break;
            case STREET:
                this.setSTREET(value);
                break;
            case CITY:
                this.setCITY(value);
                break;
            case STAR_RATING:
                this.setSTAR_RATING(Integer.parseInt(value));
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

    public String getSTREET() {
        return STREET;
    }

    public void setSTREET(String STREET) {
        this.STREET = STREET;
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public int getSTAR_RATING() {
        return STAR_RATING;
    }

    public void setSTAR_RATING(int STAR_RATING) {
        this.STAR_RATING = STAR_RATING;
    }
}
