package R18A_Group7_Asm2;

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Coin {
    double value;
    int in_stock;
    final static double THRESHOLD = .0001;

    public Coin(double value, int in_stock){
        this.value = value;
        this.in_stock = in_stock;
    }

    public static ArrayList<Coin> createCoinList(){
        ArrayList<Coin> coinList = new ArrayList<Coin>();
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("coin.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for (Object o : jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                double temp_value = Double.parseDouble((String) jsonObj.get("value"));
                int temp_in_stock = Integer.parseInt(String.valueOf(jsonObj.get("in_stock")));
                Coin temp_coin = new Coin(temp_value, temp_in_stock);
                coinList.add(temp_coin);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return coinList;
    }

    public static void resetCoinDefault() {

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("coin.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for(Object o:jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                jsonObj.put("in_stock", "5");
            }
            try (FileWriter file = new FileWriter("coin.json")) {
                file.write(jsonArray.toJSONString()+"\n");
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


    public static boolean stockEnough(Double value, int outStock){
        ArrayList<Coin> coinList = createCoinList();
        for (Coin c: coinList){
            if(Double.compare(c.value, value) == 0){
                if (outStock > c.in_stock){
                    return false;
                }
                else{
                    return true;
                }
            }
        }
        return false;
    }

    public static HashMap< Double, Integer> getCurrentAmount(){
        ArrayList<Coin> coinList = createCoinList();
        HashMap< Double, Integer> coins = new HashMap<Double, Integer>();
        for (Coin c: coinList) {
            coins.put(c.value, c.in_stock);
        }
        return coins;
    }

    public static void generate_report() throws IOException {
        List<Object[]> data = new ArrayList<Object[]>();
        data.add(new Object[] {"Value","," ,"In Stock", "\n"});
        // read coin.json first --- get all item info
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("coin.json")) {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for(Object o:jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String stock = (String) jsonObj.get("in_stock");
                String value = (String) jsonObj.get("value");
                data.add(new Object[] {"\t"+value,",",stock,",","\n"});
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        //write to report csv file
        try(FileWriter report = new FileWriter("cash_report.csv")){
            for(Object[] ol : data){
                for(Object o:ol){
                    report.write(o.toString());
                }
            }
            report.flush();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }

        Desktop.getDesktop().open(new File("cash_report.csv"));
    }

    public static HashMap< Double, Integer> getChange(double paid_amount) throws IOException, ParseException {


        BigDecimal hundred, fifty, twenty, ten, five, two, one, ofifty, otwenty, oten, ofive, otwo, oone;
        BigDecimal paid = new BigDecimal(String.valueOf(paid_amount));

        //if the return hashmap is empty, it means not enough changes
        HashMap< Double, Integer> changes = new HashMap<Double, Integer>();

        //get detail number of changes for different values
        hundred = paid.subtract(paid.remainder(new BigDecimal("100"))).divide(new BigDecimal("100"));
        paid = paid.remainder(new BigDecimal("100"));
        if (false == stockEnough(100.0, hundred.intValue())){ return changes; }

        fifty = paid.subtract(paid.remainder(new BigDecimal("50"))).divide(new BigDecimal("50"));
        paid = paid.remainder(new BigDecimal("50"));
        if (false == stockEnough(50.0, fifty.intValue())){return changes; }

        twenty = paid.subtract(paid.remainder(new BigDecimal("20"))).divide(new BigDecimal("20"));
        paid = paid.remainder(new BigDecimal("20"));
        if (false == stockEnough(20.0, twenty.intValue())){return changes; }

        ten = paid.subtract(paid.remainder(new BigDecimal("10"))).divide(new BigDecimal("10"));
        paid = paid.remainder(new BigDecimal("10"));
        if (false == stockEnough(10.0, ten.intValue())){return changes; }

        five = paid.subtract(paid.remainder(new BigDecimal("5"))).divide(new BigDecimal("5"));
        paid = paid.remainder(new BigDecimal("5"));
        if (false == stockEnough(5.0, five.intValue())){return changes; }

        two = paid.subtract(paid.remainder(new BigDecimal("2"))).divide(new BigDecimal("2"));
        paid = paid.remainder(new BigDecimal("2"));
        if (false == stockEnough(2.0, two.intValue())){return changes; }

        one = paid.subtract(paid.remainder(new BigDecimal("1"))).divide(new BigDecimal("1"));
        paid = paid.remainder(new BigDecimal("1"));
        if (false == stockEnough(1.0, one.intValue())){return changes; }

        ofifty = paid.subtract(paid.remainder(new BigDecimal("0.5"))).divide(new BigDecimal("0.5"));
        paid = paid.remainder(new BigDecimal("0.5"));
        if (false == stockEnough(0.5, ofifty.intValue())){return changes; }

        otwenty = paid.subtract(paid.remainder(new BigDecimal("0.2"))).divide(new BigDecimal("0.2"));
        paid = paid.remainder(new BigDecimal("0.2"));
        if (false == stockEnough(0.2, otwenty.intValue())){return changes; }

        oten = paid.subtract(paid.remainder(new BigDecimal("0.1"))).divide(new BigDecimal("0.1"));
        paid = paid.remainder(new BigDecimal("0.1"));
        if (false == stockEnough(0.1, oten.intValue())){return changes; }

        ofive = paid.subtract(paid.remainder(new BigDecimal("0.05"))).divide(new BigDecimal("0.05"));
        paid = paid.remainder(new BigDecimal("0.05"));
        if (false == stockEnough(0.05, ofive.intValue())){return changes; }

        otwo = paid.subtract(paid.remainder(new BigDecimal("0.02"))).divide(new BigDecimal("0.02"));
        paid = paid.remainder(new BigDecimal("0.02"));
        if (false == stockEnough(0.02, otwo.intValue())){return changes; }

        oone = paid.subtract(paid.remainder(new BigDecimal("0.01"))).divide(new BigDecimal("0.01"));
        paid = paid.remainder(new BigDecimal("0.01"));
        if (false == stockEnough(0.01, oone.intValue())){return changes; }

        //modify json file for new coin storage

        changes.put(100.0, hundred.intValue());
        changes.put(50.0, fifty.intValue() );
        changes.put(20.0, twenty.intValue());
        changes.put(10.0, ten.intValue());
        changes.put(5.0, five.intValue());
        changes.put(2.0, two.intValue());
        changes.put(1.0, one.intValue());
        changes.put(0.5, ofifty.intValue());
        changes.put(0.2, otwenty.intValue());
        changes.put(0.1, oten.intValue());
        changes.put(0.05, ofive.intValue());
        changes.put(0.02, otwo.intValue());
        changes.put(0.01, oone.intValue());
        for (double i : changes.keySet()) {
            int fill = changes.get(i);
            fill *=  -1;
            updateCoinStock(String.valueOf(i), fill);
        }

        List<BigDecimal> returnChanges = Arrays.asList(hundred, fifty, twenty, ten, five,
                two, one, ofifty, otwenty, oten, ofive, otwo, oone);

        return changes;
    }


    public static boolean updateCoinStock(String value, int fill) throws IOException {

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("coin.json")) {
            //Read JSON file
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for(Object o:jsonArray) {
                JSONObject jsonObj = (JSONObject) o;
                String temp_value = (String) jsonObj.get("value");
                if(value.equals(temp_value)) {
                    int remain = Integer.parseInt((String) jsonObj.get("in_stock"));
                    remain += fill;
                    if (remain < 0){
                        return false;
                    }
                    else {
                        String new_stock = String.valueOf(remain);
                        jsonObj.put("in_stock", new_stock);
                    }
                }
            }
            try (FileWriter file = new FileWriter("coin.json")) {
                file.write(jsonArray.toJSONString()+"\n");
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return true;
    }



}