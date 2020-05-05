package models;

import database.DatabaseData;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class ReservationService {
    /**
     * Hôtel lié au service
     */
    private Hotel hotel;

    /**
     * Réservations de l'hotel
     */
    private ObservableList<Reservation> reservations;

    /**
     * Réservations archivées
     */
    private ArrayList<Reservation> archives;

    /**
     * Instance unique
     */
    private static ReservationService instance = null;

    /**
     * Récupérer l'instance unique
     */
    public static ReservationService getInstance(Hotel hotel) {
        //Si l'instance n'est pas initialisée, initialisation
        if (instance == null) instance = new ReservationService(hotel);
        else {
           //Si l'hotel de l'instance est différent
            if (!instance.hotel.equals(hotel)) {
                //Définition de l'hotel
                instance.hotel = hotel;
                //Initialisation des réservations
                instance.initReservations();
            }
        }
        //Retour de l'instance
        return instance;
    }

    /**
     * Constructeur
     */
    private ReservationService(Hotel hotel) {
        this.hotel = hotel;
        this.archives = new ArrayList<>();
        this.reservations = FXCollections.observableArrayList();
        //Initialisation des réservations
        initReservations();
        //Ajout d'un listener sur la liste des réservations des données locales afin d'être toujours à jour
        DatabaseData.getInstance().getReservations().addListener((MapChangeListener<Integer, Reservation>) change -> {
            //Si des éléments ont été ajoutés
            if (change.wasAdded() && change.getMap().size() == DatabaseData.getInstance().getLastQueryResultSize())
                //Initialisation des réservations
                filterReservations(new ArrayList<>(change.getMap().values()));
        });
    }

    /**
     * Initialiser les réservations pour l'hotel spécifié
     */
    public void initReservations() {
        //Suppression des réservations
        reservations.clear();
        //Initialisation des réservations
        filterReservations(DatabaseData.getInstance().getReservations().values());
    }

    /**
     * Filtrer une liste de réservations
     * @param items éléments
     */
    public void filterReservations(Collection<Reservation> items) {
        //Suppression de la liste des archives
        archives.clear();
        //Pour chaque élément
        for (Reservation reservation : items) {
            //Si l'id de l'hotel est égal à celui du service réservation
            if (reservation.getHOTEL_ID() == hotel.getID())
                //Si la réservation est archivée où que la date de soritie est supérieure à aujourd'hui, on l'ajoute aux archives
                if (reservation.getIS_ARCHIVED()) archives.add(reservation);
                //Ajout de la réservation si elle n'est pas déjà contenue
                else if (!isAlreadyContained(reservation)) reservations.add(reservation);
        }
        //Suppression des réservations supprimées de la base de données
        retainReservations();
    }

    /**
     * Vérifier si une réservation est déjà contenue dans reservations
     * @param reservation réservation à vérifier
     * @return booléen
     */
    private boolean isAlreadyContained(Reservation reservation) {
        //Pour chaque réservation
        for (Reservation element : reservations)
            //Comparaison de l'élément
            if (element.getID() == reservation.getID()) {
                //Mise à jour de l'élément
                element.updateFromModel(reservation);
                return true;
            }
        return false;
    }

    /**
     * Supprimer les réservations supprimées de la base de données
     */
    private void retainReservations() {
        //Liste contenant les réservations à supprimer
        ArrayList<Reservation> itemsToRemove = new ArrayList<>();
        //Liste des IDs des réservations contenues dans les archives
        ArrayList<Integer> archivesIDs = new ArrayList<>();
        //Ajout de l'ID de chaque réservation contenue
        archives.forEach(reservation -> archivesIDs.add(reservation.getID()));
        //Liste des IDs des réservations locales provenant de la base de données
        ArrayList<Integer> localReservationsIDs = new ArrayList<>(DatabaseData.getInstance().getReservations().keySet());
        //Pour chaque réservation
        for (Reservation reservation : reservations)
            //Si la réservation n'est pas contenue dans celles locales
            if (!localReservationsIDs.contains(reservation.getID()) || archivesIDs.contains(reservation.getID()))
                //Suppression de la réservation
                itemsToRemove.add(reservation);
        //Suppression de toutes les réservations en trop
        reservations.removeAll(itemsToRemove);
    }

    /**
     * Récupérer la date actuelle en ajoutant le nombre de jour souhaité
     * @param days_to_add nombre de jours à ajouter
     * @return date
     */
    private Date getCurrentDate(int days_to_add) {
        //Récupération de l'instance du calendrier
        Calendar cal = Calendar.getInstance();
        //Heure à minuit
        cal.set(Calendar.HOUR_OF_DAY, 0);
        //Ajout de deux jours à la date
        cal.add(Calendar.DATE,days_to_add);
        //Retour de la date
        return cal.getTime();
    }

    /**
     * Enregistrer une réservation
     * @param reservation réservation
     */
    public boolean registerReservation(Reservation reservation) {
        //Vérifier que la réservation soit bien effectuée 2 jours avant la date d'arrivée
        boolean checkArrivalDate = !getCurrentDate(2).after(reservation.getARRIVAL_DATE());
        //Création de/des occupation(s) pour la réservation
        if (checkArrivalDate && ClientService.getInstance().addOccupation(reservation)) {
            //Ajout de la réservation à la liste
            reservations.add(reservation);
            //Retourne vrai
            return true;
        }
        return false;
    }

    /**
     * Archiver une réservation
     * @param reservation reservation
     */
    public void archiveReservation(Reservation reservation) {
        //Archivage de la reservation
        archives.add(reservation);
        //Suppression de la reservation
        reservations.remove(reservation);
        //Passage du booléen archivée à vrai
        reservation.setIS_ARCHIVED(true);
        //Mise à jour dans la base de données
        reservation.updateColumn(Reservation.Columns.IS_ARCHIVED);
    }

    /**
     * Récupérer la liste des occupations pour une réservation
     * @param reservation_id id réservation
     * @return liste des occupations
     */
    public ArrayList<Occupation> getReservationOccupations(int reservation_id) {
        //Liste des occupations liées
        ArrayList<Occupation> linkedOccupations = new ArrayList<>();
        //Pour chaque occupation
        for (Occupation occupation : DatabaseData.getInstance().getOccupations().values())
            //Si l'id de réservation correspond
            if (occupation.getRESERVATION_ID() == reservation_id)
                //Ajout de l'occupation dans les résultats
                linkedOccupations.add(occupation);
        //Retour des résultats
        return linkedOccupations;
    }

    /**
     * Annuler une réservation
     * @param reservation réservation
     */
    public boolean cancelReservation(Reservation reservation) {
        //Vérifier que la réservation soit bien effectuée 2 jours avant la date d'arrivée
        boolean checkArrivalDate = !getCurrentDate(2).after(reservation.getARRIVAL_DATE());
        //Si la date est valide
        if (checkArrivalDate) {
            //Passage du booléen confirmée à vrai
            reservation.setIS_CANCELLED(true);
            //Mise à jour dans la base de données
            reservation.updateColumn(Reservation.Columns.IS_CANCELLED);
            //Suppression de la réservation dans la liste
            reservations.remove(reservation);
            //Ajout de la réservation dans les archives
            archives.add(reservation);
            //Retourne vrai
            return true;
        }
        //Retourne faux
        return false;
    }

    /**
     * Confirmer l'arrivée d'un client
     * @param reservation réservation
     */
    public void confirmArrival(Reservation reservation) {
        //Confirmation de l'arrivée du client
        ClientService.getInstance().confirmClientPresence(reservation);
        //Confirmation de la réservation
        confirmReservation(reservation);
    }

    /**
     * Confirmer le versement des arrhes
     * @param reservation reservation
     */
    public void confirmPayment(Reservation reservation) {
        //Passage du booléen confirmée à vrai
        reservation.setIS_PAYED(true);
        //Mise à jour dans la base de données
        reservation.updateColumn(Reservation.Columns.IS_PAYED);
    }

    /**
     * Confirmer une réservation
     * @param reservation réservation
     */
    public void confirmReservation(Reservation reservation) {
        //Passage du booléen confirmée à vrai
        reservation.setIS_COMFIRMED(true);
        //Mise à jour dans la base de données
        reservation.updateColumn(Reservation.Columns.IS_COMFIRMED);
    }

    //************* GETTERS & SETTERS ***************//

    public ArrayList<Reservation> getArchives() { return archives; }

    public ObservableList<Reservation> getReservations() { return reservations; }

    public Hotel getHotel() { return hotel; }

    public void setHotel(Hotel hotel) { this.hotel = hotel; }
}