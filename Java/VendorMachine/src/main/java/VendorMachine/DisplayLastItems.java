package VendorMachine;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DisplayLastItems {

    public static void display(JTextArea area){
//        VendingMachine.last_items.setText(null);
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("purchase.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            int count = 0;
            for(int i = jsonArray.size()-1; i>=0; i--){
                if(count == 5){
                    break;
                }
                JSONObject jsonObj = (JSONObject)jsonArray.get(i);
                String item_name = (String) jsonObj.get("item_name");
                String username = (String) jsonObj.get("username");
                if(username.equals(" ")){
                    area.append(item_name + "\n");
                    count += 1;
                }
            }

        }catch (ParseException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
