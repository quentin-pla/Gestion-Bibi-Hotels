package models;

import database.DatabaseData;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;

import java.util.*;

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
        //Si l'instance n'est pas initialisée
        if (instance == null)
            //Initialisation de l'instance
            instance = new ReservationService(hotel);
       else {
            //Définition de l'hotel
            if (!instance.hotel.equals(hotel)) instance.hotel = hotel;
            //Initialisation des réservations
            instance.initReservations();
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
            if (change.wasAdded() && !change.getMap().isEmpty())
                //Si la taille de la map du listener est égale à la taille totale des réservations mises à jour
                if (DatabaseData.getInstance().getReservations_update_size() == change.getMap().values().size())
                    //Initialisation des réservations
                    filterReservations(new ArrayList<>(change.getMap().values()));
        });
    }

    /**
     * Initialiser les réservations pour l'hotel spécifié
     */
    private void initReservations() {
        //Initialisation des réservations
        filterReservations(DatabaseData.getInstance().getReservations().values());
    }

    /**
     * Filtrer les réservations depuis les données locales
     */
    public void filterReservations(Collection<Reservation> items) {
        //Suppression des éléments contenus dans les archives
        archives.clear();
        //Réservations filtrées
        ArrayList<Reservation> filtered = new ArrayList<>();
        //Pour chaque réservation
        for (Reservation reservation : items)
            //Si l'id de l'hotel est égal à celui du service réservation
            if (reservation.getHOTEL_ID() == hotel.getID())
                //Si la réservation est archivée où que la date de soritie est supérieure à aujourd'hui, on l'ajoute aux archives
                if (reservation.getIS_ARCHIVED()) archives.add(reservation);
                //Ajout de la réservation à la liste filtrée
                else filtered.add(reservation);
        //Définition des réservations
        reservations.setAll(filtered);
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