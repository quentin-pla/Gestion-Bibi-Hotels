package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class Hotel extends DatabaseModel {
    /**
     * Service réservation de l'hotel
     */
    private ReservationService reservationService;

    /**
     * Service client de l'hotel
     */
    private ClientService clientService;

    /**
     * Service facturation de l'hotel
     */
    private BillingService billingService;

    /**
     * Table liée à la base de données
     */
    private Tables table = Tables.HOTELS;

    /**
     * Nom
     */
    private String NAME;

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
    private enum Columns implements DatabaseColumns {
        NAME,STREET,CITY,STAR_RATING
    }

    /**
     * Constructeur
     */
    public Hotel() {
        this.reservationService = new ReservationService(this);
        this.clientService = new ClientService(this);
        this.billingService = new BillingService(this);
    }

    /**
     * Constructeur surchargé
     * @param name
     * @param street
     * @param city
     * @param star_rating
     */
    public Hotel(String name, String street, String city, int star_rating) {
        this.NAME = name;
        this.STREET = street;
        this.CITY = city;
        this.STAR_RATING = star_rating;
        this.reservationService = new ReservationService(this);
        this.clientService = new ClientService(this);
        this.billingService = new BillingService(this);
        this.save();
    }

    //************* GETTERS & SETTERS ***************//

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
