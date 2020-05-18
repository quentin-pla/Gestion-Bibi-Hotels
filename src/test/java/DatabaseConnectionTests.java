import database.DatabaseModel;
import de.saxsys.javafx.test.JfxRunner;
import junit.framework.TestCase;
import models.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static database.DatabaseConnection.*;

@RunWith(JfxRunner.class)
public class DatabaseConnectionTests extends TestCase {

    //Liste des modèles de test
    private static Bill             testBill;
    private static Client           testClient;
    private static Hotel            testHotel;
    private static Occupant         testOccupant;
    private static Occupation       testOccupation;
    private static Reservation      testReservation;
    private static Room             testRoom;
    private static RoomType         testRoomType;
    private static Service          testService;
    private static BilledService    testBilledService;
    //Booléen permettant de savoir si les modèles de test sont initialisés
    private static boolean initialized = false;

    /**
     * Insertion des modèles de test
     */
    @Before
    public void insertModels() {
        if (initialized) return;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DATE, -1000);
        Date arrival_date = cal.getTime();
        cal.add(Calendar.DATE, 3);
        Date exit_date = cal.getTime();

        testHotel = new Hotel("test","test","test",3);
        testClient = new Client("test","test","test","test","test","test");
        testRoomType = new RoomType("test",100.00,2,false,false);
        testRoom = new Room(testHotel.getID(), testRoomType.getID());
        testReservation = new Reservation(testClient.getID(), testHotel.getID(), testRoomType.getID(), arrival_date, exit_date, 3, 1,2,false,false,false,false);
        testOccupation = new Occupation(testReservation.getID(), testRoom.getID(), false);
        testOccupant = new Occupant(testOccupation.getID(), "test", "test");
        testService = new Service(testHotel.getID(), "test", 100.00, false);
        testBill = new Bill(testReservation.getID(), testClient.getID(), 100.00, false, false);
        testBilledService = new BilledService(testOccupation.getID(), testService.getID(), false);

        initialized = true;
    }

    /**
     * Suppression des modèles de test de la base de données
     */
    @AfterClass
    public static void deleteModels() {
        deleteQuery(testBilledService);
        deleteQuery(testBill);
        deleteQuery(testService);
        deleteQuery(testOccupant);
        deleteQuery(testOccupation);
        deleteQuery(testReservation);
        deleteQuery(testRoom);
        deleteQuery(testRoomType);
        deleteQuery(testClient);
        deleteQuery(testHotel);
    }

    //################ SELECT ALL #################

    /**
     * Récupérer tous les hotels de la base de données
     */
    @Test
    public void selectAllHotels() {
        ArrayList<DatabaseModel> queryResult = selectQuery(DatabaseModel.Tables.HOTELS);
        assertTrue(queryResult.size() > 0);
        boolean found = false;
        for (DatabaseModel element : queryResult)
            if (element.getAttributesData().equals(testHotel.getAttributesData())) found = true;
            else assertTrue(element instanceof Hotel);
        assertTrue(found);
    }

    /**
     * Récupérer tous les clients de la base de données
     */
    @Test
    public void selectAllClients() {
        ArrayList<DatabaseModel> queryResult = selectQuery(DatabaseModel.Tables.CLIENTS);
        assertTrue(queryResult.size() > 0);
        boolean found = false;
        for (DatabaseModel element : queryResult)
            if (element.getAttributesData().equals(testClient.getAttributesData())) found = true;
            else assertTrue(element instanceof Client);
        assertTrue(found);
    }

    /**
     * Récupérer tous les types de chambre de la base de données
     */
    @Test
    public void selectAllRoomTypes() {
        ArrayList<DatabaseModel> queryResult = selectQuery(DatabaseModel.Tables.ROOMTYPES);
        assertTrue(queryResult.size() > 0);
        boolean found = false;
        for (DatabaseModel element : queryResult)
            if (element.getAttributesData().equals(testRoomType.getAttributesData())) found = true;
            else assertTrue(element instanceof RoomType);
        assertTrue(found);
    }

    /**
     * Récupérer toutes les chambres de la base de données
     */
    @Test
    public void selectAllRooms() {
        ArrayList<DatabaseModel> queryResult = selectQuery(DatabaseModel.Tables.ROOMS);
        assertTrue(queryResult.size() > 0);
        boolean found = false;
        for (DatabaseModel element : queryResult)
            if (element.getAttributesData().equals(testRoom.getAttributesData())) found = true;
            else assertTrue(element instanceof Room);
        assertTrue(found);
    }

    /**
     * Récupérer toutes les réservations de la base de données
     */
    @Test
    public void selectAllReservations() {
        ArrayList<DatabaseModel> queryResult = selectQuery(DatabaseModel.Tables.RESERVATIONS);
        assertTrue(queryResult.size() > 0);
        boolean found = false;
        for (DatabaseModel element : queryResult)
            if (element.getAttributesData().equals(testReservation.getAttributesData())) found = true;
            else assertTrue(element instanceof Reservation);
        assertTrue(found);
    }

    /**
     * Récupérer toutes les occupations de la base de données
     */
    @Test
    public void selectAllOccupations() {
        ArrayList<DatabaseModel> queryResult = selectQuery(DatabaseModel.Tables.OCCUPATIONS);
        assertTrue(queryResult.size() > 0);
        boolean found = false;
        for (DatabaseModel element : queryResult)
            if (element.getAttributesData().equals(testOccupation.getAttributesData())) found = true;
            else assertTrue(element instanceof Occupation);
        assertTrue(found);
    }

    /**
     * Récupérer tous les occupants de la base de données
     */
    @Test
    public void selectAllOccupants() {
        ArrayList<DatabaseModel> queryResult = selectQuery(DatabaseModel.Tables.OCCUPANTS);
        assertTrue(queryResult.size() > 0);
        boolean found = false;
        for (DatabaseModel element : queryResult)
            if (element.getAttributesData().equals(testOccupant.getAttributesData())) found = true;
            else assertTrue(element instanceof Occupant);
        assertTrue(found);
    }

    /**
     * Récupérer tous les services de la base de données
     */
    @Test
    public void selectAllServices() {
        ArrayList<DatabaseModel> queryResult = selectQuery(DatabaseModel.Tables.SERVICES);
        assertTrue(queryResult.size() > 0);
        boolean found = false;
        for (DatabaseModel element : queryResult)
            if (element.getAttributesData().equals(testService.getAttributesData())) found = true;
            else assertTrue(element instanceof Service);
        assertTrue(found);
    }

    /**
     * Récupérer toutes les factures de la base de données
     */
    @Test
    public void selectAllBills() {
        ArrayList<DatabaseModel> queryResult = selectQuery(DatabaseModel.Tables.BILLS);
        assertTrue(queryResult.size() > 0);
        boolean found = false;
        for (DatabaseModel element : queryResult)
            if (element.getAttributesData().equals(testBill.getAttributesData())) found = true;
            else assertTrue(element instanceof Bill);
        assertTrue(found);
    }

    /**
     * Récupérer tous les services facturés de la base de données
     */
    @Test
    public void selectAllBilledServices() {
        ArrayList<DatabaseModel> queryResult = selectQuery(DatabaseModel.Tables.BILLEDSERVICES);
        assertTrue(queryResult.size() > 0);
        boolean found = false;
        for (DatabaseModel element : queryResult)
            if (element.getAttributesData().equals(testBilledService.getAttributesData())) found = true;
            else assertTrue(element instanceof BilledService);
        assertTrue(found);
    }

    //################ SELECT ONLY ONE #################

    /**
     * Récupérer un hotel spécifique de la base de données
     */
    @Test
    public void selectSpecificHotel() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.HOTELS, testHotel.getID());
        assertTrue(queryResult instanceof Hotel
                && queryResult.getAttributesData().equals(testHotel.getAttributesData()));
    }

    /**
     * Récupérer un client spécifique de la base de données
     */
    @Test
    public void selectSpecificClient() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.CLIENTS, testClient.getID());
        assertTrue(queryResult instanceof Client
                && queryResult.getAttributesData().equals(testClient.getAttributesData()));
    }

    /**
     * Récupérer un type de chambre spécifique de la base de données
     */
    @Test
    public void selectSpecificRoomType() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.ROOMTYPES, testRoomType.getID());
        assertTrue(queryResult instanceof RoomType
                && queryResult.getAttributesData().equals(testRoomType.getAttributesData()));
    }

    /**
     * Récupérer une chambre spécifique de la base de données
     */
    @Test
    public void selectSpecificRoom() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.ROOMS, testRoom.getID());
        assertTrue(queryResult instanceof Room
                && queryResult.getAttributesData().equals(testRoom.getAttributesData()));
    }

    /**
     * Récupérer une réservation spécifique de la base de données
     */
    @Test
    public void selectSpecificReservation() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.RESERVATIONS, testReservation.getID());
        assertTrue(queryResult instanceof Reservation
                && queryResult.getAttributesData().equals(testReservation.getAttributesData()));
    }

    /**
     * Récupérer une occupation spécifique de la base de données
     */
    @Test
    public void selectSpecificOccupation() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.OCCUPATIONS, testOccupation.getID());
        assertTrue(queryResult instanceof Occupation
                && queryResult.getAttributesData().equals(testOccupation.getAttributesData()));
    }

    /**
     * Récupérer un occupant spécifique de la base de données
     */
    @Test
    public void selectSpecificOccupant() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.OCCUPANTS, testOccupant.getID());
        assertTrue(queryResult instanceof Occupant
                && queryResult.getAttributesData().equals(testOccupant.getAttributesData()));
    }

    /**
     * Récupérer un service spécifique de la base de données
     */
    @Test
    public void selectSpecificService() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.SERVICES, testService.getID());
        assertTrue(queryResult instanceof Service
                && queryResult.getAttributesData().equals(testService.getAttributesData()));
    }

    /**
     * Récupérer une facture spécifique de la base de données
     */
    @Test
    public void selectSpecificBill() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.BILLS, testBill.getID());
        assertTrue(queryResult instanceof Bill
                && queryResult.getAttributesData().equals(testBill.getAttributesData()));
    }

    /**
     * Récupérer un service facturé spécifique de la base de données
     */
    @Test
    public void selectSpecificBilledService() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.BILLEDSERVICES, testBilledService.getID());
        assertTrue(queryResult instanceof BilledService
                && queryResult.getAttributesData().equals(testBilledService.getAttributesData()));
    }

    //################ UPDATE #################

    /**
     * Mettre à jour un hotel spécifique de la base de données
     */
    @Test
    public void updateSpecificHotel() {
        testHotel.setNAME("updated");
        updateQuery(testHotel, "NAME");
        selectSpecificHotel();
    }

    /**
     * Mettre à jour un client spécifique de la base de données
     */
    @Test
    public void updateSpecificClient() {
        testClient.setFIRSTNAME("updated");
        updateQuery(testClient, "FIRSTNAME");
        selectSpecificClient();
    }

    /**
     * Mettre à jour un type de chambre spécifique de la base de données
     */
    @Test
    public void updateSpecificRoomType() {
        testRoomType.setNAME("updated");
        updateQuery(testRoomType, "NAME");
        selectSpecificRoomType();
    }

    /**
     * Mettre à jour une chambre spécifique de la base de données
     */
    @Test
    public void updateSpecificRoom() {
        testRoom.setROOMTYPE_ID(testRoom.getROOMTYPE_ID());
        updateQuery(testRoom, "ROOMTYPE_ID");
        selectSpecificRoom();
    }

    /**
     * Mettre à jour une réservation spécifique de la base de données
     */
    @Test
    public void updateSpecificReservation() {
        testReservation.setIS_PAYED(true);
        updateQuery(testReservation, "IS_PAYED");
        selectSpecificReservation();
    }

    /**
     * Mettre à jour une occupation spécifique de la base de données
     */
    @Test
    public void updateSpecificOccupation() {
        testOccupation.setIS_CLIENT_PRESENT(true);
        updateQuery(testOccupation, "IS_CLIENT_PRESENT");
        selectSpecificOccupation();
    }

    /**
     * Mettre à jour un occupant spécifique de la base de données
     */
    @Test
    public void updateSpecificOccupant() {
        testOccupant.setFIRSTNAME("updated");
        updateQuery(testOccupant, "FIRSTNAME");
        selectSpecificOccupant();
    }

    /**
     * Mettre à jour un service spécifique de la base de données
     */
    @Test
    public void updateSpecificService() {
        testService.setNAME("updated");
        updateQuery(testService, "NAME");
        selectSpecificService();
    }

    /**
     * Mettre à jour une facture spécifique de la base de données
     */
    @Test
    public void updateSpecificBill() {
        testBill.setIS_ARCHIVED(true);
        updateQuery(testBill, "IS_ARCHIVED");
        selectSpecificBill();
    }

    /**
     * Mettre à jour un service facturé spécifique de la base de données
     */
    @Test
    public void updateSpecificBilledService() {
        testBilledService.setIS_ARCHIVED(true);
        updateQuery(testBilledService, "IS_ARCHIVED");
        selectSpecificBilledService();
    }
}
