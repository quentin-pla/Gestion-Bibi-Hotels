package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class Hotel extends DatabaseModel {
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
        NAME,STREET,CITY,STAR_RATING
    }

    //Constructeur
    public Hotel() {
        this.table = Tables.HOTELS;
    }

    @Override
    public DatabaseColumns[] getModelColumns() {
        return Columns.values();
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

    public void setSTAR_RATING(String STAR_RATING) { this.STAR_RATING = Integer.parseInt(STAR_RATING); }
}
