package org.gestion_bibi_hotels;

import org.gestion_bibi_hotels.database.DatabaseData;
import org.gestion_bibi_hotels.database.DatabaseModel;
import de.saxsys.javafx.test.JfxRunner;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests de la classe DatabaseData
 */
@RunWith(JfxRunner.class)
public class DatabaseDataTests extends TestCase {

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

    /**
     * Récupérer tous les éléments de la base de données
     */
    @Test
    public void retrieveAllItems() {
        DatabaseData.getInstance().retrieveDatabase();
        assertFalse(DatabaseData.getInstance().getBills().isEmpty());
        assertFalse(DatabaseData.getInstance().getBilledServices().isEmpty());
        assertFalse(DatabaseData.getInstance().getClients().isEmpty());
        assertFalse(DatabaseData.getInstance().getHotels().isEmpty());
        assertFalse(DatabaseData.getInstance().getOccupants().isEmpty());
        assertFalse(DatabaseData.getInstance().getOccupations().isEmpty());
        assertFalse(DatabaseData.getInstance().getReservations().isEmpty());
        assertFalse(DatabaseData.getInstance().getRooms().isEmpty());
        assertFalse(DatabaseData.getInstance().getRoomTypes().isEmpty());
        assertFalse(DatabaseData.getInstance().getServices().isEmpty());
    }

    /**
     * Récupérer l'instance d'une facture à partir de son ID
     */
    @Test
    public void retrieveReferencedBillFromID() {
        DatabaseModel result = DatabaseData.getInstance().getReferenceFromID(DatabaseModel.Tables.BILLS, TestModels.bill.getID());
        assertEquals(result.getAttributesData(), TestModels.bill.getAttributesData());
    }

    /**
     * Récupérer l'instance d'un service facturé à partir de son ID
     */
    @Test
    public void retrieveReferencedBilledServiceFromID() {
        DatabaseModel result = DatabaseData.getInstance().getReferenceFromID(DatabaseModel.Tables.BILLEDSERVICES, TestModels.billedService.getID());
        assertEquals(result.getAttributesData(), TestModels.billedService.getAttributesData());
    }

    /**
     * Récupérer l'instance d'un client à partir de son ID
     */
    @Test
    public void retrieveReferencedClientFromID() {
        DatabaseModel result = DatabaseData.getInstance().getReferenceFromID(DatabaseModel.Tables.CLIENTS, TestModels.client.getID());
        assertEquals(result.getAttributesData(), TestModels.client.getAttributesData());
    }

    /**
     * Récupérer l'instance d'un hotel à partir de son ID
     */
    @Test
    public void retrieveReferencedHotelFromID() {
        DatabaseModel result = DatabaseData.getInstance().getReferenceFromID(DatabaseModel.Tables.HOTELS, TestModels.hotel.getID());
        assertEquals(result.getAttributesData(), TestModels.hotel.getAttributesData());
    }

    /**
     * Récupérer l'instance d'un occupant à partir de son ID
     */
    @Test
    public void retrieveReferencedOccupantFromID() {
        DatabaseModel result = DatabaseData.getInstance().getReferenceFromID(DatabaseModel.Tables.OCCUPANTS, TestModels.occupant.getID());
        assertEquals(result.getAttributesData(), TestModels.occupant.getAttributesData());
    }

    /**
     * Récupérer l'instance d'une occupation à partir de son ID
     */
    @Test
    public void retrieveReferencedOccupationFromID() {
        DatabaseModel result = DatabaseData.getInstance().getReferenceFromID(DatabaseModel.Tables.OCCUPATIONS, TestModels.occupation.getID());
        assertEquals(result.getAttributesData(), TestModels.occupation.getAttributesData());
    }

    /**
     * Récupérer l'instance d'une réservation à partir de son ID
     */
    @Test
    public void retrieveReferencedReservationFromID() {
        DatabaseModel result = DatabaseData.getInstance().getReferenceFromID(DatabaseModel.Tables.RESERVATIONS, TestModels.reservation.getID());
        assertEquals(result.getAttributesData(), TestModels.reservation.getAttributesData());
    }

    /**
     * Récupérer l'instance d'une chambre à partir de son ID
     */
    @Test
    public void retrieveReferencedRoomFromID() {
        DatabaseModel result = DatabaseData.getInstance().getReferenceFromID(DatabaseModel.Tables.ROOMS, TestModels.room.getID());
        assertEquals(result.getAttributesData(), TestModels.room.getAttributesData());
    }

    /**
     * Récupérer l'instance d'un type de chambre à partir de son ID
     */
    @Test
    public void retrieveReferencedRoomTypeFromID() {
        DatabaseModel result = DatabaseData.getInstance().getReferenceFromID(DatabaseModel.Tables.ROOMTYPES, TestModels.roomType.getID());
        assertEquals(result.getAttributesData(), TestModels.roomType.getAttributesData());
    }

    /**
     * Récupérer l'instance d'un service à partir de son ID
     */
    @Test
    public void retrieveReferencedServiceFromID() {
        DatabaseModel result = DatabaseData.getInstance().getReferenceFromID(DatabaseModel.Tables.SERVICES, TestModels.service.getID());
        assertEquals(result.getAttributesData(), TestModels.service.getAttributesData());
    }

}
