package R18A_Group7_Asm2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;



public class CoinTest {
    @Test
    public void testConstructCoin(){
        Coin c = new Coin(100.0, 5);
        assertNotNull(c);
    }
    @Test
    public void testCreateList() {
        ArrayList<Coin> coins = Coin.createCoinList();
        assertNotNull(coins);
    }
    @Test
    public void testNotEnoughStock() {
        boolean enough = Coin.stockEnough(100.0, 100);
        assertFalse(enough);
    }
    @Test
    public void testEnoughStock() {
        boolean enough = Coin.stockEnough(100.0, 1);
        assertTrue(enough);
    }
    @Test
    public void testResetDefault() throws IOException {
        Coin.resetCoinDefault();
        HashMap< Double, Integer> amount = Coin.getCurrentAmount();
        assertSame(5, amount.get(100.0));
    }
    @Test
    public void testCoinStockNumber() {
        Coin.resetCoinDefault();
        HashMap< Double, Integer> amount = Coin.getCurrentAmount();
        assertSame(5, amount.get(100.0));
    }
    @Test
    public void testGetChangesNotEnough() throws IOException, ParseException {
        HashMap< Double, Integer> changes = Coin.getChange(100000000.0);
        assertTrue(changes.isEmpty());
    }
    @Test
    public void testUpdateCoin() throws IOException, ParseException {
        Coin.resetCoinDefault();
        HashMap< Double, Integer> amount = Coin.getCurrentAmount();
        assertSame(5, amount.get(100.0));
        Coin.updateCoinStock("100.0", 2 );
        amount = Coin.getCurrentAmount();
        assertSame(7, amount.get(100.0));
        Coin.updateCoinStock("100.0", -2 );
        amount = Coin.getCurrentAmount();
        assertSame(5, amount.get(100.0));
    }
    @Test
    public void testGetChangesEnough() throws IOException, ParseException {
        HashMap< Double, Integer> changes = Coin.getChange(135.56);
        assertSame(1, changes.get(100.0));
        assertSame(1, changes.get(20.0));
        assertSame(1, changes.get(10.0));
        assertSame(1, changes.get(5.0));
        assertSame(1, changes.get(0.5));
        assertSame(1, changes.get(0.05));
        assertSame(1, changes.get(0.01));
        Coin.updateCoinStock("100.0", 1 );
        Coin.updateCoinStock("20.0", 1 );
        Coin.updateCoinStock("10.0", 1 );
        Coin.updateCoinStock("5.0", 1 );
        Coin.updateCoinStock("0.5", 1 );
        Coin.updateCoinStock("0.05", 1 );
        Coin.updateCoinStock("0.01", 1 );
    }
    @Test
    public void testCoinStockUpdateAfterChange() throws IOException, ParseException {
        Coin.resetCoinDefault();
        HashMap< Double, Integer> amount = Coin.getCurrentAmount();
        assertSame(5, amount.get(100.0));
        Coin.getChange(100);
        amount = Coin.getCurrentAmount();
        assertSame(4, amount.get(100.0));
        Coin.updateCoinStock("100.0", 1);
    }
    @Test
    public void testGenerateReport() {
        try{
            Coin.generate_report();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}