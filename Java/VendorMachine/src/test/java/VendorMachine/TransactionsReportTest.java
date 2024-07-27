package VendorMachine;

import org.junit.Test;

import javax.swing.table.DefaultTableModel;

import static org.junit.Assert.*;

public class TransactionsReportTest {


    @Test
    public void write_txt() {
        TransactionsReport.write_txt();

    }

    @Test
    public void write_to_file() {
    }

    @Test
    public void get_current_date() {
        assertNotNull(TransactionsReport.get_current_date());
    }

    @Test
    public void item_list() {
        String[][] data = {};
        String[] columnNames = {"Item(s)", "Quantity", "price"};
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        Object[] row = {"Mineral water", "2", "2.00"};
        tableModel.addRow(row);
        assertNotNull(TransactionsReport.item_list(tableModel));
        assertNotNull(TransactionsReport.item_list(null));

    }
}