package R18A_Group7_Asm2;

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

public class TransactionsReport {
//    public static void main(String[] args){
//        String date = get_current_date();
//        String item = item_list(VendingMachine.tableModel);
//        double paid = 12;
//        double change = 8;
//        String method = "Cash";
//        write_to_file(date, item, paid, change, method);
//        write_txt();
//    }

    public static void write_txt() {
        List<Object[]> data = new ArrayList<Object[]>();
        data.add(new Object[] {"date","," ,"item", ",", "money paid", ",","returned change",",","method","\n"});
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("transactions.json")) {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for(Object o:jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                data.add(new Object[]{
                        "\t",
                        jsonObj.get("date and time"),
                        ",",
                        jsonObj.get("item"),
                        ",",
                        jsonObj.get("money paid"),
                        ",",
                        jsonObj.get("returned change"),
                        ",",
                        jsonObj.get("payment method"),
                        "\n"
                }
                );
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        try(FileWriter report = new FileWriter("transactions.csv")){
            for(Object[] ol : data){
                for(Object o:ol){
                    report.write(o.toString());
                }
            }
            report.flush();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write_to_file(String date, String item, double paid, double change, String method){
        JSONObject transaction = new JSONObject();
        transaction.put("date and time", date);
        transaction.put("item", item);
        transaction.put("money paid", paid);
        transaction.put("returned change", change);
        transaction.put("payment method", method);

        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("transactions.json")) {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            jsonArray.add(transaction);
            try (FileWriter file = new FileWriter("transactions.json")) {
                file.write(jsonArray.toJSONString());
                file.flush();
            }catch(IOException e){
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        write_txt();
    }

    public static String get_current_date(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date_and_time = dtf.format(now).toString();
        return date_and_time;
    }

    public static String item_list(DefaultTableModel bag){
        String item = "";
        if(!(bag == null)){
            for (int i=0; i < bag.getRowCount(); i++){
                if(!bag.getValueAt(i,0).toString().equals("Total price")){
                    item += bag.getValueAt(i,0).toString();
                    item += "\r\n";
                }
            }
        }
        item = String.format("\"%s\"", item);
        return item;
    }
}
