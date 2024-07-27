package VendorMachine;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Product {
    String name;
    int item_code;
    String category;
    int in_stock;
    double price;
    public Product(String name, int item_code, String category, int in_stock, double price){
        this.name = name;
        this.item_code = item_code;
        this.category = category;
        this.in_stock = in_stock;
        this.price = price;
    }

    public String getName(){
        return this.name;
    }

    public static void updateProductCode(String product, String new_code) throws IOException {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("products.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for(Object o:jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String temp_name = (String) jsonObj.get("name");
                if(product.equals(temp_name)) {
                    jsonObj.put("item_code", new_code);
                }
            }
            try (FileWriter file = new FileWriter("products.json")) {
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

    public static void updateProductPrice(String product, String new_price) throws IOException {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("products.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for(Object o:jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String temp_name = (String) jsonObj.get("name");
                if(product.equals(temp_name)) {
                    jsonObj.put("price", new_price);
                }
            }
            try (FileWriter file = new FileWriter("products.json")) {
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


    public static boolean updateProductStock(String product, int fill) throws IOException {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("products.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for(Object o:jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String temp_name = (String) jsonObj.get("name");
                if(product.equals(temp_name)) {
                    int remain = Integer.parseInt((String) jsonObj.get("in_stock"));
                    remain += fill;
                    if (remain < 0){
                        return false;
                    }
                    if (remain > 15){
                        return false;
                    }

                    String new_stock = String.valueOf(remain);
                    jsonObj.put("in_stock", new_stock);
                }
            }
            try (FileWriter file = new FileWriter("products.json")) {
                file.write(jsonArray.toJSONString()+"\n");
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return true;

    }

    public static List<Product> createProductList() throws IOException, ParseException {
        List<Product> productList = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("products.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for (Object o : jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String name = (String) jsonObj.get("name");
                String category = (String) jsonObj.get("category");
                int in_stock = Integer.parseInt((String) jsonObj.get("in_stock"));
                Product temp_product = new Product(name, 0001, category, in_stock, 1);
                productList.add(temp_product);
            }
        }
        return productList;
    }

    public static String getProductCode(String product) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        String ret = "";
        try (FileReader reader = new FileReader("products.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for (Object o : jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String temp_name = (String) jsonObj.get("name");
                if (product.equals(temp_name)) {
                    ret = (String) jsonObj.get("item_code");

                }
            }
        }
        return ret;
    }

    public static boolean checkItemCode(String itemCode) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        boolean res = false;
        try (FileReader reader = new FileReader("products.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for (Object o : jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String temp_code = (String) jsonObj.get("item_code");
                if (itemCode.equals(temp_code)) {
                    res = true;
                    return  res;
                }
            }
        }
        return res;
    }

    public static String getProductPrice(String product) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        String ret = "";
        try (FileReader reader = new FileReader("products.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for (Object o : jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String temp_name = (String) jsonObj.get("name");
                if (product.equals(temp_name)) {
                    ret = (String) jsonObj.get("price");

                }
            }
        }
        return ret;
    }

    public static int getProductStock(String product) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        int ret = 0;
        try (FileReader reader = new FileReader("products.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for (Object o : jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String temp_name = (String) jsonObj.get("name");
                if (product.equals(temp_name)) {
                    String stock = (String) jsonObj.get("in_stock");
                    ret = Integer.parseInt(stock);
                    return ret;
                }
            }
        }
        return ret;
    }


}