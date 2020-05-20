package org.gestion_bibi_hotels;

import de.saxsys.javafx.test.JfxRunner;
import org.gestion_bibi_hotels.database.DatabaseData;
import org.gestion_bibi_hotels.database.DatabaseModel;
import org.gestion_bibi_hotels.models.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(JfxRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientServiceTests {

    /**
     * Insertion des modèles de test
     */
    @BeforeClass
    public static void initTestModels() {
        TestModels.insertModels();
        TestModels.initServices();
    }

    /**
     * Suppression des modèles de test
     */
    @AfterClass
    public static void deleteModels() {
        TestModels.deleteModels();
    }

    /**
     * Vérifier les services disponibles pour une occupation
     */
    @Test
    public void check1AvailableServices() {
        ArrayList<Service> result = ClientService.getInstance().getAvailableServices(TestModels.occupation);
        boolean found = false;
        for (Service service : result)
            if (service.getAttributesData().equals(TestModels.service.getAttributesData()))
                found = true;
        assertTrue(found);
        TestModels.service = (Service) DatabaseData.getInstance().getReferenceFromID(DatabaseModel.Tables.SERVICES, TestModels.service.getID());
        TestModels.service.setUNIQUE_ORDER(true);
        result = ClientService.getInstance().getAvailableServices(TestModels.occupation);
        assertTrue(result.isEmpty());
    }

    /**
     * Vérifier le nombre de places restantes dans une chambre
     */
    @Test
    public void check2RemainingSize() {
        int result = ClientService.getInstance().getRemainingSize(TestModels.occupation);
        assertEquals(3, result);
        Occupant test2 = new Occupant(TestModels.occupation.getID(), "test2", "test2");
        result = ClientService.getInstance().getRemainingSize(TestModels.occupation);
        assertEquals(2, result);
        test2.delete();
    }

    /**
     * Vérifier changement préesnce client
     */
    @Test
    public void check3UpdatePresence() {
        ClientService.getInstance().updatePresence(TestModels.occupation, true);
        assertTrue(TestModels.occupation.getIS_CLIENT_PRESENT());
        ClientService.getInstance().updatePresence(TestModels.occupation, false);
        assertFalse(TestModels.occupation.getIS_CLIENT_PRESENT());
    }

    /**
     * Vérifier la facturation d'un service pour une occupation
     */
    @Test
    public void check4BillService() {
        ClientService.getInstance().billService(TestModels.occupation, TestModels.service);
        ArrayList<BilledService> result = ClientService.getInstance().getBilledServices(TestModels.occupation);
        assertEquals(2, result.size());
        BilledService lastInserted = TestModels.billedService;
        for (BilledService billedService : result)
            if (billedService.getID() > lastInserted.getID()) lastInserted = billedService;
        lastInserted.delete();
    }

    /**
     * Vérifier l'attribution du status client régulier à un client
     */
    @Test
    public void check5SetRegularClient() {
        ClientService.getInstance().setRegularClient(TestModels.client);
        assertTrue(TestModels.client.getIS_REGULAR());
    }

    @Test
    public void check6IsServiceFactured() {
        assertTrue(ClientService.getInstance().isServiceFactured(TestModels.occupation, TestModels.service));
    }

    /**
     * Vérifier l'historique des réservaitons d'un client
     */
    @Test
    public void check7GetClientHistory() {
        int newBillID = TestModels.bill.getID() + 1;
        TestModels.bill.delete();
        ArrayList<Reservation> result = ClientService.getInstance().getClientHistory(TestModels.client);
        assertTrue(result.isEmpty());
        TestModels.reservation.setIS_ARCHIVED(true);
        TestModels.reservation.updateColumn(Reservation.Columns.IS_ARCHIVED);
        ClientService.getInstance().initOccupations();
        result = ClientService.getInstance().getClientHistory(TestModels.client);
        boolean found = false;
        for (Reservation reservation : result)
            if (reservation.getAttributesData().equals(TestModels.reservation.getAttributesData()))
                found = true;
        assertTrue(found);
        TestModels.bill = (Bill) DatabaseData.getInstance().getReferenceFromID(DatabaseModel.Tables.BILLS, newBillID);
    }
}