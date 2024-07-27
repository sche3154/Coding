package R18A_Group7_Asm2;

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


public class SalesReport {
    public static void main(String[] args) throws IOException {
        SalesReport.generate_report();
//        int quantity = SalesReport.get_sold_quantity("Mineral Water");
//        System.out.println(quantity);
    }
    public static void generate_report() throws IOException {
        List<Object[]> data = new ArrayList<Object[]>();
        data.add(new Object[] {"Item_code","," ,"Item_name", ",", "Quantity sold", "\n"});
        // read product.json first --- get all item info
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("products.json")) {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for(Object o:jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String name = (String) jsonObj.get("name");
                String code = (String) jsonObj.get("item_code");
                int quantity = get_sold_quantity(name);
                data.add(new Object[] {"\t"+code,",",name,",",quantity, "\n"});
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        //write to report csv file
        try(FileWriter report = new FileWriter("sales_report.csv")){
            for(Object[] ol : data){
                for(Object o:ol){
                    report.write(o.toString());
                }
            }
            report.flush();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public static int get_sold_quantity(String item_name){
        JSONParser jsonParser = new JSONParser();
        int quantity = 0;
        try (FileReader reader = new FileReader("purchase.json")) {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for(Object o:jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                if (item_name.equals(jsonObj.get("item_name").toString())) {
                    int purq = Integer.parseInt(jsonObj.get("quantity").toString());
                    quantity += purq;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return quantity;
    }
}
