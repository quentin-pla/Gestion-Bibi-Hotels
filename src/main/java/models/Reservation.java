package models;

import database.DatabaseModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation implements DatabaseModel {
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
    private enum columns {
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
    public DatabaseModel newInstance() { return new Reservation(); }

    @Override
    public void setColumn(String columnItem, String value) {
        Reservation.columns column = Reservation.columns.valueOf(columnItem);
        switch (column) {
            case ID:
                this.setID(Integer.parseInt(value));
                break;
            case CLIENT_ID:
                this.setCLIENT_ID(Integer.parseInt(value));
                break;
            case HOTEL_ID:
                this.setHOTEL_ID(Integer.parseInt(value));
                break;
            case ROOMTYPE_ID:
                this.setROOMTYPE_ID(Integer.parseInt(value));
                break;
            case ARRIVAL_DATE:
                this.setARRIVAL_DATE(parseDate(value));
                break;
            case EXIT_DATE:
                this.setEXIT_DATE(parseDate(value));
                break;
            case DURATION:
                this.setDURATION(Integer.parseInt(value));
                break;
            case ROOM_COUNT:
                this.setROOM_COUNT(Integer.parseInt(value));
            case PEOPLE_COUNT:
                this.setPEOPLE_COUNT(Integer.parseInt(value));
                break;
            case IS_PAYED:
                this.setIS_PAYED(Boolean.parseBoolean(value));
                break;
            case IS_COMFIRMED:
                this.setIS_COMFIRMED(Boolean.parseBoolean(value));
                break;
            case IS_CANCELLED:
                this.setIS_CANCELLED(Boolean.parseBoolean(value));
                break;
            default:
                break;
        }
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCLIENT_ID() {
        return CLIENT_ID;
    }

    public void setCLIENT_ID(int CLIENT_ID) {
        this.CLIENT_ID = CLIENT_ID;
    }

    public int getHOTEL_ID() {
        return HOTEL_ID;
    }

    public void setHOTEL_ID(int HOTEL_ID) {
        this.HOTEL_ID = HOTEL_ID;
    }

    public int getROOMTYPE_ID() {
        return ROOMTYPE_ID;
    }

    public void setROOMTYPE_ID(int ROOMTYPE_ID) {
        this.ROOMTYPE_ID = ROOMTYPE_ID;
    }

    public Date getARRIVAL_DATE() {
        return ARRIVAL_DATE;
    }

    public void setARRIVAL_DATE(Date ARRIVAL_DATE) {
        this.ARRIVAL_DATE = ARRIVAL_DATE;
    }

    public Date getEXIT_DATE() {
        return EXIT_DATE;
    }

    public void setEXIT_DATE(Date EXIT_DATE) {
        this.EXIT_DATE = EXIT_DATE;
    }

    public int getDURATION() {
        return DURATION;
    }

    public void setDURATION(int DURATION) {
        this.DURATION = DURATION;
    }

    public int getROOM_COUNT() {
        return ROOM_COUNT;
    }

    public void setROOM_COUNT(int ROOM_COUNT) {
        this.ROOM_COUNT = ROOM_COUNT;
    }

    public int getPEOPLE_COUNT() {
        return PEOPLE_COUNT;
    }

    public void setPEOPLE_COUNT(int PEOPLE_COUNT) {
        this.PEOPLE_COUNT = PEOPLE_COUNT;
    }

    public boolean isIS_PAYED() {
        return IS_PAYED;
    }

    public void setIS_PAYED(boolean IS_PAYED) {
        this.IS_PAYED = IS_PAYED;
    }

    public boolean isIS_COMFIRMED() {
        return IS_COMFIRMED;
    }

    public void setIS_COMFIRMED(boolean IS_COMFIRMED) {
        this.IS_COMFIRMED = IS_COMFIRMED;
    }

    public boolean isIS_CANCELLED() {
        return IS_CANCELLED;
    }

    public void setIS_CANCELLED(boolean IS_CANCELLED) {
        this.IS_CANCELLED = IS_CANCELLED;
    }
}
