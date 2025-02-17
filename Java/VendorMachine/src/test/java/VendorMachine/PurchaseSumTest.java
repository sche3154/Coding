package VendorMachine;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import javax.swing.table.DefaultTableModel;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

public class PurchaseSumTest {

    @Test
    public void userPurchase() {
        String[][] data = {};
        String[] columnNames = {"Item(s)", "Quantity", "price"};
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);

        PurchaseSum.UserPurchase(tableModel);

        Object[] row1 = {"Mineral Water", "2", "2"};
        Object[] row2 = {"Total price", "", "4"};
        tableModel.addRow(row1);
        tableModel.addRow(row2);

        PurchaseSum.UserPurchase(tableModel);

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("purchase.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            jsonArray.remove(jsonArray.size()-1);
            try (FileWriter file = new FileWriter("purchase.json")) {

                file.write(jsonArray.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void writeToFile() {
        PurchaseSum.writeToFile("2412","test",1,1);
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("purchase.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            jsonArray.remove(jsonArray.size()-1);
            try (FileWriter file = new FileWriter("purchase.json")) {

                file.write(jsonArray.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}