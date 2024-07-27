package VendorMachine;


import org.junit.*;
import static org.junit.Assert.*;

public class OrderCompletedTest {
    private OrderCompleted oc;

    @Before
    public void setup() {
        this.oc = new OrderCompleted();
    }

    @Test
    public void mainTest() {
        assertNotNull(this.oc);
        assertNotNull(this.oc.statusLabel);
    }

    @Test
    public void buttonTest(){
        try{
            assertNull(OrderCompleted.frame);
        }catch(AssertionError e){
            e.printStackTrace();
        }

    }

    @Test
    public void backTest() {
        try{
            OrderCompleted o = new OrderCompleted();
            o.backButton.doClick();
        }catch(NullPointerException e){
            e.printStackTrace();
        }

    }

}
