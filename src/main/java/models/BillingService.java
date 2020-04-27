package models;

import java.util.ArrayList;

public class BillingService {
    /**
     * Réduction pour les groupes en pourcentages
     */
    private final int groupDiscount = 10;

    /**
     * Réduction pour les clients réguliers en pourcentages
     */
    private final int regularClientDiscount = 20;

    /**
     * Liste des factures en attente de paiement
     */
    private ArrayList<Bill> pending_bills;

    /**
     * Archives des factures
     */
    private ArrayList<Bill> archives;

    /**
     * Hôtel lié au service
     */
    private Hotel hotel;

    /**
     * Constructeur
     * @param hotel hotel lié
     */
    public BillingService(Hotel hotel) {
        this.hotel = hotel;
    }

    /**
     * Créer une facture
     * @param reservation reservation
     */
    public void createBill(Reservation reservation) {
        //Récupération des occupations liées à la réservation
        ArrayList<Occupation> occupations = ClientService.getInstance().getReservationOccupations(reservation.getID());
        //Montant total de la facture
        double total_amount = 0.0;
        //Pour chaque occupation
        for (Occupation occupation : occupations)
            //Incrémentation du montant total
            total_amount += getTotalBillAmount(occupation);
        //Création d'une nouvelle facture
        pending_bills.add(new Bill(reservation.getCLIENT_ID(), total_amount));
    }

    /**
     * Archiver une facture
     * @param bill facture
     */
    public void archiveBill(Bill bill) {
        //Archivage de la facture
        archives.add(bill);
        //Suppression de la facture dans les factures en attente
        pending_bills.remove(bill);
    }

    /**
     * Récupérer le montant total de la facture
     * @param occupation occupation
     * @return montant total
     */
    public double getTotalBillAmount(Occupation occupation) {
        //Montant total
        double total_amount = 0.0;
        //Récupération du type de la chambre
        RoomType roomType = occupation.getRoom().getRoomType();
        //Ajout du montant du type de chambre au montant total pour le nombre de nuits passé
        total_amount += roomType.getPRICE() * occupation.getReservation().getDURATION();
        //Ajout du montant des services facturés
        for (Service service : occupation.getBilledServices())
            total_amount += service.getPRICE();
        //Réduction en fonction du nombre de personnes (isolé/groupe)
        if (occupation.getReservation().getPEOPLE_COUNT() > 1) total_amount -= (total_amount * groupDiscount)/100;
        //Réduction en fonction du status du client (régulier ou pas)
        if (occupation.getReservation().getClient().getIS_REGULAR())
            total_amount -= (total_amount * regularClientDiscount)/100;
        //Retour du montant total
        return total_amount;
    }

    //************* GETTERS & SETTERS ***************//

    public ArrayList<Bill> getPending_bills() {
        return pending_bills;
    }

    public ArrayList<Bill> getArchives() {
        return archives;
    }
}
