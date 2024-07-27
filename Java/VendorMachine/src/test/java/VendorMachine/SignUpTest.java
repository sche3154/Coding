package VendorMachine;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.fail;

public class SignUpTest {
    @Test
    public void main() {
        try {
            SignUp.main(null);
        } catch (Exception e){
            fail(Arrays.toString(e.getStackTrace()) +"");

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
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    @Test
    public void buttonTest1() {
        SignUp signUp = new SignUp();
        try{
            signUp.create.doClick();
        }catch (Exception e) {
//            fail(Arrays.toString(e.getStackTrace()) +"");
            e.printStackTrace();
        }
    }

}
