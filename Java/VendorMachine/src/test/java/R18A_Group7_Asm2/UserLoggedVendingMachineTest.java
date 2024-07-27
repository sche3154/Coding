package R18A_Group7_Asm2;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.fail;

public class UserLoggedVendingMachineTest {
    @Test
    public void main() {
        try {
            UserLoggedVendingMachine.main(null);
        } catch (Exception e){
            fail(Arrays.toString(e.getStackTrace()) +"");

        }
    }

    @Test
    public void buttonTest1() {
        UserLoggedVendingMachine userLoggedVendingMachine = new UserLoggedVendingMachine();
        try{
            userLoggedVendingMachine.log_out.doClick();
        }catch (Exception e) {
            fail(Arrays.toString(e.getStackTrace()) +"");
        }
    }

    @Test
    public void buttonTest2() {
        UserLoggedVendingMachine userLoggedVendingMachine = new UserLoggedVendingMachine();
        try{
            userLoggedVendingMachine.checkout.doClick();
        }catch (Exception e) {
            fail(Arrays.toString(e.getStackTrace()) +"");
        }
    }

    @Test
    public void buttonTest3() {
        UserLoggedVendingMachine userLoggedVendingMachine = new UserLoggedVendingMachine();
        try{
            userLoggedVendingMachine.addToBag.doClick();
        }catch (Exception e) {
            fail(Arrays.toString(e.getStackTrace()) +"");
        }
    }

}
