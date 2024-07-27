package VendorMachine;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CreditCard {

    public static boolean cardExist(String name, String number) throws IOException, ParseException{
        JSONParser jsonParser = new JSONParser();
        boolean exist = false;
        try (FileReader reader = new FileReader("credit_cards.json")){
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for (Object o : jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String card_name = (String) jsonObj.get("name");
                String card_number = (String) jsonObj.get("number");
                if (card_number.equals(number) && card_name.equals(name)){
                    exist = true;
                }
            }
        }
        return exist;
    }


    public static String getCard(String name) throws IOException, ParseException{
        JSONParser jsonParser = new JSONParser();

        String saved_Card;

        try (FileReader reader = new FileReader("credit_cards.json")){
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for (Object o : jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String card_name = (String) jsonObj.get("name");
                String card_number = (String) jsonObj.get("number");
                if (card_name.equals(name)){
                    saved_Card = card_number;
                    return saved_Card;
                }
            }
        }

        return null;
    }
}