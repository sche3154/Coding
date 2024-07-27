package R18A_Group7_Asm2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;



public class ProductTest {
    @Test
    public void testConstructor() {
        Product p = new  Product("nameTest", 10,"CategoryTest",1, 7.0);
        assertNotNull(p);
    }
    @Test
    public void getNameTest() {
        Product p = new  Product("nameTest", 10,"CategoryTest",1, 7.0);
        assertEquals(p.getName(),"nameTest");
    }
    @Test
    public void updateTest() throws IOException {
        assertFalse(Product.updateProductStock("Mineral Water",20));
//        Product.updateProductStock("Mineral Water",-20);
        assertFalse(Product.updateProductStock("Mineral Water",9));
//        Product.updateProductStock("Mineral Water",-9);
    }

    @Test
    public void updateTest1() throws IOException {
        assertFalse(Product.updateProductStock("Mineral Water",-20));
        Product.updateProductStock("Mineral Water",20);
    }

    @Test
    public void updateGetStockTest() throws IOException, ParseException {
        Product.updateProductStock("Mineral Water",2);
        assertSame(11,Product.getProductStock("Mineral Water"));
        Product.updateProductStock("Mineral Water",-2);
    }

    @Test
    public void getListTest()  {
        try{
            List<Product> productList = new ArrayList<Product>();
            productList = Product.createProductList();
            assertEquals("Mineral Water",productList.get(0).getName());
        }catch(IOException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateGetCodeTest() {
        try{
            assertEquals("0001",Product.getProductCode("Mineral Water"));
            Product.updateProductCode("Mineral Water", "0000");
            assertEquals("0000",Product.getProductCode("Mineral Water"));
            Product.updateProductCode("Mineral Water", "0001");
        }catch(IOException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateGetPriceTest() {
        try{
            assertEquals("2.99",Product.getProductPrice("Mineral Water"));
            Product.updateProductPrice("Mineral Water", "3.00");
            assertEquals("3.00",Product.getProductPrice("Mineral Water"));
            Product.updateProductPrice("Mineral Water", "2.99");
        }catch(IOException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            e.printStackTrace();
        }
    }


}