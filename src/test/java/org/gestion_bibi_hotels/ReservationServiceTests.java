package org.gestion_bibi_hotels;

import org.gestion_bibi_hotels.database.DatabaseData;
import org.gestion_bibi_hotels.database.DatabaseModel;
import de.saxsys.javafx.test.JfxRunner;
import javafx.collections.ObservableList;
import org.gestion_bibi_hotels.models.Bill;
import org.gestion_bibi_hotels.models.Occupation;
import org.gestion_bibi_hotels.models.Reservation;
import org.gestion_bibi_hotels.models.ReservationService;
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
public class ReservationServiceTests {

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
     * Vérifier le filtrage des réservations pour un hotel donné
     */
    @Test
    public void check1ReservationsFilter() {
        ObservableList<Reservation> result = ReservationService.getInstance(TestModels.hotel).getReservations();
        assertEquals(result.get(0).getAttributesData(), TestModels.reservation.getAttributesData());
    }

    /**
     * Vérifier confirmation du paiement
     */
    @Test
    public void check2ConfirmPayment() {
        ReservationService.getInstance(TestModels.hotel).confirmPayment(TestModels.reservation);
        TestModels.reservation = (Reservation) DatabaseData.getInstance().getReferenceFromID(DatabaseModel.Tables.RESERVATIONS, TestModels.reservation.getID());
        assertTrue(TestModels.reservation.getIS_PAYED());
    }

    /**
     * Vérifier confirmation d'arrivée
     */
    @Test
    public void check3ConfirmArrival() {
        ReservationService.getInstance(TestModels.hotel).confirmArrival(TestModels.reservation);
        assertTrue(TestModels.reservation.getIS_COMFIRMED());
        TestModels.occupation = (Occupation) DatabaseData.getInstance().getReferenceFromID(DatabaseModel.Tables.OCCUPATIONS, TestModels.occupation.getID());
        assertTrue(TestModels.occupation.getIS_CLIENT_PRESENT());
    }

    /**
     * Vérifier l'archivage d'une réservation
     */
    @Test
    public void check4ArchiveReservation() {
        int newBillID = TestModels.bill.getID() + 1;
        TestModels.bill.delete();
        Reservation testReservation = ReservationService.getInstance(TestModels.hotel).getReservations().get(0);
        assertNotNull(testReservation);
        ReservationService.getInstance(TestModels.hotel).archiveReservation(testReservation);
        assertTrue(ReservationService.getInstance(TestModels.hotel).getArchives().contains(testReservation));
        assertTrue(testReservation.getIS_ARCHIVED());
        TestModels.bill = (Bill) DatabaseData.getInstance().getReferenceFromID(DatabaseModel.Tables.BILLS, newBillID);
    }

    /**
     * Récupération des occupations liées à une réservation
     */
    @Test
    public void check5ReservationOccupations() {
        ArrayList<Occupation> result = ReservationService.getInstance(TestModels.hotel).getReservationOccupations(TestModels.reservation.getID());
        boolean found = false;
        for (Occupation occupation : result)
            if (occupation.getAttributesData().equals(TestModels.occupation.getAttributesData()))
                found = true;
        assertTrue(found);
    }
}
