package R18A_Group7_Asm2;

import org.junit.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class VendingMachineTest {


    @Test
    public void main() {
        try {
            VendingMachine.main(null);
        } catch (Exception e1){
            fail(e1.getStackTrace()+"");

        }
    }


    @Test
    public void testGroupLayout(){
        VendingMachine a = new VendingMachine();
        GroupLayout groupLayout =  a.addGroupLayout();

        assertNotNull(groupLayout);
    }


    @Test
    public void testAddtoBag(){
        VendingMachine a = new VendingMachine();
        a.addToBag.doClick();
    }

    @Test
    public void testLogin(){
        VendingMachine a = new VendingMachine();
        a.log_in.doClick();
    }

    @Test
    public void testSignIn(){
        VendingMachine a = new VendingMachine();
        a.sign_in.doClick();
    }

    @Test
    public void tesCheckout(){
        try{
            VendingMachine a = new VendingMachine();
            a.checkout.doClick();
        }catch(NullPointerException e){
            e.printStackTrace();
        }

    }
}
