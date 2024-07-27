package R18A_Group7_Asm2;

import java.awt.*;
import java.io.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AddToBag extends Component {
    boolean isExist;

    //If the chosen item(s) already exist in the shopping list, and the customer enter its quantity
    //number again, it means that he/she wants to reset the quantity of this item (not buy more).
    public AddToBag(){
        this.isExist = false;
    }

    public void addText(JTextField tx, JComboBox cb, DefaultTableModel bag){
        try {
            if (!tx.getText().equals("")){
                String quan = tx.getText();
                int stock = get_stock(cb.getSelectedItem().toString());
                int intquan = Integer.parseInt(quan);
                double price = get_price(cb.getSelectedItem().toString()) * intquan;
                if(intquan < 0){
                    throw new Exception();
                }

                boolean understock = IsUnderStock(stock, intquan);
                if(understock){
                    String message = String.format("%s is in short supply.\n The current inventory is %d.",
                                         cb.getSelectedItem().toString(), stock);
                    JOptionPane.showMessageDialog(null, message, "Insufficient stock", JOptionPane.ERROR_MESSAGE);
                    tx.setText("");
                    return;
                }

                for (int i=0; i < bag.getRowCount(); i++){
                    if(bag.getValueAt(i, 0).equals(cb.getSelectedItem().toString())){
                        if(intquan == 0){
                            bag.removeRow(i);
                            tx.setText("");
                            return;
                        }
                        setIsExist();
                        bag.setValueAt(quan, i, 1);
                        bag.setValueAt(price, i,2);
                    }

                }
                if(!this.isExist){
                    Object[] row = {cb.getSelectedItem(), quan, price};
                    bag.addRow(row);
                }

            }

        }catch (Exception exception){
            JOptionPane.showMessageDialog(null,
                    "Sorry, Invalid Input!",
                    "Amount Error!",
                    JOptionPane.ERROR_MESSAGE);
        }
        tx.setText("");
    }

    public boolean getIsExist(){
        return this.isExist;
    }

    public void setIsExist(){
        this.isExist = true;
    }

    public int get_stock(String productName){
        JSONParser jsonParser = new JSONParser();

        try(FileReader reader = new FileReader("products.json")){
            // read json file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for(Object o:jsonArray){
                JSONObject jsonObj = (JSONObject) o;
                String name = (String) jsonObj.get("name");
                if(productName.equals(name)){
                    int stock = Integer.parseInt((String) jsonObj.get("in_stock"));
                    return stock;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public double get_price(String productName){
        JSONParser jsonParser = new JSONParser();

        try(FileReader reader = new FileReader("products.json")){
            // read json file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for(Object o:jsonArray){
                JSONObject jsonObj = (JSONObject) o;
                String name = (String) jsonObj.get("name");
                if(productName.equals(name)){
                    double price = Double.parseDouble((String) jsonObj.get("in_stock"));
                    return price;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public boolean IsUnderStock(int stock, int quantity){
        if(stock >= quantity){
            return false;
        }else{
            return true;
        }
    }

    public static double get_totalprice(DefaultTableModel bag){
        double total_price = 0;
        if(bag!=null){
            for (int i=0; i < bag.getRowCount(); i++){
                if(!bag.getValueAt(i,0).toString().equals("Total price")){
                    total_price += Double.parseDouble(bag.getValueAt(i,2).toString());
                }
            }
        }
        return total_price;
    }

    public double set_totalprice(DefaultTableModel bag){
        for (int i=0; i < bag.getRowCount(); i++){
            if(bag.getValueAt(i,0).toString().equals("Total price")){
                bag.removeRow(i);
            }
        }
        double total_price = get_totalprice(bag);
        int row_count = bag.getRowCount();
        Object[] row = {"Total price", "", total_price};
        bag.insertRow(row_count, row);

        return  total_price;
    }

    // do it after order is paid successfully
    public static void reduce_stock(DefaultTableModel bag){
        if(bag!=null){
            for (int i=0; i < bag.getRowCount(); i++){
                if(!bag.getValueAt(i,0).toString().equals("Total price")){
                    String item_name = bag.getValueAt(i,0).toString();
                    int sold_quantity = Integer.parseInt(bag.getValueAt(i,1).toString());
                    JSONParser jsonParser = new JSONParser();
                    try (FileReader reader = new FileReader("products.json")) {
                        //Read JSON file
                        JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
                        for(Object o:jsonArray) {
                            JSONObject jsonObj = (JSONObject) o;
                            String temp_name = (String) jsonObj.get("name");
                            if(item_name.equals(temp_name)) {
                               Product.updateProductStock(item_name, -(sold_quantity));
                            }
                        }

                    } catch(IOException | ParseException e){
                        e.printStackTrace();
                    }
                }

            }
        }
    }


}
