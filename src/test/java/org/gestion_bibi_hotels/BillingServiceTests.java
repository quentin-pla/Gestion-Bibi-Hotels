package org.gestion_bibi_hotels;

import de.saxsys.javafx.test.JfxRunner;
import javafx.collections.ObservableList;
import org.gestion_bibi_hotels.models.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

@RunWith(JfxRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BillingServiceTests {

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
     * Vérifier le filtrage des factures par rapport à un hôtel choisit
     */
    @Test
    public void check1FilterBills() {
        ObservableList<Bill> result = BillingService.getInstance(TestModels.hotel).getPending_bills();
        boolean found = false;
        for (Bill bill : result)
            if (bill.getAttributesData().equals(TestModels.bill.getAttributesData()))
                found = true;
        assertTrue(found);
    }

    /**
     * Vérifier la confirmation du paiement d'une facture
     */
    @Test
    public void check2ConfirmPayment() {
        assertFalse(TestModels.bill.getIS_PAYED());
        BillingService.getInstance(TestModels.hotel).confirmPayment(TestModels.bill);
        assertTrue(TestModels.bill.getIS_PAYED());
    }

    /**
     * Vérifier le calcul du montant total d'une facture
     */
    @Test
    public void check3CalculateTotalBillAmount() {
        assertEquals(0.0, TestModels.bill.getAMOUNT(), 0.0);
        BillingService.getInstance(TestModels.hotel).calculateTotalBillAmount(TestModels.bill);
        double billedserviceprice = TestModels.billedService.getService().getPRICE();
        double totalprice = (TestModels.room.getRoomType().getPRICE() * TestModels.reservation.getDURATION()) + billedserviceprice;
        assertEquals(totalprice, TestModels.bill.getAMOUNT(), 0.0);
        TestModels.roomType.setPRICE(50.00);
        TestModels.roomType.updateColumn(RoomType.Columns.PRICE);
        totalprice = (TestModels.room.getRoomType().getPRICE() * TestModels.reservation.getDURATION()) + billedserviceprice;
        BillingService.getInstance(TestModels.hotel).calculateTotalBillAmount(TestModels.bill);
        assertEquals(totalprice, TestModels.bill.getAMOUNT(), 0.0);
        TestModels.reservation.setDURATION(5);
        TestModels.reservation.updateColumn(Reservation.Columns.DURATION);
        totalprice = (TestModels.room.getRoomType().getPRICE() * TestModels.reservation.getDURATION()) + billedserviceprice;
        BillingService.getInstance(TestModels.hotel).calculateTotalBillAmount(TestModels.bill);
        assertEquals(totalprice, TestModels.bill.getAMOUNT(), 0.0);
        TestModels.reservation.setPEOPLE_COUNT(4);
        TestModels.reservation.updateColumn(Reservation.Columns.PEOPLE_COUNT);
        totalprice = ((TestModels.room.getRoomType().getPRICE() * TestModels.reservation.getDURATION())
                + billedserviceprice);
        totalprice -= (totalprice * BillingService.getInstance(TestModels.hotel).getGroupDiscount())/100.00;
        BillingService.getInstance(TestModels.hotel).calculateTotalBillAmount(TestModels.bill);
        assertEquals(totalprice, TestModels.bill.getAMOUNT(), 0.0);
        TestModels.client.setIS_REGULAR(true);
        TestModels.client.updateColumn(Client.Columns.IS_REGULAR);
        totalprice = ((TestModels.room.getRoomType().getPRICE() * TestModels.reservation.getDURATION())
                + billedserviceprice);
        totalprice -=  (totalprice * BillingService.getInstance(TestModels.hotel).getGroupDiscount())/100.00
                + (totalprice * BillingService.getInstance(TestModels.hotel).getRegularClientDiscount())/100.00;
        BillingService.getInstance(TestModels.hotel).calculateTotalBillAmount(TestModels.bill);
        assertEquals(totalprice, TestModels.bill.getAMOUNT(), 0.0);
    }

    /**
     * Vérifier l'archivage d'une facture
     */
    @Test
    public void check4ArchiveBill() {
        assertFalse(TestModels.bill.getIS_ARCHIVED());
        BillingService.getInstance(TestModels.hotel).archiveBill(TestModels.bill);
        assertTrue(TestModels.bill.getIS_ARCHIVED());
    }
}
