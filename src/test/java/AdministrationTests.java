import de.saxsys.javafx.test.JfxRunner;
import models.Administration;
import models.Bill;
import models.Reservation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(JfxRunner.class)
public class AdministrationTests {

    /**
     * Insertion des modèles de test
     */
    @BeforeClass
    public static void initTestModels() {
        TestModels.insertModels();
        TestModels.bill.setIS_ARCHIVED(true);
        TestModels.bill.updateColumn(Bill.Columns.IS_ARCHIVED);
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
     * Vérification du fonctionnement de la méthode permettant d'avoir le ratio de nuits réservées / nuits totales
     * de chaque type de chambre
     */
    @Test
    public void checkAvailableBilledRoomRatio() {
        Double result = Administration.getInstance().getAvailableBilledRoomRatio().get("test");
        assertEquals(1.1, result, 0.1);
        TestModels.reservation.setDURATION(3);
        TestModels.reservation.updateColumn(Reservation.Columns.DURATION);
        result = Administration.getInstance().getAvailableBilledRoomRatio().get("test");
        assertEquals(3.3, result, 0.1);
    }

    /**
     * Vérification de la méthode permettant d'avoir le bénéfice de chaque hotel
     */
    @Test
    public void checkBilledAmounts() {
        Double result = Administration.getInstance().getBilledAmounts().get("test");
        assertEquals(0.0, result, 0.0);
        TestModels.bill.setAMOUNT(300.0);
        TestModels.bill.updateColumn(Bill.Columns.AMOUNT);
        result = Administration.getInstance().getBilledAmounts().get("test");
        assertEquals(300.0, result, 0.0);
    }

    /**
     * Vérification de la méthode permettant d'avoir le nombre de services facturés
     * pour chaque service
     */
    @Test
    public void checkBilledServicesAmount() {
        int result = Administration.getInstance().getBilledServicesAmount().get("test");
        assertEquals(1, result);
    }
}
