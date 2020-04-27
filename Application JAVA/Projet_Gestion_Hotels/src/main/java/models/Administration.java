package models;

import java.util.ArrayList;

public class Administration {
    /**
     * Hotels disponibles
     */
    public ArrayList<Hotel> hotels;

    /**
     * Constructeur
     */
    private Administration() {
         this.hotels = new ArrayList<>();
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

    public void getAvailableBilledRoomRatio(int room_type_id) {
        //
    }

    public void getBilledAmounds(int hotel_id) {
        //
    }
}
