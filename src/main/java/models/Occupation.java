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
     * @param reservation_id id de la réservation liée
     * @param room_id id de la chambre liée
     * @param is_client_present client présent ou non dans la chambre
     */
    public Occupation(int reservation_id, int room_id, boolean is_client_present) {
        super(Tables.OCCUPATIONS);
        this.billedServices = new ArrayList<>();
        this.occupants = new ArrayList<>();
        this.RESERVATION_ID = reservation_id;
        this.ROOM_ID = room_id;
        this.IS_CLIENT_PRESENT = is_client_present;
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

    public int getRESERVATION_ID() {
        return RESERVATION_ID;
    }

    public void setRESERVATION_ID(String RESERVATION_ID) {
        this.RESERVATION_ID = Integer.parseInt(RESERVATION_ID);
    }

    public int getROOM_ID() {
        return ROOM_ID;
    }

    public void setROOM_ID(String ROOM_ID) {
        this.ROOM_ID = Integer.parseInt(ROOM_ID);
    }

    public boolean getIS_CLIENT_PRESENT() {
        return IS_CLIENT_PRESENT;
    }

    public void setIS_CLIENT_PRESENT(String IS_CLIENT_PRESENT) { this.IS_CLIENT_PRESENT = Boolean.parseBoolean(IS_CLIENT_PRESENT); }

    public ArrayList<Service> getBilledServices() {
        return billedServices;
    }

    public ArrayList<Occupant> getOccupants() {
        return occupants;
    }

    public boolean getIS_ARCHIVED() { return IS_ARCHIVED; }

    public void setIS_ARCHIVED(String IS_ARCHIVED) { this.IS_ARCHIVED = Boolean.parseBoolean(IS_ARCHIVED); }
}
