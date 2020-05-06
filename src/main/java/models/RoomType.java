package models;

import database.DatabaseColumns;
import database.DatabaseModel;

/**
 * Modèle Type de chambre depuis la base de données
 */
public class RoomType extends DatabaseModel {
    /**
     * Nom
     */
    private String NAME;

    /**
     * Prix
     */
    private double PRICE;

    /**
     * Nombre de lits
     */
    private int BED_CAPACITY;

    /**
     * Téléphone compris
     */
    private boolean HAS_PHONE;

    /**
     * Télévision comprise
     */
    private boolean HAS_TV;

    /**
     * Liste des colonnes
     */
    public enum Columns implements DatabaseColumns {
        NAME,PRICE,BED_CAPACITY,HAS_PHONE,HAS_TV
    }

    /**
     * Constructeur
     */
    public RoomType() {
        super(Tables.ROOMTYPES);
    }

    /**
     * Constructeur surchargé
     * @param NAME nom du type de chambre
     * @param PRICE prix du type de chambre
     */
    public RoomType(String NAME, double PRICE, int BED_CAPACITY, boolean HAS_PHONE, boolean HAS_TV) {
        super(Tables.ROOMTYPES);
        this.NAME = NAME;
        this.PRICE = PRICE;
        this.BED_CAPACITY = BED_CAPACITY;
        this.HAS_PHONE = HAS_PHONE;
        this.HAS_TV = HAS_TV;
        this.save();
    }

    //************* GETTERS & SETTERS ***************//

    @Override
    public DatabaseColumns[] getColumns() {
        return Columns.values();
    }

    public static Tables getTableName() { return Tables.ROOMTYPES; }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public double getPRICE() {
        return PRICE;
    }

    public void setPRICE(double PRICE) {
        this.PRICE = PRICE;
    }

    public int getBED_CAPACITY() {
        return BED_CAPACITY;
    }

    public void setBED_CAPACITY(int BED_CAPACITY) {
        this.BED_CAPACITY = BED_CAPACITY;
    }

    public boolean getHAS_PHONE() {
        return HAS_PHONE;
    }

    public void setHAS_PHONE(boolean HAS_PHONE) {
        this.HAS_PHONE = HAS_PHONE;
    }

    public boolean getHAS_TV() {
        return HAS_TV;
    }

    public void setHAS_TV(boolean HAS_TV) {
        this.HAS_TV = HAS_TV;
    }
}
