package R18A_Group7_Asm2;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class UserTest {
    @Test
    public void constructTest() {
        User u = new User("leo1","11", "Customer","Charles", "40691");
        assertNotNull(u);

    }

    @Test
    public void addRemoveExistUserTest() throws IOException {
        User.addNewUser("leo1","11", "Customer");
        boolean exist = User.userExist("leo1","11");
        assertTrue(exist);
        User.removeUser("leo1", "Customer");
        exist = User.userExist("leo1","11");
        assertFalse(exist);

    }


    @Test
    public void userNameTest() throws IOException {
        User.addNewUser("leo1","11", "Customer");
        boolean exist = User.nameExist("leo1");
        assertTrue(exist);
        User.removeUser("leo1", "Customer");

    }


    @Test
    public void saveBankDetailsTest() throws IOException {
        User.addNewUser("leo","11", "Customer");

        try{
            User.saveBankDetail("leo", "Leo", "000");
            String[] card_details = User.getUserCard("leo");
            assertEquals(card_details[0], "Leo");
            assertEquals(card_details[1], "000");

        }catch(NullPointerException e){
            e.printStackTrace();
        }
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("user.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            jsonArray.remove(jsonArray.size()-1);
            try (FileWriter file = new FileWriter("user.json")) {

                file.write(jsonArray.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void isCustomerTest() {
        User.addNewUser("leo1","11", "Customer");
        boolean isCus = User.isCustomer("leo1");
        assertTrue(isCus);
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("user.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            jsonArray.remove(jsonArray.size()-1);
            try (FileWriter file = new FileWriter("user.json")) {

                file.write(jsonArray.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void isSellerTest() throws IOException {
        User.addNewUser("leo1","11", "Seller");
        boolean isSell = User.isSeller("leo1");
        assertTrue(isSell);
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("user.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            jsonArray.remove(jsonArray.size()-1);
            try (FileWriter file = new FileWriter("user.json")) {

                file.write(jsonArray.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void isCashierTest() throws IOException {
        User.addNewUser("leo1","11", "Cashier");
        boolean isSell = User.isCashier("leo1");
        assertTrue(isSell);
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("user.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            jsonArray.remove(jsonArray.size()-1);
            try (FileWriter file = new FileWriter("user.json")) {

                file.write(jsonArray.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



}