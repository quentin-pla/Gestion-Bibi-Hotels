package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class Hotel extends DatabaseModel {
    /**
     * Service réservation de l'hotel
     */
    private ReservationService reservationService;

    /**
     * Service facturation de l'hotel
     */
    private BillingService billingService;

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
    public enum Columns implements DatabaseColumns {
        NAME,STREET,CITY,STAR_RATING
    }

    /**
     * Constructeur
     */
    public Hotel() {
        super(Tables.HOTELS);
        this.reservationService = new ReservationService(this);
        this.billingService = new BillingService(this);
    }

    /**
     * Constructeur surchargé
     * @param NAME nom
     * @param STREET adresse
     * @param CITY ville
     * @param STAR_RATING nombre d'étoiles
     */
    public Hotel(String NAME, String STREET, String CITY, int STAR_RATING) {
        super(Tables.HOTELS);
        this.NAME = NAME;
        this.STREET = STREET;
        this.CITY = CITY;
        this.STAR_RATING = STAR_RATING;
        this.reservationService = new ReservationService(this);
        this.billingService = new BillingService(this);
        this.save();
    }

    /**
     * Initialisation des données nécessaires pour les services
     */
    public void initServices() {
        //Initialisation des réservations
        getReservationService().initReservations();
        //Initialisation des factures
        getBillingService().initBills();
    }

    //************* GETTERS & SETTERS ***************//

    @Override
    public DatabaseColumns[] getColumns() {
        return Columns.values();
    }

    public ReservationService getReservationService() {
        return reservationService;
    }

    public void setReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public BillingService getBillingService() {
        return billingService;
    }

    public void setBillingService(BillingService billingService) {
        this.billingService = billingService;
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
