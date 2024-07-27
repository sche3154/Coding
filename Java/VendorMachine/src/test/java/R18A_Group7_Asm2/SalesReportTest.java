package R18A_Group7_Asm2;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SalesReportTest {

    @Test
    public void generate_report() {
        try{
            SalesReport.generate_report();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}