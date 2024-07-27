package VendorMachine;

import junit.framework.TestCase;
import org.junit.Test;

import javax.swing.*;

public class DisplayLastItemsTest extends TestCase {

    @Test
    public void testDisplay() {
        DisplayLastItems d = new DisplayLastItems();
        JTextArea area = new JTextArea();
        DisplayLastItems.display(area);
    }
}