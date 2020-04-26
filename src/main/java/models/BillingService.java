package models;

public class BillingService {
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
}
