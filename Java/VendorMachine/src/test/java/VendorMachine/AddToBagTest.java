package VendorMachine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.io.IOException;

import static org.junit.Assert.*;

public class AddToBagTest {

    @Test
    public void addText() throws Exception {
        AddToBag atb = new AddToBag();
        JTextField tx = new JTextField();
        String kinds[]={"Mineral Water","Sprite","Coca cola","Pepsi","Juice"};
        JComboBox cb = new JComboBox(kinds);
        cb.setSelectedItem("Mineral Water");
        String[][] data = {};
        String[] columnNames = {"Item(s)", "Quantity", "price"};
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);

        atb.addText(tx, cb, tableModel);

        // run normally
        tx.setText("1");
        atb.addText(tx, cb, tableModel);
        assertEquals(1,tableModel.getRowCount());
        assertFalse(atb.getIsExist());

        tx.setText("1");
        assertEquals(1,tableModel.getRowCount());
//        assertEquals("Mineral Water", tableModel.getValueAt(1,0));
//        assertEquals("Mineral Water", cb.getSelectedItem().toString());
        atb.addText(tx, cb, tableModel);
        assertTrue(atb.getIsExist());

        //throw exception
        tx.setText("0");
        atb.addText(tx, cb, tableModel);

        tx.setText("-1");
        atb.addText(tx, cb, tableModel);

        tx.setText("100");
        atb.addText(tx, cb, tableModel);

        tx.setText("q");
        atb.addText(tx, cb, tableModel);

    }

    @Test
    public void setIsExist() {
        AddToBag atb = new AddToBag();
        atb.setIsExist();
        assertTrue(atb.getIsExist());
    }

    @Test
    public void Reduce_stock() {
        try{
            String[][] data = {};
            String[] columnNames = {"Item(s)", "Quantity", "price"};
            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
            Object[] row = {"Mineral Water", "2", "2.00"};
            tableModel.addRow(row);
            AddToBag.reduce_stock(tableModel);
            Product.updateProductStock("Mineral Water", 2);
            AddToBag.reduce_stock(null);
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
