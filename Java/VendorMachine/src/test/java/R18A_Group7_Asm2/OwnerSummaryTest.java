package R18A_Group7_Asm2;

import org.junit.Before;
import org.junit.Test;

import java.beans.Transient;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class OwnerSummaryTest {

    OwnerSummary ownerSummary;
    List<OwnerSummary.CancelledTransaction> cancelledTransactionList;


    @Before
    public void setup() {
        ownerSummary = OwnerSummary.getInstance();
        cancelledTransactionList = ownerSummary.getTransactionList();
    }

    @Test
    public void constructorTest() {
        assertNotNull(ownerSummary);
    }

    @Test
    public void getListTest() {
        assertFalse(cancelledTransactionList.isEmpty());
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String time = simpleDateFormat.format(date.getTime());

        ownerSummary.addTransaction(time, "user cancelled", "Testing developer");

        assertFalse(cancelledTransactionList.isEmpty());
    }

    @Test
    public void reportTest() throws IOException {
        assertTrue(ownerSummary.writeIntoHistory());
        assertTrue(ownerSummary.generateReport());
    }

}
