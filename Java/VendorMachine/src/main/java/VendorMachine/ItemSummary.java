package VendorMachine;

import org.checkerframework.checker.units.qual.C;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class ItemSummary {

    public static class ItemDetail {
        String itemcode;
        String price;
        String name;
        String category;
        String instock;



        public ItemDetail(String itemcode, String price, String name, String category,String instock) {
            this.itemcode = itemcode;
            this.price = price;
            this.category = category;
            this.instock = instock;
            this.name = name;

        }

        public String getItemcode() {
            return this.itemcode;
        }
        public String getPrice() {
            return this.price;
        }
        public String getName() { return this.name; }
        public String getCategory() { return this.category; }
        public String getInstock() { return this.instock; }

    }

    private static ItemSummary itemSummary;
    private final List<ItemDetail> itemDetailList;

    private ItemSummary() {
        this.itemDetailList = new ArrayList<>();
    }

    public static ItemSummary getInstance() {
        if(itemSummary == null) {
            itemSummary = new ItemSummary();
            itemSummary.readFromHistory();
        }

        return itemSummary;
    }

    public void adduser(String itemcode, String price, String name, String category,String instock) {
        if(itemSummary == null) {
            itemSummary = new ItemSummary();
        }

        this.itemDetailList.add(new ItemDetail(itemcode,price,name,category,instock));
    }

    public List<ItemDetail> getUserList() {
        return this.itemDetailList;
    }

    public void readFromHistory() {

        if (itemSummary == null) {
            itemSummary = new ItemSummary();
        }

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("products.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray array = (JSONArray) obj;
            System.out.println(itemDetailList);

            //Iterate over transaction array
            array.forEach( tran -> parseTransactionObject( (JSONObject) tran ) );

        } catch (NullPointerException | IOException | ParseException e) {
            e.printStackTrace();
        }

    }



    public void writeIntoHistory() {
        JSONArray array = new JSONArray();

        for(ItemDetail u : itemDetailList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("item_code", u.getItemcode());
            jsonObject.put("price", u.getPrice());
            jsonObject.put("name", u.getName());
            jsonObject.put("category", u.getCategory());
            jsonObject.put("in_stock", u.getInstock());

            array.add(jsonObject);
        }

        //Write JSON file
        try (FileWriter file = new FileWriter("products.json")) {

            file.write(array.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void parseTransactionObject(JSONObject jsonObject){
        itemDetailList.add(
                new ItemDetail(
                        (String) jsonObject.get("item_code"),
                        (String) jsonObject.get("price"),
                        (String) jsonObject.get("name"),
                        (String) jsonObject.get("category"),
                        (String) jsonObject.get("in_stock")
                )
        );
    }

}
