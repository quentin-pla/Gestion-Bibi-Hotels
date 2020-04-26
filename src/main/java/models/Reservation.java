package models;

import database.DatabaseColumns;
import database.DatabaseModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation extends DatabaseModel {
    /**
     * Table liée à la base de données
     */
    private Tables table = Tables.RESERVATIONS;

    /**
     * ID du client lié
     */
    protected int CLIENT_ID;

    /**
     * ID de l'hotel lié
     */
    protected int HOTEL_ID;

    /**
     * ID du type de chambre lié
     */
    protected int ROOMTYPE_ID;

    /**
     * Date d'arrivée
     */
    protected Date ARRIVAL_DATE;

    /**
     * Date de sortie
     */
    protected Date EXIT_DATE;

    /**
     * Durée
     */
    protected int DURATION;

    /**
     * Nombre de chambre
     */
    protected int ROOM_COUNT;

    /**
     * Nombre d'occupants
     */
    protected int PEOPLE_COUNT;

    /**
     * Montant payé
     */
    protected boolean IS_PAYED;

    /**
     * Réservation confirmée
     */
    protected boolean IS_COMFIRMED;

    /**
     * Réservation annulée
     */
    protected boolean IS_CANCELLED;

    /**
     * Liste des colonnes
     */
    private enum Columns implements DatabaseColumns {
        CLIENT_ID,HOTEL_ID,ROOMTYPE_ID,ARRIVAL_DATE,EXIT_DATE,DURATION,ROOM_COUNT,PEOPLE_COUNT,IS_PAYED,IS_COMFIRMED,IS_CANCELLED
    }

    /**
     * Créer une date à partir d'un texte
     * @param value date au format string
     * @return Date
     */
    private Date parseDate(String value) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Constructeur
     */
    public Reservation() {}

    /**
     * Constructeur surchargé
     * @param CLIENT_ID id du client lié
     * @param HOTEL_ID id de l'hotel lié
     * @param ROOMTYPE_ID id du type de chambre lié
     * @param ARRIVAL_DATE date d'arrivée
     * @param EXIT_DATE date de départ
     * @param DURATION durée
     * @param ROOM_COUNT nombre de chambres
     * @param PEOPLE_COUNT nombre de personnes
     * @param IS_PAYED paiement réservation
     * @param IS_COMFIRMED est confirmée
     * @param IS_CANCELLED est annulée
     */
    public Reservation(int CLIENT_ID, int HOTEL_ID, int ROOMTYPE_ID, Date ARRIVAL_DATE, Date EXIT_DATE, int DURATION, int ROOM_COUNT, int PEOPLE_COUNT, boolean IS_PAYED, boolean IS_COMFIRMED, boolean IS_CANCELLED) {
        this.CLIENT_ID = CLIENT_ID;
        this.HOTEL_ID = HOTEL_ID;
        this.ROOMTYPE_ID = ROOMTYPE_ID;
        this.ARRIVAL_DATE = ARRIVAL_DATE;
        this.EXIT_DATE = EXIT_DATE;
        this.DURATION = DURATION;
        this.ROOM_COUNT = ROOM_COUNT;
        this.PEOPLE_COUNT = PEOPLE_COUNT;
        this.IS_PAYED = IS_PAYED;
        this.IS_COMFIRMED = IS_COMFIRMED;
        this.IS_CANCELLED = IS_CANCELLED;
        this.save();
    }

    //*************** GETTERS & SETTERS ***************//

    @Override
    public DatabaseColumns[] getModelColumns() {
        return Columns.values();
    }

    public int getCLIENT_ID() {
        return CLIENT_ID;
    }

    public void setCLIENT_ID(String CLIENT_ID) {
        this.CLIENT_ID = Integer.parseInt(CLIENT_ID);
    }

    public int getHOTEL_ID() {
        return HOTEL_ID;
    }

    public void setHOTEL_ID(String HOTEL_ID) {
        this.HOTEL_ID = Integer.parseInt(HOTEL_ID);
    }

    public int getROOMTYPE_ID() {
        return ROOMTYPE_ID;
    }

    public void setROOMTYPE_ID(String ROOMTYPE_ID) {
        this.ROOMTYPE_ID = Integer.parseInt(ROOMTYPE_ID);
    }

    public Date getARRIVAL_DATE() {
        return ARRIVAL_DATE;
    }

    public void setARRIVAL_DATE(String ARRIVAL_DATE) { this.ARRIVAL_DATE = parseDate(ARRIVAL_DATE); }

    public Date getEXIT_DATE() {
        return EXIT_DATE;
    }

    public void setEXIT_DATE(String EXIT_DATE) { this.EXIT_DATE = parseDate(EXIT_DATE); }

    public int getDURATION() {
        return DURATION;
    }

    public void setDURATION(String DURATION) {
        this.DURATION = Integer.parseInt(DURATION);
    }

    public int getROOM_COUNT() {
        return ROOM_COUNT;
    }

    public void setROOM_COUNT(String ROOM_COUNT) {
        this.ROOM_COUNT = Integer.parseInt(ROOM_COUNT);
    }

    public int getPEOPLE_COUNT() {
        return PEOPLE_COUNT;
    }

    public void setPEOPLE_COUNT(String PEOPLE_COUNT) {
        this.PEOPLE_COUNT = Integer.parseInt(PEOPLE_COUNT);
    }

    public boolean isIS_PAYED() {
        return IS_PAYED;
    }

    public void setIS_PAYED(String IS_PAYED) { this.IS_PAYED = Boolean.parseBoolean(IS_PAYED); }

    public boolean isIS_COMFIRMED() {
        return IS_COMFIRMED;
    }

    public void setIS_COMFIRMED(String IS_COMFIRMED) {
        this.IS_COMFIRMED = Boolean.parseBoolean(IS_COMFIRMED);
    }

    public boolean isIS_CANCELLED() {
        return IS_CANCELLED;
    }

    public void setIS_CANCELLED(String IS_CANCELLED) {
        this.IS_CANCELLED = Boolean.parseBoolean(IS_CANCELLED);
    }
}
