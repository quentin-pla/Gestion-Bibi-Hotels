package models;

import database.DatabaseColumns;
import database.DatabaseData;
import database.DatabaseModel;

import java.util.ArrayList;

public class Occupation extends DatabaseModel {
    /**
     * Services facturés
     */
    private ArrayList<Service> billedServices;

    /**
     * Occupants
     */
    private ArrayList<Occupant> occupants;

    /**
     * ID de la réservation liée
     */
    private int RESERVATION_ID;

    /**
     * ID de la chambre liée
     */
    private int ROOM_ID;

    /**
     * Client présent dans la chambre
     */
    private boolean IS_CLIENT_PRESENT;

    /**
     * Occupation archivée
     */
    private boolean IS_ARCHIVED;

    /**
     * Liste des colonnes
     */
    public enum Columns implements DatabaseColumns {
        RESERVATION_ID,ROOM_ID,IS_CLIENT_PRESENT,IS_ARCHIVED
    }

    /**
     * Constructeur
     */
    public Occupation() {
        super(Tables.OCCUPATIONS);
        this.billedServices = new ArrayList<>();
        this.occupants = new ArrayList<>();
    }

    /**
     * Ajouter un service à facturer
     * @param service service
     */
    public void billService(Service service) {
        this.billedServices.add(service);
    }

    /**
     * Ajouter un occupant dans la chambre
     * @param occupant occupant
     */
    public void addOccupant(Occupant occupant) {
        this.occupants.add(occupant);
    }

    /**
     * Constructeur surchargé
     * @param RESERVATION_ID id de la réservation liée
     * @param ROOM_ID id de la chambre liée
     * @param IS_CLIENT_PRESENT client présent ou non dans la chambre
     */
    public Occupation(int RESERVATION_ID, int ROOM_ID, boolean IS_CLIENT_PRESENT) {
        super(Tables.OCCUPATIONS);
        this.billedServices = new ArrayList<>();
        this.occupants = new ArrayList<>();
        this.RESERVATION_ID = RESERVATION_ID;
        this.ROOM_ID = ROOM_ID;
        this.IS_CLIENT_PRESENT = IS_CLIENT_PRESENT;
        this.save();
    }

    //************* REFERENCES ***************//

    /**
     * Réservation liée
     * @return réservation
     */
    public Reservation getReservation() {
        return (Reservation) DatabaseData.getInstance().getReferenceFromID(Tables.RESERVATIONS,RESERVATION_ID);
    }

    /**
     * Chambre liée
     * @return chambre
     */
    public Room getRoom() {
        return (Room) DatabaseData.getInstance().getReferenceFromID(Tables.ROOMS, ROOM_ID);
    }

    //************* GETTERS & SETTERS ***************//

    @Override
    public DatabaseColumns[] getColumns() { return Columns.values(); }

    public ArrayList<Service> getBilledServices() {
        return billedServices;
    }

    public void setBilledServices(ArrayList<Service> billedServices) {
        this.billedServices = billedServices;
    }

    public ArrayList<Occupant> getOccupants() {
        return occupants;
    }

    public void setOccupants(ArrayList<Occupant> occupants) {
        this.occupants = occupants;
    }

    public int getRESERVATION_ID() {
        return RESERVATION_ID;
    }

    public void setRESERVATION_ID(int RESERVATION_ID) {
        this.RESERVATION_ID = RESERVATION_ID;
    }

    public int getROOM_ID() {
        return ROOM_ID;
    }

    public void setROOM_ID(int ROOM_ID) {
        this.ROOM_ID = ROOM_ID;
    }

    public boolean getIS_CLIENT_PRESENT() {
        return IS_CLIENT_PRESENT;
    }

    public void setIS_CLIENT_PRESENT(boolean IS_CLIENT_PRESENT) {
        this.IS_CLIENT_PRESENT = IS_CLIENT_PRESENT;
    }

    public boolean getIS_ARCHIVED() {
        return IS_ARCHIVED;
    }

    public void setIS_ARCHIVED(boolean IS_ARCHIVED) {
        this.IS_ARCHIVED = IS_ARCHIVED;
    }
}
