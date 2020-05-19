import models.*;

import java.util.Calendar;
import java.util.Date;

public abstract class TestModels {
    //Liste des modèles de test
    public static Bill bill;
    public static Client client;
    public static Hotel hotel;
    public static Occupant occupant;
    public static Occupation occupation;
    public static Reservation reservation;
    public static Room room;
    public static RoomType roomType;
    public static Service service;
    public static BilledService billedService;

    /**
     * Insertion des modèles de test
     */
    public static void insertModels() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DATE, -10);
        Date arrival_date = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date exit_date = cal.getTime();

        hotel = new Hotel("test","test","test",3);
        client = new Client("test","test","test","test","test","test");
        roomType = new RoomType("test",100.00,2,false,false);
        room = new Room(hotel.getID(), roomType.getID());
        reservation = new Reservation(client.getID(), hotel.getID(), roomType.getID(), arrival_date, exit_date, 1, 1,1,false,false,false,false);
        occupation = new Occupation(reservation.getID(), room.getID(), false);
        occupant = new Occupant(occupation.getID(), "test", "test");
        service = new Service(hotel.getID(), "test", 100.00, false);
        bill = new Bill(reservation.getID(), client.getID(), 0.00, false, false);
        billedService = new BilledService(occupation.getID(), service.getID(), false);
    }

    /**
     * Initialiser les services
     */
    public static void initServices() {
        Administration.getInstance().initHotels();
        BillingService.getInstance(hotel).initBills();
        ClientService.getInstance().initOccupations();
        ReservationService.getInstance(hotel).initReservations();
    }

    /**
     * Suppression des modèles de test de la base de données
     */
    public static void deleteModels() {
        billedService.delete();
        bill.delete();
        service.delete();
        occupant.delete();
        occupation.delete();
        reservation.delete();
        room.delete();
        roomType.delete();
        client.delete();
        hotel.delete();
        billedService = null;
        bill = null;
        service = null;
        occupant = null;
        occupation = null;
        reservation = null;
        room = null;
        roomType = null;
        client = null;
        hotel = null;
    }
}
