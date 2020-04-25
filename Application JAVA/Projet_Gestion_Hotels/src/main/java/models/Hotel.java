package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class Hotel extends DatabaseModel {
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
    private enum Columns implements DatabaseColumns {
        ID,NAME,STREET,CITY,STAR_RATING
    }

    @Override
    public DatabaseColumns[] getModelColumns() {
        return Columns.values();
    }

    @Override
    public DatabaseModel newInstance() {
        return new Hotel();
    }

    public int getID() {
        return ID;
    }

    public void setID(String ID) { this.ID = Integer.parseInt(ID); }

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

    public void setSTAR_RATING(String STAR_RATING) { this.STAR_RATING = Integer.parseInt(STAR_RATING); }
}
