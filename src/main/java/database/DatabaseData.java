package database;

import models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static database.DatabaseConnection.selectQuery;

public class DatabaseData {
    /**
     * Factures
     */
    private Map<Integer,Bill> bills;

    /**
     * Clients
     */
    private Map<Integer,Client> clients;

    /**
     * Hotels
     */
    private Map<Integer,Hotel> hotels;

    /**
     * Occupants
     */
    private Map<Integer,Occupant> occupants;

    /**
     * Occupations
     */
    private Map<Integer,Occupation> occupations;

    /**
     * Réservations
     */
    private Map<Integer,Reservation> reservations;

    /**
     * Chambres
     */
    private Map<Integer,Room> rooms;

    /**
     * Types de chambre
     */
    private Map<Integer,RoomType> roomTypes;

    /**
     * Services
     */
    private Map<Integer,Service> services;

    /**
     * Constructeur
     */
    private DatabaseData() {
        this.bills = new HashMap<>();
        this.clients = new HashMap<>();
        this.hotels = new HashMap<>();
        this.occupants = new HashMap<>();
        this.occupations = new HashMap<>();
        this.reservations = new HashMap<>();
        this.rooms = new HashMap<>();
        this.roomTypes = new HashMap<>();
        this.services = new HashMap<>();
        //Récupération des données de la base de données
        retrieveDatabase();
    }

    /**
     * Instance unique
     */
    private static DatabaseData instance = null;

    /**
     * Récupérer l'instance unique
     */
    public static DatabaseData getInstance() {
        //Si l'instance n'est pas initialisée
        if (instance == null) {
            //Initialisation de l'instance
            instance = new DatabaseData();
            //Initialisation des services
            instance.initServices();
        }
        //Retour de l'instance
        return instance;
    }

    /**
     * Rafraichir les données locales
     */
    public void refreshData() {
        //Instance null pour initialiser à nouveau la classe
        instance = null;
        //Initialisation avec une nouvelle instance
        getInstance();
    }

    /**
     * Récupérer toutes les données de la base de données
     */
    public void retrieveDatabase() {
        //Hotels
        retrieveDatabaseHotels();
        //Clients
        retrieveDatabaseClients();
        //Types de chambre
        retrieveDatabaseRoomTypes();
        //Chambres
        retrieveDatabaseRooms();
        //Reservations
        retrieveDatabaseReservations();
        //Occupations
        retrieveDatabaseOccupations();
        //Occupants
        retrieveDatabaseOccupants();
        //Services
        retrieveDatabaseServices();
        //Factures
        retrieveDatabaseBills();
    }

    /**
     * Initialisation des services de la chaine d'hotels
     */
    public void initServices() {
        //Pour chaque hotel
        for (Hotel hotel : hotels.values())
            //Initialisation du service réservation et facturation
            hotel.initServices();
        //Initialisation des hotels pour l'administration
        Administration.getInstance().initHotels();
        //Initialisation des occupations pour le service client
        ClientService.getInstance().initOccupations();
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseBills() {
        ArrayList<DatabaseModel> results = selectQuery(DatabaseModel.Tables.BILLS);
        for (DatabaseModel bill : results) bills.put(bill.getID(), (Bill) bill);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseClients() {
        ArrayList<DatabaseModel> results = selectQuery(DatabaseModel.Tables.CLIENTS);
        for (DatabaseModel client : results) clients.put(client.getID(), (Client) client);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseHotels() {
        ArrayList<DatabaseModel> results = selectQuery(DatabaseModel.Tables.HOTELS);
        for (DatabaseModel hotel : results) hotels.put(hotel.getID(), (Hotel) hotel);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseOccupants() {
        ArrayList<DatabaseModel> results = selectQuery(DatabaseModel.Tables.OCCUPANTS);
        for (DatabaseModel occupant : results) occupants.put(occupant.getID(), (Occupant) occupant);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseOccupations() {
        ArrayList<DatabaseModel> results = selectQuery(DatabaseModel.Tables.OCCUPATIONS);
        for (DatabaseModel occupation : results) occupations.put(occupation.getID(), (Occupation) occupation);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseReservations() {
        ArrayList<DatabaseModel> results = selectQuery(DatabaseModel.Tables.RESERVATIONS);
        for (DatabaseModel reservation : results) reservations.put(reservation.getID(), (Reservation) reservation);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseRooms() {
        ArrayList<DatabaseModel> results = selectQuery(DatabaseModel.Tables.ROOMS);
        for (DatabaseModel room : results) rooms.put(room.getID(), (Room) room);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseRoomTypes() {
        ArrayList<DatabaseModel> results = selectQuery(DatabaseModel.Tables.ROOMTYPES);
        for (DatabaseModel room_type : results) roomTypes.put(room_type.getID(), (RoomType) room_type);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseServices() {
        ArrayList<DatabaseModel> results = selectQuery(DatabaseModel.Tables.SERVICES);
        for (DatabaseModel service : results) services.put(service.getID(), (Service) service);
    }

    /**
     * Obtenir un élément à partir de son ID
     * @param table table
     * @param ID ID
     * @return élément
     */
    public DatabaseModel getReferenceFromID(DatabaseModel.Tables table, int ID) {
        //Élément récupéré
        DatabaseModel item;
        //En fonction de la table
        switch (table) {
            case BILLS:
                item = bills.get(ID);
                return (item != null) ? item : selectQuery(DatabaseModel.Tables.BILLS, ID);
            case CLIENTS:
                item = clients.get(ID);
                return (item != null) ? item : selectQuery(DatabaseModel.Tables.CLIENTS, ID);
            case HOTELS:
                item = hotels.get(ID);
                return (item != null) ? item : selectQuery(DatabaseModel.Tables.HOTELS, ID);
            case OCCUPANTS:
                item = occupants.get(ID);
                return (item != null) ? item : selectQuery(DatabaseModel.Tables.OCCUPANTS, ID);
            case OCCUPATIONS:
                item = occupations.get(ID);
                return (item != null) ? item : selectQuery(DatabaseModel.Tables.OCCUPATIONS, ID);
            case RESERVATIONS:
                item = reservations.get(ID);
                return (item != null) ? item : selectQuery(DatabaseModel.Tables.RESERVATIONS, ID);
            case ROOMS:
                item = rooms.get(ID);
                return (item != null) ? item : selectQuery(DatabaseModel.Tables.ROOMS, ID);
            case ROOMTYPES:
                item = roomTypes.get(ID);
                return (item != null) ? item : selectQuery(DatabaseModel.Tables.ROOMTYPES, ID);
            case SERVICES:
                item = services.get(ID);
                return (item != null) ? item : selectQuery(DatabaseModel.Tables.SERVICES, ID);
            default:
                break;
        }
        return null;
    }

    //*************** GETTERS REFERENCES ***************//

    public Map<Integer, Bill> getBills() {
        return bills;
    }

    public Map<Integer, Client> getClients() { return clients; }

    public Map<Integer, Hotel> getHotels() {
        return hotels;
    }

    public Map<Integer, Occupant> getOccupants() {
        return occupants;
    }

    public Map<Integer, Occupation> getOccupations() {
        return occupations;
    }

    public Map<Integer, Reservation> getReservations() {
        return reservations;
    }

    public Map<Integer, Room> getRooms() {
        return rooms;
    }

    public Map<Integer, RoomType> getRoomTypes() {
        return roomTypes;
    }

    public Map<Integer, Service> getServices() {
        return services;
    }
}
