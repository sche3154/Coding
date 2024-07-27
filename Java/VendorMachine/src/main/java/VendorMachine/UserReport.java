package VendorMachine;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.table.DefaultTableModel;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserReport {
    public static void main(String[] args) throws IOException {
        UserReport.generate_report();
//        int quantity = SalesReport.get_sold_quantity("Mineral Water");
//        System.out.println(quantity);
    }
    public static void generate_report() throws IOException {
        List<Object[]> data = new ArrayList<Object[]>();
        data.add(new Object[] {"Acc_name","," ,"Acc_type", "\n"});
        // read product.json first --- get all item info
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("user.json")) {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for(Object o:jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String name = (String) jsonObj.get("account_name");
                String type = (String) jsonObj.get("account_type");
                data.add(new Object[] {"\t"+name,",",type,",", "\n"});
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        //write to report csv file
        try(FileWriter report = new FileWriter("user_report.csv")){
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

}
