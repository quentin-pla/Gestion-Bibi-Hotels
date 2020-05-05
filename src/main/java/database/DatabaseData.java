package database;

import controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import models.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static database.DatabaseConnection.selectQuery;

public class DatabaseData {
    /**
     * Factures
     */
    private ObservableMap<Integer,Bill> bills;

    /**
     * Clients
     */
    private ObservableMap<Integer,Client> clients;

    /**
     * Hotels
     */
    private ObservableMap<Integer,Hotel> hotels;

    /**
     * Occupants
     */
    private ObservableMap<Integer,Occupant> occupants;

    /**
     * Occupations
     */
    private ObservableMap<Integer,Occupation> occupations;

    /**
     * Réservations
     */
    private ObservableMap<Integer,Reservation> reservations;

    /**
     * Chambres
     */
    private ObservableMap<Integer,Room> rooms;

    /**
     * Types de chambre
     */
    private ObservableMap<Integer,RoomType> roomTypes;

    /**
     * Services
     */
    private ObservableMap<Integer,Service> services;

    /**
     * Éléments obtenus depuis la requête
     */
    private ArrayList<DatabaseModel> queryResult;

    /**
     * Constructeur
     */
    private DatabaseData() {
        this.bills = FXCollections.observableHashMap();
        this.clients = FXCollections.observableHashMap();
        this.hotels = FXCollections.observableHashMap();
        this.occupants = FXCollections.observableHashMap();
        this.occupations = FXCollections.observableHashMap();
        this.reservations = FXCollections.observableHashMap();
        this.rooms = FXCollections.observableHashMap();
        this.roomTypes = FXCollections.observableHashMap();
        this.services = FXCollections.observableHashMap();
        //Récupération des données de la base de données
        retrieveDatabase();
        //Initialisation du rafraichissement
        initRefresh();
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
        }
        //Retour de l'instance
        return instance;
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
     * Rafraichir les éléments d'une table depuis la base de données
     */
    public void initRefresh() {
        //Initialisation d'un nouveau timer
        Timer timer = new Timer(true);
        //Récupération des données de la base de données toutes les 10s
        timer.schedule(new TimerTask() {
            public void run() {
                //service sélectionné
                MainController.ServicePanel actual_service = MainController.getInstance().getSelected_service();
                //Si le service ne vaut pas null
                if (actual_service != null) {
                    //Selon le service
                    switch (actual_service) {
                        case RESERVATION:
                            //Récupération des réservations
                            retrieveDatabaseReservations();
                            break;
                        case BILLING:
                            //Récupération des factures
                            retrieveDatabaseBills();
                            break;
                        case CLIENT:
                            //Récupération des occupations
                            retrieveDatabaseOccupations();
                            break;
                        default:
                            break;
                    }
                }
            }
        } ,0,5000);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseBills() {
        bills.clear();
        queryResult = selectQuery(DatabaseModel.Tables.BILLS);
        for (DatabaseModel bill : queryResult) bills.put(bill.getID(), (Bill) bill);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseClients() {
        clients.clear();
        queryResult = selectQuery(DatabaseModel.Tables.CLIENTS);
        for (DatabaseModel client : queryResult) clients.put(client.getID(), (Client) client);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseHotels() {
        hotels.clear();
        queryResult = selectQuery(DatabaseModel.Tables.HOTELS);
        for (DatabaseModel hotel : queryResult) hotels.put(hotel.getID(), (Hotel) hotel);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseOccupants() {
        occupants.clear();
        queryResult = selectQuery(DatabaseModel.Tables.OCCUPANTS);
        for (DatabaseModel occupant : queryResult) occupants.put(occupant.getID(), (Occupant) occupant);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseOccupations() {
        occupations.clear();
        queryResult = selectQuery(DatabaseModel.Tables.OCCUPATIONS);
        for (DatabaseModel occupation : queryResult) occupations.put(occupation.getID(), (Occupation) occupation);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseReservations() {
        reservations.clear();
        queryResult = selectQuery(DatabaseModel.Tables.RESERVATIONS);
        for (DatabaseModel reservation : queryResult) reservations.put(reservation.getID(), (Reservation) reservation);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseRooms() {
        rooms.clear();
        queryResult = selectQuery(DatabaseModel.Tables.ROOMS);
        for (DatabaseModel room : queryResult) rooms.put(room.getID(), (Room) room);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseRoomTypes() {
        roomTypes.clear();
        queryResult = selectQuery(DatabaseModel.Tables.ROOMTYPES);
        for (DatabaseModel room_type : queryResult) roomTypes.put(room_type.getID(), (RoomType) room_type);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    public void retrieveDatabaseServices() {
        services.clear();
        queryResult = selectQuery(DatabaseModel.Tables.SERVICES);
        for (DatabaseModel service : queryResult) services.put(service.getID(), (Service) service);
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

    public ObservableMap<Integer, Bill> getBills() {
        return bills;
    }

    public ObservableMap<Integer, Client> getClients() { return clients; }

    public ObservableMap<Integer, Hotel> getHotels() {
        return hotels;
    }

    public ObservableMap<Integer, Occupant> getOccupants() { return occupants; }

    public ObservableMap<Integer, Occupation> getOccupations() { return occupations; }

    public ObservableMap<Integer, Reservation> getReservations() {
        return reservations;
    }

    public ObservableMap<Integer, Room> getRooms() {
        return rooms;
    }

    public ObservableMap<Integer, RoomType> getRoomTypes() {
        return roomTypes;
    }

    public ObservableMap<Integer, Service> getServices() {
        return services;
    }

    public int getLastQueryResultSize() { return this.queryResult.size(); }
}
