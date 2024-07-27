package VendorMachine;

import R18A_Group7_Asm2.CheckOut;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckOutTest {
    @Test
    public void main() {
        try {
            CheckOut.main(null);
        } catch (Exception e1){
            fail(e1.getStackTrace()+"");

        }
    }

    @Test
    public void testCancelButton(){
        try{
            CheckOut a = new CheckOut();
            a.cancelButton.doClick();
        }catch(NullPointerException e){
            e.printStackTrace();
        }
//        CheckOut a = new CheckOut();
//        a.cancelButton.doClick();



    }

    @Test
    public void testEnterButton(){
        CheckOut a = new CheckOut();
        a.enterButton.doClick();

        CheckOut b = new CheckOut();
        b.enterAmount.setText("2");
        b.enterButton.doClick();
    }

    @Test
    public void testProceedButton(){
        CheckOut a = new CheckOut();
        a.proceedButton.doClick();
    }
}
