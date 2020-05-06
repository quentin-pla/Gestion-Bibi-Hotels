package database;

import controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import models.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static database.DatabaseConnection.selectQuery;

/**
 * Données locales provenant de la base de données
 */
public class DatabaseData {
    /**
     * Factures
     */
    private ObservableMap<Integer,Bill> bills;

    /**
     * Services facturés
     */
    private ObservableMap<Integer,BilledService> billedServices;

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
     * Éléments obtenus d'une requête
     */
    private ArrayList<DatabaseModel> queryResult;

    /**
     * Constructeur
     */
    private DatabaseData() {
        this.bills = FXCollections.observableHashMap();
        this.billedServices = FXCollections.observableHashMap();
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
        //Initialisation du rafraichissement périodique
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
     * dans un ordre précis en fonction des clés étrangères
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
        //Services facturés
        retrieveDatabaseBilledServices();
    }

    /**
     * Rafraichir les éléments d'une table depuis la base de données
     */
    public void initRefresh() {
        //Initialisation d'un nouveau timer
        Timer timer = new Timer(true);
        //Récupération des données de la base de données toutes les 5s
        timer.schedule(new TimerTask() {
            public void run() {
                //Service sélectionné
                MainController.ServicePanel actual_service = MainController.getInstance().getSelected_service();
                //Si le service existe
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
                            //Récupération des services facturés
                            retrieveDatabaseBilledServices();
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
        //Suppression des éléments de la liste
        bills.clear();
        //Exécution de la requête pour récupérer les données
        queryResult = selectQuery(DatabaseModel.Tables.BILLS);
        //Ajout des éléments dans la liste
        for (DatabaseModel bill : queryResult) bills.put(bill.getID(), (Bill) bill);
    }

    /**
     * Récupérer tous les services facturés de la base de données
     */
    public void retrieveDatabaseBilledServices() {
        //Suppression des éléments de la liste
        billedServices.clear();
        //Exécution de la requête pour récupérer les données
        queryResult = selectQuery(DatabaseModel.Tables.BILLEDSERVICES);
        //Ajout des éléments dans la liste
        for (DatabaseModel billedService : queryResult) billedServices.put(billedService.getID(), (BilledService) billedService);
    }

    /**
     * Récupérer tous les clients de la base de données
     */
    public void retrieveDatabaseClients() {
        //Suppression des éléments de la liste
        clients.clear();
        //Exécution de la requête pour récupérer les données
        queryResult = selectQuery(DatabaseModel.Tables.CLIENTS);
        //Ajout des éléments dans la liste
        for (DatabaseModel client : queryResult) clients.put(client.getID(), (Client) client);
    }

    /**
     * Récupérer tous les hôtels de la base de données
     */
    public void retrieveDatabaseHotels() {
        //Suppression des éléments de la liste
        hotels.clear();
        //Exécution de la requête pour récupérer les données
        queryResult = selectQuery(DatabaseModel.Tables.HOTELS);
        //Ajout des éléments dans la liste
        for (DatabaseModel hotel : queryResult) hotels.put(hotel.getID(), (Hotel) hotel);
    }

    /**
     * Récupérer tous les occupants de la base de données
     */
    public void retrieveDatabaseOccupants() {
        //Suppression des éléments de la liste
        occupants.clear();
        //Exécution de la requête pour récupérer les données
        queryResult = selectQuery(DatabaseModel.Tables.OCCUPANTS);
        //Ajout des éléments dans la liste
        for (DatabaseModel occupant : queryResult) occupants.put(occupant.getID(), (Occupant) occupant);
    }

    /**
     * Récupérer toutes les occupations de la base de données
     */
    public void retrieveDatabaseOccupations() {
        //Suppression des éléments de la liste
        occupations.clear();
        //Exécution de la requête pour récupérer les données
        queryResult = selectQuery(DatabaseModel.Tables.OCCUPATIONS);
        //Ajout des éléments dans la liste
        for (DatabaseModel occupation : queryResult) occupations.put(occupation.getID(), (Occupation) occupation);
    }

    /**
     * Récupérer toutes les réservations de la base de données
     */
    public void retrieveDatabaseReservations() {
        //Suppression des éléments de la liste
        reservations.clear();
        //Exécution de la requête pour récupérer les données
        queryResult = selectQuery(DatabaseModel.Tables.RESERVATIONS);
        //Ajout des éléments dans la liste
        for (DatabaseModel reservation : queryResult) reservations.put(reservation.getID(), (Reservation) reservation);
    }

    /**
     * Récupérer toutes les chambres de la base de données
     */
    public void retrieveDatabaseRooms() {
        //Suppression des éléments de la liste
        rooms.clear();
        //Exécution de la requête pour récupérer les données
        queryResult = selectQuery(DatabaseModel.Tables.ROOMS);
        //Ajout des éléments dans la liste
        for (DatabaseModel room : queryResult) rooms.put(room.getID(), (Room) room);
    }

    /**
     * Récupérer tous les types de chambre de la base de données
     */
    public void retrieveDatabaseRoomTypes() {
        //Suppression des éléments de la liste
        roomTypes.clear();
        //Exécution de la requête pour récupérer les données
        queryResult = selectQuery(DatabaseModel.Tables.ROOMTYPES);
        //Ajout des éléments dans la liste
        for (DatabaseModel room_type : queryResult) roomTypes.put(room_type.getID(), (RoomType) room_type);
    }

    /**
     * Récupérer tous les services de la base de données
     */
    public void retrieveDatabaseServices() {
        //Suppression des éléments de la liste
        services.clear();
        //Exécution de la requête pour récupérer les données
        queryResult = selectQuery(DatabaseModel.Tables.SERVICES);
        //Ajout des éléments dans la liste
        for (DatabaseModel service : queryResult) services.put(service.getID(), (Service) service);
    }

    /**
     * Récupérer un modèle référence à partir de son ID
     * @param table table
     * @param ID ID
     * @return élément référencé
     */
    public DatabaseModel getReferenceFromID(DatabaseModel.Tables table, int ID) {
        //Élément récupéré
        DatabaseModel item;
        //En fonction de la table
        switch (table) {
            case BILLS:
                //Récupération de l'élément dans la table
                item = bills.get(ID);
                //Retour de l'élément si existant ou récupération depuis la BDD si introuvable
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

    public ObservableMap<Integer, Bill> getBills() { return bills; }

    public ObservableMap<Integer, BilledService> getBilledServices() { return billedServices; }

    public ObservableMap<Integer, Client> getClients() { return clients; }

    public ObservableMap<Integer, Hotel> getHotels() { return hotels; }

    public ObservableMap<Integer, Occupant> getOccupants() { return occupants; }

    public ObservableMap<Integer, Occupation> getOccupations() { return occupations; }

    public ObservableMap<Integer, Reservation> getReservations() { return reservations; }

    public ObservableMap<Integer, Room> getRooms() { return rooms; }

    public ObservableMap<Integer, RoomType> getRoomTypes() { return roomTypes; }

    public ObservableMap<Integer, Service> getServices() { return services; }

    /**
     * Retourner la taille des éléments récupérés depuis la requête SQL
     * @return taille des résultats
     */
    public int getLastQueryResultSize() { return this.queryResult.size(); }
}
