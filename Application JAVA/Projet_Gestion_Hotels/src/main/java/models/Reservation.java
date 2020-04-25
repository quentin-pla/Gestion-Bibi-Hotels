package models;

import database.DatabaseColumns;
import database.DatabaseModel;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation extends DatabaseModel {
    //ID
    protected int ID;
    //ID du client lié
    protected int CLIENT_ID;
    //ID de l'hotel lié
    protected int HOTEL_ID;
    //ID du type de chambre lié
    protected int ROOMTYPE_ID;
    //Date d'arrivée
    protected Date ARRIVAL_DATE;
    //Date de sortie
    protected Date EXIT_DATE;
    //Durée
    protected int DURATION;
    //Nombre de chambre
    protected int ROOM_COUNT;
    //Nombre d'occupants
    protected int PEOPLE_COUNT;
    //Montant payé
    protected boolean IS_PAYED;
    //Réservation confirmée
    protected boolean IS_COMFIRMED;
    //Réservation annulée
    protected boolean IS_CANCELLED;

    //Liste des colonnes
    private enum Columns implements DatabaseColumns {
        ID,CLIENT_ID,HOTEL_ID,ROOMTYPE_ID,ARRIVAL_DATE,EXIT_DATE,DURATION,ROOM_COUNT,PEOPLE_COUNT,IS_PAYED,IS_COMFIRMED,IS_CANCELLED
    }

    //Créer une date à partir d'un texte
    private Date parseDate(String value) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DatabaseColumns[] getModelColumns() {
        return Columns.values();
    }

    @Override
    public DatabaseModel newInstance() { return new Reservation(); }

    public int getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = Integer.parseInt(ID);
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
