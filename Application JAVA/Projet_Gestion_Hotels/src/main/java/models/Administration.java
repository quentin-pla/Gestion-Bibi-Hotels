package models;

import database.DatabaseData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Administration {
    /**
     * Hotels disponibles
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
                //Nom du type de chambre
                String room_type_name = bill.getReservation().getRoomType().getNAME();
                //Incrémentation de 1 dans le ratio pour le type de chambre
                roomTypesRatio.put(room_type_name,roomTypesRatio.get(room_type_name) + 1);
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
    public Map<Hotel,ArrayList<Bill>> getBilledAmounts() {
        //Initialisation d'une map
        Map<Hotel,ArrayList<Bill>> results = new HashMap<>();
        //Pour chaque hotel
        for (Hotel hotel : hotels)
            //Ajout des factures
            results.put(hotel, BillingService.getInstance(hotel).getArchives());
        //Retour des résultats
        return results;
    }

    public ArrayList<Hotel> getHotels() {
        return hotels;
    }
}
