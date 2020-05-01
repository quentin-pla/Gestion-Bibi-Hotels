package models;

import database.DatabaseData;

import java.util.*;

public class ReservationService {
    /**
     * Hôtel lié au service
     */
    private Hotel hotel;

    /**
     * Réservations de l'hotel
     */
    private ArrayList<Reservation> reservations;

    /**
     * Réservations archivées
     */
    private ArrayList<Reservation> archives;

    /**
     * Constructeur
     * @param hotel hotel lié
     */
    public ReservationService(Hotel hotel) {
        this.hotel = hotel;
        this.reservations = new ArrayList<>();
        this.archives = new ArrayList<>();
    }

    /**
     * Récupérer les réservations depuis les données locales
     */
    public void initReservations() {
        //Récupération des réservations depuis les données de la base de données
        Collection<Reservation> data = DatabaseData.getInstance().getReservations().values();
        //Pour chaque réservation
        for (Reservation reservation : data)
            //Si l'id de l'hotel est égal à celui du service réservation
            if (reservation.getHOTEL_ID() == hotel.getID())
                //Si la réservation est archivée on l'ajoute aux archives
                if (reservation.getIS_ARCHIVED()) archives.add(reservation);
                //Sinon on l'ajoute dans les réservations
                else reservations.add(reservation);
            }
    }

    /**
     * Rafraichir la liste des réservations et archives
     */
    public void refreshReservations() {
        //Suppression de la liste des réservations
        reservations.clear();
        //Suppression de la liste des archives
        archives.clear();
        //Ajout des réservations et archives
        initReservations();
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

    public ArrayList<Reservation> getReservations() { return reservations; }

    public Hotel getHotel() { return hotel; }

    public void setReservations(ArrayList<Reservation> reservations) { this.reservations = reservations; }

    public void setArchives(ArrayList<Reservation> archives) { this.archives = archives; }
}
