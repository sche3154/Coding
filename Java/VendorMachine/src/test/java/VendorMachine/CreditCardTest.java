package VendorMachine;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class CreditCardTest {
    @Test
    public void cardExist() throws IOException, ParseException {

        assertTrue(CreditCard.cardExist("Charles", "40691"));
        assertFalse(CreditCard.cardExist("noRecord", "12345"));
        assertFalse(CreditCard.cardExist("Charles", "12345"));
        assertFalse(CreditCard.cardExist("noRecord", "40691"));
    }

    @Before
    public void setUp() throws Exception {
        CreditCard card = new CreditCard();
    }

    @Test
    public void getCard() throws IOException, ParseException {
        assertNotNull(CreditCard.getCard("Charles"));
        assertNull(CreditCard.getCard("nothisuser"));
    }
}
