package R18A_Group7_Asm2;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class User {
    String account_name;
    String account_password;
    String card_name;
    String card_number;
    String account_type;

    public User(String account_name, String account_password,
                String account_type, String card_name,String card_number){
        this.account_name = account_name;
        this.account_password = account_password;
        this.account_type = account_type;
        this.card_name = card_name;
        this.card_number = card_number;
    }

    public static void addNewUser(String account_name, String account_password, String account_type) {
        JSONParser jsonParser = new JSONParser();

        ///add new currency///
        JSONObject userDetails = new JSONObject();

        userDetails.put("account_name", account_name);
        userDetails.put("account_password", account_password);
        userDetails.put("account_type", account_type);
        userDetails.put("card_name", "null");
        userDetails.put("card_number", "null");
        //exchange rate array for recording changes
        JSONArray array = new JSONArray();
        array.add(userDetails);
        JSONArray userList = new JSONArray();
        userList.add(userDetails);

        try (FileReader reader = new FileReader("user.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray readList = (JSONArray) obj;
            readList.add(userDetails);
            try (FileWriter file = new FileWriter("user.json")) {

                file.write(readList.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static void removeUser(String account_name, String account_type) {
        JSONParser jsonParser = new JSONParser();

        int index = 0;
        try (FileReader reader = new FileReader("user.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for (Object o : jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String name = (String) jsonObj.get("account_name");
                String type = (String) jsonObj.get("account_type");
                if(name.equals(account_name) && type.equals(account_type) ){
                    jsonArray.remove(index);
                    break;
                }
                index ++;
            }
            try (FileWriter file = new FileWriter("user.json")) {
                file.write(jsonArray.toJSONString()+"\n");
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }



    public static void  saveBankDetail(String account_name,String card_name, String card_number) throws IOException {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("user.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for (Object o : jsonArray) {

                JSONObject jsonObj = (JSONObject) o;
                String temp_name = (String) jsonObj.get("account_name");
                if (account_name.equals(temp_name)) {
                    jsonObj.put("card_name", card_name);
                    jsonObj.put("card_number", card_number);
                }
            }
            try (FileWriter file = new FileWriter("user.json")) {
                file.write(jsonArray.toJSONString()+"\n");
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static boolean isCustomer(String username){

        boolean isCus = false;

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("user.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for (Object o : jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String name = (String) jsonObj.get("account_name");
                String type = (String) jsonObj.get("account_type");
                if(name.equals(username) ){
                    if (type.equals("Customer")){
                        isCus = true;
                    }
                }
            }

        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return  isCus;
    }

    public static boolean isSeller(String username){

        boolean isSell = false;

        JSONParser jsonParser = new JSONParser();

        ///add new currency///
        try (FileReader reader = new FileReader("user.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for (Object o : jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String name = (String) jsonObj.get("account_name");
                String type = (String) jsonObj.get("account_type");
                if(name.equals(username) ){
                    if (type.equals("Seller")){
                        isSell = true;
                    }
                }
            }

        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return  isSell;
    }

    public static boolean isCashier(String username){

        boolean isCash = false;

        JSONParser jsonParser = new JSONParser();

        ///add new currency///
        try (FileReader reader = new FileReader("user.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for (Object o : jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String name = (String) jsonObj.get("account_name");
                String type = (String) jsonObj.get("account_type");
                if(name.equals(username) ){
                    if (type.equals("Cashier")){
                        isCash = true;
                    }
                }
            }

        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return  isCash;
    }

    public static boolean isOwner(String username){

        boolean isOwner = false;

        JSONParser jsonParser = new JSONParser();

        ///add new currency///
        try (FileReader reader = new FileReader("user.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for (Object o : jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String name = (String) jsonObj.get("account_name");
                String type = (String) jsonObj.get("account_type");
                if(name.equals(username) ){
                    if (type.equals("Owner")){
                        isOwner = true;
                    }
                }
            }

        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return  isOwner;
    }

    public static boolean userExist(String username, String account_password){

        boolean exist = false;

        JSONParser jsonParser = new JSONParser();

        ///add new currency///
        try (FileReader reader = new FileReader("user.json")) {
            //Read JSON file
//            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for (Object o : jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String name = (String) jsonObj.get("account_name");
                String password = (String) jsonObj.get("account_password");

                if(name.equals(username) && password.equals(account_password)){
                    exist = true;
                }
            }

        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }


        return  exist;
    }


    public static boolean nameExist(String username){

        boolean exist = false;

        JSONParser jsonParser = new JSONParser();


        try (FileReader reader = new FileReader("user.json")) {
            //Read JSON file
//            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for (Object o : jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String name = (String) jsonObj.get("account_name");
                if(name.equals(username) ){
                    exist = true;
                }
            }

        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }


        return  exist;
    }


    public static String[] getUserCard(String username){

        String[] card_details;

        JSONParser jsonParser = new JSONParser();

        ///add new currency///
        try (FileReader reader = new FileReader("user.json")) {
            //Read JSON file
//            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for (Object o : jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String name = (String) jsonObj.get("account_name");
                if(name.equals(username) ){
                    String cardname = (String) jsonObj.get("card_name");
                    String cardnumber = (String) jsonObj.get("card_number");
                    if(cardname.equals("null") == false){
                        card_details = new String[2];
                        card_details[0] = cardname;
                        card_details[1] = cardnumber;

                        return card_details;
                    }
                }
            }

        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }


        return null;
    }




}