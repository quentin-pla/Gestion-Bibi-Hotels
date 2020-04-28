package models;

import database.DatabaseData;
import database.DatabaseModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static database.DatabaseConnection.getAvailableRoomsQuery;
import static database.DatabaseConnection.selectQuery;

public class ClientService {
    /**
     * Liste des occupations
     */
    private ArrayList<Occupation> occupations;

    /**
     * Archives des occupations
     */
    private ArrayList<Occupation> archives;

    /**
     * Constructeur
     */
    private ClientService() {
        this.occupations = new ArrayList<>();
        this.archives = new ArrayList<>();
    }

    /**
     * Récupérer les occupations depuis les données locales
     */
    public void initOccupations() {
        //Récupération des occupations depuis les données de la base de données
        Collection<Occupation> data = DatabaseData.getInstance().getOccupations().values();
        //Pour chaque occupation
        for (Occupation occupation : data)
            //Si elle est archivée on l'ajoute aux archives
            if (occupation.getIS_ARCHIVED()) archives.add(occupation);
                //Sinon on l'ajoute dans les occupations
            else occupations.add(occupation);
    }

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

    public ArrayList<Occupation> getReservationOccupations(int reservation_id) {
        //Liste des occupations liées
        ArrayList<Occupation> linkedOccupations = new ArrayList<>();
        //Pour chaque occupation
        for (Occupation occupation : occupations)
            //Si l'id de réservation correspond
            if (occupation.getRESERVATION_ID() == reservation_id)
                //Ajout de l'occupation dans les résultats
                linkedOccupations.add(occupation);
        //Retour des résultats
        return linkedOccupations;
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
    }

    //*************** GETTERS & SETTERS ***************//

    public ArrayList<Occupation> getOccupations() {
        return occupations;
    }

    public ArrayList<Occupation> getArchives() {
        return archives;
    }

    public void setOccupations(ArrayList<Occupation> occupations) {
        this.occupations = occupations;
    }

    public void setArchives(ArrayList<Occupation> archives) {
        this.archives = archives;
    }
}
