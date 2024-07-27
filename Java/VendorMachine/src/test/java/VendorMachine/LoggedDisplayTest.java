package VendorMachine;

import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class LoggedDisplayTest {

    @Test
    public void display() {
        LoggedDisplay ld = new LoggedDisplay();
        JTextArea area = new JTextArea();
        String username = "faker";
        LoggedDisplay.display(area, username);
    }
}