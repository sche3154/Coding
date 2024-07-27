package R18A_Group7_Asm2;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.fail;

public class LoggedCheckOutTest {
    @Test
    public void main() {
        try {
            LoggedCheckOut.main(null);
        } catch (Exception e){
            fail(Arrays.toString(e.getStackTrace()) +"");

        }
    }

    @Test
    public void buttonTest1() {
        LoggedCheckOut loggedCheckout = new LoggedCheckOut();
        try{
            loggedCheckout.cancelButton.doClick();
        }catch (Exception e) {
            fail(Arrays.toString(e.getStackTrace()) +"");
        }
    }

    @Test
    public void buttonTest2() {
        LoggedCheckOut loggedCheckout = new LoggedCheckOut();
        try{
            loggedCheckout.enterButton.doClick();
        }catch (Exception e) {
            fail(Arrays.toString(e.getStackTrace()) +"");
        }
    }

    @Test
    public void buttonTest3() {
        LoggedCheckOut loggedCheckout = new LoggedCheckOut();
        try{
            loggedCheckout.proceedButton.doClick();
        }catch (Exception e) {
            fail(Arrays.toString(e.getStackTrace()) +"");
        }
    }

    @Test
    public void buttonTest4() {
        LoggedCheckOut loggedCheckout = new LoggedCheckOut();
        try{
            loggedCheckout.saveBankAccount.doClick();
        }catch (Exception e) {
            fail(Arrays.toString(e.getStackTrace()) +"");
        }
    }

}
