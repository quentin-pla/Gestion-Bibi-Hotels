package org.gestion_bibi_hotels.models;

import org.gestion_bibi_hotels.database.DatabaseData;

import java.util.*;

/**
 * Administration de la chaine d'hôtels
 */
public class Administration {
    /**
     * Hotels de la chaine
     */
    private ArrayList<Hotel> hotels;

    /**
     * Fréquence de récupération des infos sur la chaine d'hotels
     * Par défaut tous les 3 mois donc 90 jours
     */
    private final int informationsFrequency = 90;

    /**
     * Constructeur
     */
    private Administration() {
        this.hotels = new ArrayList<>();
    }

    /**
     * Initialisation des hotels
     */
    public void initHotels() {
        //Suppression de la liste des hotels
        hotels.clear();
        //Ajout des hotels à la liste
        hotels.addAll(DatabaseData.getInstance().getHotels().values());
    }

    /**
     * Instance unique
     */
    private static Administration instance = null;

    /**
     * Récupérer l'instance unique
     */
    public static Administration getInstance() {
        //Si l'instance n'est pas initialisée
        if (instance == null)
            //Initialisation de l'instance
            instance = new Administration();
        //Retour de l'instance
        return instance;
    }

    /**
     * Informations pour chaque type de confort, le nombre de nuits disponibles / nombre de nuits facturées
     * Retour sous forme de pourcentages
     */
    public Map<String,Double> getAvailableBilledRoomRatio() {
        //Récupération de l'instance du calendrier
        Calendar cal = Calendar.getInstance();
        //Heure à minuit
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        //Enlèvement de 3 mois à la date
        cal.add(Calendar.DATE,-informationsFrequency);
        //Ratio pour chaque type de chambre
        Map<String,Double> roomTypesRatio = new HashMap<>();
        //Récupération de tous les types de chambres
        Collection<RoomType> roomTypes = DatabaseData.getInstance().getRoomTypes().values();
        //Pour chaque type de chambre
        for (RoomType roomType : roomTypes)
            //Ajout du type de chambre dans les résultats
            roomTypesRatio.put(roomType.getNAME(),0.0);
        //Pour chaque hotel
        for (Hotel hotel : hotels) {
            //Pour chaque facture archivée de l'hotel
            for (Bill bill : BillingService.getInstance(hotel).getArchives()) {
                //Si la date de sortie de la réservation est comprise dans les 3 derniers mois et que la réservation n'est pas annulée
                if (cal.getTime().before(bill.getReservation().getEXIT_DATE()) && !bill.getReservation().getIS_CANCELLED()) {
                    //Nom du type de chambre
                    String room_type_name = bill.getReservation().getRoomType().getNAME();
                    //Incrémentation de 1 dans le ratio pour le type de chambre
                    roomTypesRatio.put(room_type_name, roomTypesRatio.get(room_type_name) + bill.getReservation().getDURATION());
                }
            }
        }
        //Pour chaque ratio division par le nombre de jours
        roomTypesRatio.replaceAll((t, v) -> (roomTypesRatio.get(t) / informationsFrequency) * 100);
        //Retour des ratios
        return roomTypesRatio;
    }

    /**
     * Factures de chaque chambre de chaque hotel
     */
    public Map<String,Double> getBilledAmounts() {
        //Initialisation d'une map
        Map<String,Double> results = new HashMap<>();
        //Pour chaque hotel
        for (Hotel hotel : hotels) {
            //Ajout de l'hotel dans les résultats
            results.put(hotel.getNAME(), 0.0);
            //Pour chaque facture archivée
            for (Bill bill : BillingService.getInstance(hotel).getArchives())
                //Si la réservation liée n'est pas annulée
                if (!bill.getReservation().getIS_CANCELLED())
                    //Ajout des factures
                    results.put(hotel.getNAME(), results.get(hotel.getNAME()) + bill.getAMOUNT());
        }
        //Retour des résultats
        return results;
    }

    /**
     * Nombre de services facturés pour chaque hotel
     */
    public Map<String,Integer> getBilledServicesAmount() {
        //Initialisation d'une map
        Map<String,Integer> results = new HashMap<>();
        //Pour chaque service facturé
        for (BilledService billedService : DatabaseData.getInstance().getBilledServices().values()) {
            //Si la réservation liée n'est pas annulée
            if (!billedService.getOccupation().getReservation().getIS_CANCELLED()) {
                //Si le service n'est pas contenu dans les résultats
                if (!results.containsKey(billedService.getService().getNAME()))
                    //Ajout du service dans les résultats
                    results.put(billedService.getService().getNAME(), 1);
                else
                    //Incrémentation du nombre de services facturés
                    results.put(billedService.getService().getNAME(), results.get(billedService.getService().getNAME()) + 1);
            }
        }
        //Retour des résultats
        return results;
    }

    //************* GETTERS & SETTERS ***************//

    public int getInformationsFrequency() { return informationsFrequency; }
}
