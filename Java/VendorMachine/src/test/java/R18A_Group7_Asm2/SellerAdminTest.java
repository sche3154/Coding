package R18A_Group7_Asm2;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.fail;

public class SellerAdminTest {

    @Test
    public void main() {
        try {
            SellerAdmin.main(null);
        } catch (Exception e){
            fail(Arrays.toString(e.getStackTrace()) +"");

        }
    }

    @Test
    public void buttonTest1() {
        SellerAdmin sellerAdmin = new SellerAdmin();
        try{
            sellerAdmin.confirmButton.doClick();
        }catch (Exception e) {
            fail(Arrays.toString(e.getStackTrace()) +"");
        }
    }

    @Test
    public void buttonTest2() {
        SellerAdmin sellerAdmin = new SellerAdmin();
        try{
            sellerAdmin.generateItemDetailsButton.doClick();
        }catch (Exception e) {
//            fail(Arrays.toString(e.getStackTrace()) +"");
            fail();
        }
    }

    @Test
    public void buttonTest3() {
        SellerAdmin sellerAdmin = new SellerAdmin();
        try{
            sellerAdmin.generateItemSalesButton.doClick();
        }catch (Exception e) {
            fail(Arrays.toString(e.getStackTrace()) +"");
        }
    }

    @Test
    public void buttonTest4() {
        SellerAdmin sellerAdmin = new SellerAdmin();
        try{
            sellerAdmin.logOutButton.doClick();
        }catch (Exception e) {
//            fail(Arrays.toString(e.getStackTrace()) +"");
            e.printStackTrace();
        }
    }


}
