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
    public Administration() {
         this.hotels = new ArrayList<>();
    }

    /**
     * Ajouter un nouvel hotel à la chaine
     * @return hotel
     */
    public Hotel buildHotel(String name, String street, String city, int star_rating) {
        //Instanciation d'un nouvel hotel
        Hotel hotel = new Hotel(name, street, city, star_rating);
        //Ajout de l'hotel à la liste de ceux qu'elle administre
        hotels.add(hotel);
        //Retour de l'hotel
        return hotel;
    }
}
