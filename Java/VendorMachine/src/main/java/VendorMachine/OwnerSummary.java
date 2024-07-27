package VendorMachine;

import org.checkerframework.checker.units.qual.C;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OwnerSummary {
    public static class CancelledTransaction {
        String timeStamp;
        String reason;
        String username;

        public CancelledTransaction(String timeStamp, String reason, String username) {
            this.timeStamp = timeStamp;
            this.reason = reason;
            this.username = username;

        }

        public String getTimeStamp() {
            return this.timeStamp;
        }

        public String getReason() {
            return this.reason;
        }

        public String getUsername() {
            return this.username;
        }

    }

    private static OwnerSummary ownerSummary;
    private final List<CancelledTransaction> transactionList;

    private OwnerSummary() {
        this.transactionList = new ArrayList<>();
    }

    public static OwnerSummary getInstance() {
        if(ownerSummary == null) {
            ownerSummary = new OwnerSummary();
            ownerSummary.readFromHistory();
        }

        return ownerSummary;
    }

    public void addTransaction(String timeStamp, String reason, String username) {
        if(ownerSummary == null) {
            ownerSummary = new OwnerSummary();
        }

        this.transactionList.add(new CancelledTransaction(timeStamp, reason, username));
    }

    public List<CancelledTransaction> getTransactionList() {
        return this.transactionList;
    }

    public void readFromHistory() {

        if (ownerSummary == null) {
            ownerSummary = new OwnerSummary();
        }

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("cancelledTransactions.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray array = (JSONArray) obj;
            System.out.println(transactionList);

            //Iterate over transaction array
            array.forEach( tran -> parseTransactionObject( (JSONObject) tran ) );

        } catch (NullPointerException | IOException | ParseException e) {
            e.printStackTrace();
        }

    }



    public boolean writeIntoHistory() {
        JSONArray array = new JSONArray();

        for(CancelledTransaction c : transactionList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", c.getTimeStamp());
            jsonObject.put("reason", c.getReason());
            jsonObject.put("user", c.getUsername());

            array.add(jsonObject);
        }

        //Write JSON file
        try (FileWriter file = new FileWriter("cancelledTransactions.json")) {

            file.write(array.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    private void parseTransactionObject(JSONObject jsonObject){
        transactionList.add(
                new CancelledTransaction(
                        (String) jsonObject.get("date"),
                        (String) jsonObject.get("reason"),
                        (String) jsonObject.get("user")
                )
        );
    }


    public boolean generateReport() throws IOException {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Date","," ,"Reason", ",", "User", "\n"});

        for(CancelledTransaction c : transactionList) {
            data.add(new String[]{c.getTimeStamp(), ",", c.getReason(), ",", c.getUsername(), "\n"});
        }


        try(FileWriter report = new FileWriter("cancel_report.csv")){
            for(String[] array : data) {
                for(String s : array) {
                    report.write(s);
                }
            }
            report.flush();
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }

        Desktop.getDesktop().open(new File("cancel_report.csv"));
        return true;
    }





}
