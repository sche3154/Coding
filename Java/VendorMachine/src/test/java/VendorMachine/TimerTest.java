package VendorMachine;

import org.checkerframework.checker.units.qual.C;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {
    Timer timer;

    @Before
    public void setup() {
        timer = Timer.getInstance();
    }


    @Test
    public void constructorTest() {
        assertNotNull(timer);;
    }

    @Test
    public void frameTest() {
        VendingMachine.frame = new VendingMachine();
        timer.setCurrentFrame("vend");
        timer.removeFrame("vend");

        UserLoggedVendingMachine.frame = new UserLoggedVendingMachine();
        timer.setCurrentFrame("lvend");
        timer.removeFrame("lvend");

        SignUp.frame = new SignUp();
        timer.setCurrentFrame("sign");
        timer.removeFrame("sign");

        OwnerAdmin.frame = new OwnerAdmin();
        timer.setCurrentFrame("oa");
        timer.removeFrame("oa");

        OwnerToSeller.frame = new OwnerToSeller();
        timer.setCurrentFrame("os");
        timer.removeFrame("os");

        OwnerToCashier.frame = new OwnerToCashier();
        timer.setCurrentFrame("oc");
        timer.removeFrame("oc");

        OrderCompleted.frame = new OrderCompleted();
        timer.setCurrentFrame("od");
        timer.removeFrame("od");

        LogIn.frame = new LogIn();
        timer.setCurrentFrame("log");
        timer.removeFrame("log");

        LoggedCheckOut.frame = new LoggedCheckOut();
        timer.setCurrentFrame("lc");
        timer.removeFrame("lc");

        CheckOut.frame = new CheckOut();
        timer.setCurrentFrame("ch");
        timer.removeFrame("ch");

        CashierAdmin.frame = new CashierAdmin();
        timer.setCurrentFrame("ca");
        timer.removeFrame("ca");

        SellerAdmin.frame = new SellerAdmin();
        timer.setCurrentFrame("se");
        timer.removeFrame("se");

        VendingMachine.frame.dispose();

    }
}
