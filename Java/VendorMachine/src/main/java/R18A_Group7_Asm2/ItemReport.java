package R18A_Group7_Asm2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

public class ItemReport extends JFrame{
    public static void main(String[] args) throws IOException {
        ItemReport.generate_report();
//        int quantity = SalesReport.get_sold_quantity("Mineral Water");
//        System.out.println(quantity);
    }
    public static void generate_report() throws IOException {
        List<Object[]> data = new ArrayList<Object[]>();
        data.add(new Object[] {"Item_code","," ,"Item_name", ",", "price",",","category",",","in_stock", "\n"});
        // read product.json first --- get all item info
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("products.json")) {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for(Object o:jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String name = (String) jsonObj.get("name");
                String code = (String) jsonObj.get("item_code");
                String  price = (String) jsonObj.get("price");
                String  category = (String) jsonObj.get("category");
                String  in_stock = (String) jsonObj.get("in_stock");
                data.add(new Object[] {"\t"+code,",",name,",",price,",",category,",",in_stock, "\n"});
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        //write to report csv file
        try(FileWriter report = new FileWriter("item_report.csv")){
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
