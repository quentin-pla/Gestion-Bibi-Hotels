package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class Hotel extends DatabaseModel {
    /**
     * Nom
     */
    private String HOTEL_NAME;

    /**
     * Adresse
     */
    private String STREET;

    /**
     * Ville
     */
    private String CITY;

    /**
     * Nombre d'étoiles
     */
    private int STAR_RATING;

    /**
     * Liste des colonnes
     */
    public enum Columns implements DatabaseColumns {
        HOTEL_NAME,STREET,CITY,STAR_RATING
    }

    /**
     * Constructeur
     */
    public Hotel() {
        super(Tables.HOTELS);
    }

    /**
     * Constructeur surchargé
     * @param HOTEL_NAME nom
     * @param STREET adresse
     * @param CITY ville
     * @param STAR_RATING nombre d'étoiles
     */
    public Hotel(String HOTEL_NAME, String STREET, String CITY, int STAR_RATING) {
        super(Tables.HOTELS);
        this.HOTEL_NAME = HOTEL_NAME;
        this.STREET = STREET;
        this.CITY = CITY;
        this.STAR_RATING = STAR_RATING;
        this.save();
    }

    //************* GETTERS & SETTERS ***************//

    @Override
    public DatabaseColumns[] getColumns() {
        return Columns.values();
    }

    public String getHOTEL_NAME() {
        return HOTEL_NAME;
    }

    public void setHOTEL_NAME(String HOTEL_NAME) {
        this.HOTEL_NAME = HOTEL_NAME;
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
