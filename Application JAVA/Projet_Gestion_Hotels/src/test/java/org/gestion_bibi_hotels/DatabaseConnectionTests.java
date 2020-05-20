package org.gestion_bibi_hotels;

import org.gestion_bibi_hotels.database.DatabaseModel;
import de.saxsys.javafx.test.JfxRunner;
import junit.framework.TestCase;
import org.gestion_bibi_hotels.models.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.gestion_bibi_hotels.database.DatabaseConnection.selectQuery;
import static org.gestion_bibi_hotels.database.DatabaseConnection.updateQuery;

/**
 * Tests de la classe DatabaseConnection
 */
@RunWith(JfxRunner.class)
public class DatabaseConnectionTests extends TestCase {
    
    /**
     * Insertion des modèles de test
     */
    @BeforeClass
    public static void insertModels() {
        TestModels.insertModels();
    }

    /**
     * Suppression des modèles de test
     */
    @AfterClass
    public static void deleteModels() {
        TestModels.deleteModels();
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
            if (element.getAttributesData().equals(TestModels.hotel.getAttributesData())) found = true;
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
            if (element.getAttributesData().equals(TestModels.client.getAttributesData())) found = true;
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
            if (element.getAttributesData().equals(TestModels.roomType.getAttributesData())) found = true;
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
            if (element.getAttributesData().equals(TestModels.room.getAttributesData())) found = true;
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
            if (element.getAttributesData().equals(TestModels.reservation.getAttributesData())) found = true;
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
            if (element.getAttributesData().equals(TestModels.occupation.getAttributesData())) found = true;
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
            if (element.getAttributesData().equals(TestModels.occupant.getAttributesData())) found = true;
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
            if (element.getAttributesData().equals(TestModels.service.getAttributesData())) found = true;
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
            if (element.getAttributesData().equals(TestModels.bill.getAttributesData())) found = true;
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
            if (element.getAttributesData().equals(TestModels.billedService.getAttributesData())) found = true;
            else assertTrue(element instanceof BilledService);
        assertTrue(found);
    }

    //################ SELECT ONLY ONE #################

    /**
     * Récupérer un hotel spécifique de la base de données
     */
    @Test
    public void selectSpecificHotel() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.HOTELS, TestModels.hotel.getID());
        assertTrue(queryResult instanceof Hotel
                && queryResult.getAttributesData().equals(TestModels.hotel.getAttributesData()));
    }

    /**
     * Récupérer un client spécifique de la base de données
     */
    @Test
    public void selectSpecificClient() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.CLIENTS, TestModels.client.getID());
        assertTrue(queryResult instanceof Client
                && queryResult.getAttributesData().equals(TestModels.client.getAttributesData()));
    }

    /**
     * Récupérer un type de chambre spécifique de la base de données
     */
    @Test
    public void selectSpecificRoomType() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.ROOMTYPES, TestModels.roomType.getID());
        assertTrue(queryResult instanceof RoomType
                && queryResult.getAttributesData().equals(TestModels.roomType.getAttributesData()));
    }

    /**
     * Récupérer une chambre spécifique de la base de données
     */
    @Test
    public void selectSpecificRoom() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.ROOMS, TestModels.room.getID());
        assertTrue(queryResult instanceof Room
                && queryResult.getAttributesData().equals(TestModels.room.getAttributesData()));
    }

    /**
     * Récupérer une réservation spécifique de la base de données
     */
    @Test
    public void selectSpecificReservation() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.RESERVATIONS, TestModels.reservation.getID());
        assertTrue(queryResult instanceof Reservation
                && queryResult.getAttributesData().equals(TestModels.reservation.getAttributesData()));
    }

    /**
     * Récupérer une occupation spécifique de la base de données
     */
    @Test
    public void selectSpecificOccupation() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.OCCUPATIONS, TestModels.occupation.getID());
        assertTrue(queryResult instanceof Occupation
                && queryResult.getAttributesData().equals(TestModels.occupation.getAttributesData()));
    }

    /**
     * Récupérer un occupant spécifique de la base de données
     */
    @Test
    public void selectSpecificOccupant() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.OCCUPANTS, TestModels.occupant.getID());
        assertTrue(queryResult instanceof Occupant
                && queryResult.getAttributesData().equals(TestModels.occupant.getAttributesData()));
    }

    /**
     * Récupérer un service spécifique de la base de données
     */
    @Test
    public void selectSpecificService() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.SERVICES, TestModels.service.getID());
        assertTrue(queryResult instanceof Service
                && queryResult.getAttributesData().equals(TestModels.service.getAttributesData()));
    }

    /**
     * Récupérer une facture spécifique de la base de données
     */
    @Test
    public void selectSpecificBill() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.BILLS, TestModels.bill.getID());
        assertTrue(queryResult instanceof Bill
                && queryResult.getAttributesData().equals(TestModels.bill.getAttributesData()));
    }

    /**
     * Récupérer un service facturé spécifique de la base de données
     */
    @Test
    public void selectSpecificBilledService() {
        DatabaseModel queryResult = selectQuery(DatabaseModel.Tables.BILLEDSERVICES, TestModels.billedService.getID());
        assertTrue(queryResult instanceof BilledService
                && queryResult.getAttributesData().equals(TestModels.billedService.getAttributesData()));
    }

    //################ UPDATE #################

    /**
     * Mettre à jour un hotel spécifique de la base de données
     */
    @Test
    public void updateSpecificHotel() {
        TestModels.hotel.setNAME("updated");
        updateQuery(TestModels.hotel, "NAME");
        selectSpecificHotel();
    }

    /**
     * Mettre à jour un client spécifique de la base de données
     */
    @Test
    public void updateSpecificClient() {
        TestModels.client.setFIRSTNAME("updated");
        updateQuery(TestModels.client, "FIRSTNAME");
        selectSpecificClient();
    }

    /**
     * Mettre à jour un type de chambre spécifique de la base de données
     */
    @Test
    public void updateSpecificRoomType() {
        TestModels.roomType.setNAME("updated");
        updateQuery(TestModels.roomType, "NAME");
        selectSpecificRoomType();
    }

    /**
     * Mettre à jour une chambre spécifique de la base de données
     */
    @Test
    public void updateSpecificRoom() {
        TestModels.room.setROOMTYPE_ID(TestModels.room.getROOMTYPE_ID());
        updateQuery(TestModels.room, "ROOMTYPE_ID");
        selectSpecificRoom();
    }

    /**
     * Mettre à jour une réservation spécifique de la base de données
     */
    @Test
    public void updateSpecificReservation() {
        TestModels.reservation.setIS_PAYED(true);
        updateQuery(TestModels.reservation, "IS_PAYED");
        selectSpecificReservation();
    }

    /**
     * Mettre à jour une occupation spécifique de la base de données
     */
    @Test
    public void updateSpecificOccupation() {
        TestModels.occupation.setIS_CLIENT_PRESENT(true);
        updateQuery(TestModels.occupation, "IS_CLIENT_PRESENT");
        selectSpecificOccupation();
    }

    /**
     * Mettre à jour un occupant spécifique de la base de données
     */
    @Test
    public void updateSpecificOccupant() {
        TestModels.occupant.setFIRSTNAME("updated");
        updateQuery(TestModels.occupant, "FIRSTNAME");
        selectSpecificOccupant();
    }

    /**
     * Mettre à jour un service spécifique de la base de données
     */
    @Test
    public void updateSpecificService() {
        TestModels.service.setNAME("updated");
        updateQuery(TestModels.service, "NAME");
        selectSpecificService();
    }

    /**
     * Mettre à jour une facture spécifique de la base de données
     */
    @Test
    public void updateSpecificBill() {
        TestModels.bill.setIS_ARCHIVED(true);
        updateQuery(TestModels.bill, "IS_ARCHIVED");
        selectSpecificBill();
    }

    /**
     * Mettre à jour un service facturé spécifique de la base de données
     */
    @Test
    public void updateSpecificBilledService() {
        TestModels.billedService.setIS_ARCHIVED(true);
        updateQuery(TestModels.billedService, "IS_ARCHIVED");
        selectSpecificBilledService();
    }
}
