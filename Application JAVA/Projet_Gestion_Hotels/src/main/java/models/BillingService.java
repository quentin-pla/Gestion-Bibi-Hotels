package models;

import database.DatabaseData;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collection;

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
    private ObservableList<Bill> pending_bills;

    /**
     * Archives des factures
     */
    private ArrayList<Bill> archives;

    /**
     * Hôtel lié au service
     */
    private Hotel hotel;

    /**
     * Instance unique
     */
    private static BillingService instance = null;

    /**
     * Récupérer l'instance unique
     */
    public static BillingService getInstance(Hotel hotel) {
        //Si l'instance n'est pas initialisée
        if (instance == null)
            //Initialisation de l'instance
            instance = new BillingService(hotel);
        else {
            //Définition de l'hotel
            if (!instance.hotel.equals(hotel)) instance.hotel = hotel;
            //Initialisation des factures
            instance.initBills();
        }
        //Retour de l'instance
        return instance;
    }

    /**
     * Constructeur
     */
    private BillingService(Hotel hotel) {
        this.hotel = hotel;
        this.archives = new ArrayList<>();
        this.pending_bills = FXCollections.observableArrayList();
        //Initialisation des factures
        initBills();
        //Ajout d'un listener sur la liste des factures des données locales afin d'être toujours à jour
        DatabaseData.getInstance().getBills().addListener((MapChangeListener<Integer, Bill>) change -> {
            //Si des éléments ont été ajoutés
            if (change.wasAdded() && !change.getMap().isEmpty())
                //Si la taille de la map du listener est égale à la taille totale des factures mises à jour
                if (DatabaseData.getInstance().getBills_update_size() == change.getMap().values().size())
                    //Initialisation des factures
                    filterBills(new ArrayList<>(change.getMap().values()));
        });
    }

    /**
     * Initialiser les factures pour l'hotel spécifié
     */
    private void initBills() {
        //Initialisation des factures
        filterBills(DatabaseData.getInstance().getBills().values());
    }

    /**
     * Filtrer les factures depuis les données locales
     */
    public void filterBills(Collection<Bill> items) {
        //Suppression de la liste des archives
        archives.clear();
        //Réservations filtrées
        ArrayList<Bill> filtered = new ArrayList<>();
        //Pour chaque facture
        for (Bill bill : items)
            //Si la facture appartient bien à l'hotel
            if (bill.getReservation().getHOTEL_ID() == hotel.getID())
                //Si elle est archivée on l'ajoute aux archives
                if (bill.getIS_ARCHIVED()) archives.add(bill);
                //Ajout de la facture à la liste filtrée
                else filtered.add(bill);
        //Définition des factures
        pending_bills.setAll(filtered);
    }

    /**
     * Confirmer le paiement de la facture
     * @param bill facture
     */
    public void confirmPayment(Bill bill) {
        //Passage du booléen confirmée à vrai
        bill.setIS_PAYED(true);
        //Mise à jour dans la base de données
        bill.updateColumn(Bill.Columns.IS_PAYED);
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
        //Passage du booléen archivée à vrai
        bill.setIS_ARCHIVED(true);
        //Mise à jour dans la base de données
        bill.updateColumn(Bill.Columns.IS_ARCHIVED);
    }

    /**
     * Récupérer le montant total de la facture
     * @param bill facture
     */
    public void calculateTotalBillAmount(Bill bill) {
        //Récupération des occupations liées à la réservation
        ArrayList<Occupation> occupations = ReservationService.getInstance(hotel).getReservationOccupations(bill.getRESERVATION_ID());
        //Montant total de la facture
        double total_amount = 0.0;
        //Pour chaque occupation
        for (Occupation occupation : occupations) {
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
        }
        //Mise à jour du montant de la facture
        bill.setAMOUNT(total_amount);
        //Mise à jour dans la base de données
        bill.updateColumn(Bill.Columns.AMOUNT);
    }

    //************* GETTERS & SETTERS ***************//

    public ObservableList<Bill> getPending_bills() { return pending_bills; }

    public Hotel getHotel() {
        return hotel;
    }

    public ArrayList<Bill> getArchives() {
        return archives;
    }


}
