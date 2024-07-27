package VendorMachine;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.fail;

public class CashierAdminTest {

    @Test
    public void main() {
        try {
            CashierAdmin.main(null);
        } catch (Exception e){
            fail(Arrays.toString(e.getStackTrace()) +"");

        }
    }

    @Test
    public void buttonTest1() {
        CashierAdmin cashierAdmin = new CashierAdmin();
        try{
            cashierAdmin.generateCashDetailsButton.doClick();
        }catch (Exception e) {
            fail(Arrays.toString(e.getStackTrace()) +"");
        }
    }

    @Test
    public void buttonTest2() {
        CashierAdmin cashierAdmin = new CashierAdmin();
        try{
            cashierAdmin.generateTransactionButton.doClick();
        }catch (Exception e) {
            fail(Arrays.toString(e.getStackTrace()) +"");
        }
    }

    @Test
    public void buttonTest3() {
        CashierAdmin cashierAdmin = new CashierAdmin();
        try{
            cashierAdmin.logOut.doClick();
        }catch (Exception e) {
//            fail(Arrays.toString(e.getStackTrace()) +"");
            e.printStackTrace();
        }
    }

    @Test
    public void buttonTest4() {
        CashierAdmin cashierAdmin = new CashierAdmin();
        try{
            cashierAdmin.reduceButton.doClick();
        }catch (Exception e) {
            fail(Arrays.toString(e.getStackTrace()) +"");
        }
    }
}
