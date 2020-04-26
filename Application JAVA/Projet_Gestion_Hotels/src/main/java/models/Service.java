package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class Service extends DatabaseModel {
    /**
     * Table liée à la base de données
     */
    private Tables table = Tables.SERVICES;

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
    private int PRICE;

    /**
     * Liste des colonnes
     */
    private enum Columns implements DatabaseColumns {
        HOTEL_ID,NAME,PRICE
    }

    /**
     * Constructeur
     */
    public Service() {}

    /**
     * Constructeur surchargé
     * @param HOTEL_ID id de l'hotel lié
     * @param NAME nom du service
     * @param PRICE prix du service
     */
    public Service(int HOTEL_ID, String NAME, int PRICE) {
        this.HOTEL_ID = HOTEL_ID;
        this.NAME = NAME;
        this.PRICE = PRICE;
    }

    //************* GETTERS & SETTERS ***************//

    @Override
    public DatabaseColumns[] getModelColumns() {
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

    public int getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = Integer.parseInt(PRICE);
    }
}
