package R18A_Group7_Asm2;

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