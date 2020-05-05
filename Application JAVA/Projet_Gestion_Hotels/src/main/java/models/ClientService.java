package models;

import database.DatabaseData;
import database.DatabaseModel;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static database.DatabaseConnection.getAvailableRoomsQuery;
import static database.DatabaseConnection.selectQuery;

public class ClientService {
    /**
     * Liste des occupations
     */
    private ObservableList<Occupation> occupations;

    /**
     * Archives des occupations
     */
    private ArrayList<Occupation> archives;

    /**
     * Instance unique
     */
    private static ClientService instance = null;

    /**
     * Récupérer l'instance unique
     */
    public static ClientService getInstance() {
        //Si l'instance n'est pas initialisée
        if (instance == null)
            //Initialisation de l'instance
            instance = new ClientService();
        //Retour de l'instance
        return instance;
    }

    /**
     * Constructeur
     */
    private ClientService() {
        this.archives = new ArrayList<>();
        this.occupations = FXCollections.observableArrayList();
        //Initialisation des occupations
        initOccupations();
        //Ajout d'un listener sur la liste des occupations des données locales afin d'être toujours à jour
        DatabaseData.getInstance().getOccupations().addListener((MapChangeListener<Integer, Occupation>) change -> {
            //Si des éléments ont été ajoutés
            if (change.wasAdded() && change.getMap().size() == DatabaseData.getInstance().getLastQueryResultSize())
                //Initialisation des occupations
                filterOccupations(new ArrayList<>(change.getMap().values()));
        });
    }

    /**
     * Récupérer les occupations depuis les données locales
     */
    public void initOccupations() {
        //Suppression des occupations
        occupations.clear();
        //Initialisation des occupations
        filterOccupations(DatabaseData.getInstance().getOccupations().values());
    }

    /**
     * Filtrer une liste d'occupations
     * @param items éléments
     */
    public void filterOccupations(Collection<Occupation> items) {
        //Suppression des archives
        archives.clear();
        //Pour chaque occupation
        for (Occupation occupation : items) {
            //Si elle est archivée on l'ajoute aux archives
            if (occupation.getIS_ARCHIVED()) archives.add(occupation);
                //Sinon on l'ajoute dans les occupations si elle n'est pas déjà contenue
            else if (!isAlreadyContained(occupation)) occupations.add(occupation);
        }
        //Suppression des occupations supprimées de la base de données
        retainOccupations();
    }

    /**
     * Vérifier si une occupation est déjà contenue dans occupations
     * @param occupation occupation à vérifier
     * @return booléen
     */
    private boolean isAlreadyContained(Occupation occupation) {
        //Pour chaque occupation
        for (Occupation element : occupations)
            //Comparaison de l'élément
            if (element.compareTo(occupation)) return true;
        return false;
    }

    /**
     * Supprimer les occupations supprimées de la base de données
     */
    private void retainOccupations() {
        //Liste contenant les occupations à supprimer
        ArrayList<Occupation> itemsToRemove = new ArrayList<>();
        //Liste des IDs des occupations locales provenant de la base de données
        ArrayList<Integer> localOccupationsIDs = new ArrayList<>(DatabaseData.getInstance().getOccupations().keySet());
        //Pour chaque occupation
        for (Occupation occupation : occupations)
            //Si l'occupation n'est pas contenue dans celles locales, on la supprime
            if (!localOccupationsIDs.contains(occupation.getID())) itemsToRemove.add(occupation);
        //Suppression de toutes les occupations en trop
        occupations.removeAll(itemsToRemove);
    }

    /**
     * Récupérer l'historique des occupations d'un client
     * @param client_id id du client
     * @return occupations
     */
    public ArrayList<Occupation> getClientHistory(int client_id) {
        //Historique
        ArrayList<Occupation> history = new ArrayList<>();
        //Pour chaque occupation dans les archives
        for (Occupation archive : archives)
            //Si l'archive est liée au client
            if (archive.getReservation().getCLIENT_ID() == client_id)
                //Ajout de l'archive dans l'historique
                history.add(archive);
        //Retour de l'historique
        return history;
    }

    /**
     * Récupérer la liste des services disponibles pour l'occupation
     * @param occupation occupation
     * @return liste de services
     */
    public ArrayList<Service> getOccupationServices(Occupation occupation) {
        //Liste des services disponibles
        ArrayList<Service> availableServices = new ArrayList<>();
        //Pour chaque service
        for (Service service : DatabaseData.getInstance().getServices().values())
            //Si l'id du service correspond à l'hotel de la chambre occupée
            if (service.getHOTEL_ID() == occupation.getRoom().getHOTEL_ID())
                //Si le service n'est pas unique ou que le service est unique mais pas encore facturé pour l'occupation
                if (!service.getUNIQUE_ORDER() || (service.getUNIQUE_ORDER() && !occupation.getBilledServices().contains(service)))
                    //Ajout du service dans les résultats
                    availableServices.add(service);
        //Retour des résultats
        return availableServices;
    }

    /**
     * Envoyer un mail au client
     * @param mail adresse mail
     */
    public void sendAdvertising(String mail) {
        System.out.println("Publicité envoyée à l'adresse mail " + mail);
    }

    /**
     * Affecter des chambres pour une réservation
     * @param reservation reservation
     */
    public boolean addOccupation(Reservation reservation) {
        //Vérification s'il reste des chambres disponibles pour la réservation
        List<Integer> results = getAvailableRoomsQuery(reservation);
        //Si le nombre de chambres restantes est inférieur au nombre de chambre voulu
        if (results.size() < reservation.getROOM_COUNT())
            //Retourne faux
            return false;
        //Récupération du nombre de chambres nécessaire en fonction du nombre de chambre voulu
        results = results.subList(0, reservation.getROOM_COUNT());
        //Pour chaque chambre
        for (Integer room_id : results)
            //Ajout d'une nouvelle occupation dans la liste des occupations
            occupations.add(new Occupation(reservation.getID(), room_id, false));
        //Retourne vrai
        return true;
    }

    /**
     * Confirmer la présence du client lors de son arrivée
     * @param reservation réservation
     */
    public void confirmClientPresence(Reservation reservation) {
        //Pour chaque occupations
        for (Occupation occupation : occupations) {
            //Si l'occupation est liée à la réservation
            if (occupation.getRESERVATION_ID() == reservation.getID()) {
                //Passage du booléen concernant la présence du client à vrai
                occupation.setIS_CLIENT_PRESENT(true);
                //Mise à jour dans la base de données
                occupation.updateColumn(Occupation.Columns.IS_CLIENT_PRESENT);
            }
        }
    }

    /**
     * Mettre à jour la présence du client dans sa chambre
     * @param occupation chambre occupée
     * @param value présence du client
     */
    public void updatePresence(Occupation occupation, boolean value) {
        //Passage du booléen à la valeur passée en paramètre
        occupation.setIS_CLIENT_PRESENT(value);
        //Mise à jour dans la base de données
        occupation.updateColumn(Occupation.Columns.IS_CLIENT_PRESENT);
    }

    /**
     * Facturer un service pour un client occupant une chambre
     * @param occupation chambre du client
     * @param service service à facturer
     */
    public void billService(Occupation occupation, Service service) {
        //Facturer le service au client occupant la chambre
        occupation.billService(service);
    }

    /**
     * Définir un client comme étant régulier
     * @param client_id id du client
     */
    public void setRegularClient(int client_id) {
        //Récupération du client depuis la base de données
        Client client = (Client) selectQuery(DatabaseModel.Tables.CLIENTS, client_id);
        //Passage du booléen à vrai
        client.setIS_REGULAR(true);
        //Mise à jour dans la base de données
        client.updateColumn(Client.Columns.IS_REGULAR);
    }

    /**
     * Archiver une occupation
     * @param occupation occupation
     */
    public void archiveOccupation(Occupation occupation) {
        //Archivage de l'occupation
        archives.add(occupation);
        //Suppression de l'occupation
        occupations.remove(occupation);
        //Passage du booléen archivée à vrai
        occupation.setIS_ARCHIVED(true);
        //Mise à jour dans la base de données
        occupation.updateColumn(Occupation.Columns.IS_ARCHIVED);
        //Client non présent
        occupation.setIS_CLIENT_PRESENT(false);
        //Mise à jour dans la base de données
        occupation.updateColumn(Occupation.Columns.IS_CLIENT_PRESENT);
    }

    //*************** GETTERS & SETTERS ***************//

    public ObservableList<Occupation> getOccupations() { return occupations; }

    public ArrayList<Occupation> getArchives() {
        return archives;
    }
}
