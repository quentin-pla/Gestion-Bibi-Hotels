package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
     * Constructeur
     * @param hotel hotel lié
     */
    public ReservationService(Hotel hotel) {
        this.hotel = hotel;
        this.reservations = new ArrayList<>();
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
        if (checkArrivalDate && getClientService().addOccupation(reservation)) {
            //Ajout de la réservation à la liste
            reservations.add(reservation);
            //Retourne vrai
            return true;
        }
        return false;
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
            reservation.setIS_CANCELLED("true");
            //Mise à jour dans la base de données
            reservation.updateColumn(Reservation.Columns.IS_CANCELLED);
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
        getClientService().confirmClientPresence(reservation);
    }

    /**
     * Confirmer le versement des arrhes
     * @param reservation reservation
     */
    public void confirmPayment(Reservation reservation) {
        //Passage du booléen confirmée à vrai
        reservation.setIS_PAYED("true");
        //Mise à jour dans la base de données
        reservation.updateColumn(Reservation.Columns.IS_PAYED);
    }

    /**
     * Confirmer une réservation
     * @param reservation réservation
     */
    public void confirmReservation(Reservation reservation) {
        //Passage du booléen confirmée à vrai
        reservation.setIS_COMFIRMED("true");
        //Mise à jour dans la base de données
        reservation.updateColumn(Reservation.Columns.IS_COMFIRMED);
    }

    //************* GETTERS & SETTERS ***************//

    /**
     * Récupérer l'instance du service client
     * @return service client
     */
    public ClientService getClientService() {
        return ClientService.getInstance();
    }
}
