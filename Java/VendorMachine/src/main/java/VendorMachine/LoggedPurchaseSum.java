package VendorMachine;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LoggedPurchaseSum {
    private static String item_name;
    private static String item_code;
    private static double price;
    private static int quantity;
    // Read shopping bag (Table in VM)
//    public static void main(String[] args) {
//        String[][] data = {};
//        String[] columnNames = {"Item(s)", "Quantity", "Price"};
//        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
//        JTable shoppingItems = new JTable(tableModel);
//        Object[] row = {"Mars", 3};
//        tableModel.addRow(row);
//        UserPurchase(tableModel);
//    }
    public static void UserPurchase(DefaultTableModel bag, String username){
        for (int i=0; i < bag.getRowCount() - 1; i++){
            item_name = bag.getValueAt(i, 0).toString();
            int quantity = Integer.parseInt(bag.getValueAt(i,1).toString());
            JSONParser jsonParser = new JSONParser();
            try (FileReader reader = new FileReader("products.json")) {
                //Read JSON file
                JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
                for(Object o:jsonArray) {
                    JSONObject jsonObj = (JSONObject) o;
                    String temp_name = (String) jsonObj.get("name");
                    if(item_name.equals(temp_name)) {
                        //get item code
                        item_code = (String) jsonObj.get("item_code");
                        //get item price
                        double item_price  = Double.parseDouble((jsonObj.get("price").toString()));
                        price = item_price * quantity;
                        writeToFile(item_code, item_name, quantity, price, username);
                    }
                }

            } catch(IOException | ParseException e){
                e.printStackTrace();
            }
        }
    }

    public static void writeToFile(String item_code, String item_name, int quantity, double price, String username){
        JSONObject item_details = new JSONObject();
        item_details.put("username", username);
        item_details.put("item_code", item_code);
        item_details.put("item_name", item_name);
        item_details.put("quantity", String.valueOf(quantity));
        item_details.put("price", String.valueOf(price));

//        JSONArray array = new JSONArray();
//        array.add(item_details);

//        JSONObject itemObject = new JSONObject();
//        itemObject.put("username", null);
//        itemObject.put("Arrays", array);

        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("purchase.json")) {

            //Read JSON file
//            Object obj = jsonParser.parse(reader);
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
//            JSONArray purchaseList = (JSONArray) obj;
            jsonArray.add(item_details);
            try (FileWriter file = new FileWriter("purchase.json")) {

                file.write(jsonArray.toJSONString());
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

}

