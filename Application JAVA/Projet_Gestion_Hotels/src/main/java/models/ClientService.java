package models;

public class ClientService {
    /**
     * Hôtel lié au service
     */
    private Hotel hotel;

    /**
     * Constructeur
     * @param hotel hotel lié
     */
    public ClientService(Hotel hotel) {
        this.hotel = hotel;
    }
}
