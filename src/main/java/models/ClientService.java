package models;

import database.DatabaseData;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;

import java.util.*;

import static database.DatabaseConnection.getAvailableRoomsQuery;

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
        //Récupération des occupations de la base de données
        DatabaseData.getInstance().retrieveDatabaseOccupations();
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
        //Récupération de l'instance du calendrier
        Calendar cal = Calendar.getInstance();
        //Heure du jour à minuit
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        //Pour chaque occupation
        for (Occupation occupation : items) {
            //Date d'arrivée de la réservation de l'occupation
            Date arrival_date = occupation.getReservation().getARRIVAL_DATE();
            //Date de départ de la réservation de l'occupation
            Date exit_date = occupation.getReservation().getEXIT_DATE();
            //Si la date actuelle est comprise entre la date d'arrivée et de départ
            if (!cal.getTime().before(arrival_date) && !cal.getTime().after(exit_date))
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
            if (element.getID() == occupation.getID()) {
                //Mise à jour de l'élément
                element.updateFromModel(occupation);
                return true;
            }
        return false;
    }

    /**
     * Supprimer les occupations supprimées de la base de données
     */
    private void retainOccupations() {
        //Liste contenant les occupations à supprimer
        ArrayList<Occupation> itemsToRemove = new ArrayList<>();
        //Liste des IDs des occupations contenues dans les archives
        ArrayList<Integer> archivesIDs = new ArrayList<>();
        //Ajout de l'ID de chaque occupation contenue
        archives.forEach(occupation -> archivesIDs.add(occupation.getID()));
        //Liste des IDs des occupations locales provenant de la base de données
        ArrayList<Integer> localOccupationsIDs = new ArrayList<>(DatabaseData.getInstance().getOccupations().keySet());
        //Pour chaque occupation
        for (Occupation occupation : occupations)
            //Si l'occupation n'est pas contenue dans celles locales
            if (!localOccupationsIDs.contains(occupation.getID()) || archivesIDs.contains(occupation.getID()))
                //Suppression de l'occupation
                itemsToRemove.add(occupation);
        //Suppression de toutes les occupations en trop
        occupations.removeAll(itemsToRemove);
    }

    /**
     * Récupérer l'historique des réservations d'un client
     * @param client client
     * @return réservations
     */
    public ArrayList<Reservation> getClientHistory(Client client) {
        //Historique
        ArrayList<Reservation> history = new ArrayList<>();
        //Pour chaque occupation dans les archives
        for (Reservation reservation : DatabaseData.getInstance().getReservations().values())
            //Si la réservation est liée au client et qu'elle est archivée
            if (reservation.getIS_ARCHIVED() && reservation.getCLIENT_ID() == client.getID())
                //Ajout de l'archive dans l'historique
                history.add(reservation);
        //Retour de l'historique
        return history;
    }

    /**
     * Récupérer la liste des services disponibles pour l'occupation
     * @param occupation occupation
     * @return liste de services
     */
    public ArrayList<Service> getAvailableServices(Occupation occupation) {
        //Liste des services disponibles
        ArrayList<Service> availableServices = new ArrayList<>();
        //Pour chaque service
        for (Service service : DatabaseData.getInstance().getServices().values())
            //Si l'id du service correspond à l'hotel de la chambre occupée
            if (service.getHOTEL_ID() == occupation.getRoom().getHOTEL_ID()) {
                //Si le service n'est pas unique ou que le service est unique mais pas encore facturé pour l'occupation
                if (!service.getUNIQUE_ORDER() || (service.getUNIQUE_ORDER() && !isServiceFactured(occupation, service)))
                    //Ajout du service dans les résultats
                    availableServices.add(service);
            }
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
     * Récupérer l'espace restant dans l'occupation
     * @param occupation occupation
     */
    public int getRemainingSize(Occupation occupation) {
        //Si le nombre d'occupants est inférieur au nombre de lits dans la chambre (un lit = deux personnes)
        return (occupation.getRoom().getRoomType().getBED_CAPACITY() * 2) - getOccupants(occupation).size();
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
        new BilledService(occupation.getID(), service.getID(),false);
    }

    /**
     * Définir un client comme étant régulier
     * @param client client
     */
    public void setRegularClient(Client client) {
        //Passage du booléen à vrai
        client.setIS_REGULAR(true);
        //Mise à jour dans la base de données
        client.updateColumn(Client.Columns.IS_REGULAR);
    }

    /**
     * Récupérer les occupants liés à une occupation
     * @param occupation occupation
     * @return liste d'occupants
     */
    public ArrayList<Occupant> getOccupants(Occupation occupation) {
        //Résultats
        ArrayList<Occupant> results = new ArrayList<>();
        //Pour chaque occupant de la base de données
        for (Occupant occupant : DatabaseData.getInstance().getOccupants().values())
            //Si l'id de l'occupation correspond avec celui de l'occupation passée en paramètre
            if (occupant.getOCCUPATION_ID() == occupation.getID()) results.add(occupant);
        //Retour des résultats
        return results;
    }

    /**
     * Récupérer les services facturés à une occupation
     * @param occupation occupation
     * @return liste des services facturés
     */
    public ArrayList<BilledService> getBilledServices(Occupation occupation) {
        //Résultats
        ArrayList<BilledService> results = new ArrayList<>();
        //Pour chaque service facturé de la base de données
        for (BilledService billedservice : DatabaseData.getInstance().getBilledServices().values())
            //Si l'id de l'occupation correspond avec celui de l'occupation passée en paramètre
            if (billedservice.getOCCUPATION_ID() == occupation.getID()) results.add(billedservice);
        //Retour des résultats
        return results;
    }

    public boolean isServiceFactured(Occupation occupation, Service service) {
        //Récupération des services facturés pour l'occupation
        ArrayList<BilledService> billedServices = getBilledServices(occupation);
        //Pour chaque service facturé
        for (BilledService billedService : billedServices)
            //Si l'id du service dans le service facturé correspond avec celui passé en paramètre, retourne vrai
            if (billedService.getSERVICE_ID() == service.getID()) return true;
        //Retourne faux
        return false;
    }

    //*************** GETTERS & SETTERS ***************//

    public ObservableList<Occupation> getOccupations() { return occupations; }

    public ArrayList<Occupation> getArchives() {
        return archives;
    }
}
