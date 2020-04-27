package models;

import database.DatabaseColumns;
import database.DatabaseData;
import database.DatabaseModel;

public class Service extends DatabaseModel {
    /**
     * ID de l'hotel lié
     */
    private int HOTEL_ID;

    /**
     * Nom
     */
    private String NAME;

    /**
     * Prix
     */
    private double PRICE;

    /**
     * Achat multiple
     */
    private boolean UNIQUE_ORDER;

    /**
     * Liste des colonnes
     */
    public enum Columns implements DatabaseColumns {
        HOTEL_ID,NAME,PRICE,UNIQUE_ORDER
    }

    /**
     * Constructeur
     */
    public Service() {
        super(Tables.SERVICES);
    }

    /**
     * Constructeur surchargé
     * @param HOTEL_ID id de l'hotel lié
     * @param NAME nom du service
     * @param PRICE prix du service
     */
    public Service(int HOTEL_ID, String NAME, double PRICE) {
        super(Tables.SERVICES);
        this.HOTEL_ID = HOTEL_ID;
        this.NAME = NAME;
        this.PRICE = PRICE;
    }

    //************* REFERENCES ***************//

    /**
     * Hotel lié
     * @return hotel
     */
    public Hotel getHotel() {
        return (Hotel) DatabaseData.getInstance().getReferenceFromID(Tables.HOTELS,HOTEL_ID);
    }

    //************* GETTERS & SETTERS ***************//

    @Override
    public DatabaseColumns[] getColumns() {
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

    public double getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = Double.parseDouble(PRICE);
    }

    public boolean getUNIQUE_ORDER() { return UNIQUE_ORDER; }

    public void setUNIQUE_ORDER(String UNIQUE_ORDER) { this.UNIQUE_ORDER = Boolean.parseBoolean(UNIQUE_ORDER); }
}
