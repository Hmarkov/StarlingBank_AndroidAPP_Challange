package com.company;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    //The class GenerateUID contains the Access Token

    //timeframe for transactions feed
    public static String min="2022-03-01";
    public static String max="2022-03-12";

    //Function to round up to the nearest bigger number based on the transactions
    public static Integer roundCalculator(String responseBody) {
        double res = 0.0;
        //create a JSON object array to iterate through the transactions digits
        JSONObject obj = new JSONObject(responseBody);
        JSONArray feedItems = obj.getJSONArray("feedItems");

        for (int i = 0; i < feedItems.length(); i++) {
            JSONObject amount = (JSONObject) feedItems.getJSONObject(i).get("amount");
            Integer minorUnits = (Integer) amount.get("minorUnits");
            double convert= Double.valueOf(minorUnits)/100;
            res += (Math.ceil(convert))-convert;
        }
        System.out.println("Total savings: "+res);
        return Integer.parseInt(String.valueOf(String.format("%.0f", res*100)));
    }


    public static void main(String[] args) throws Exception {
        AccountFeed accountFeed = new AccountFeed();
        SavingsGoal sg= new SavingsGoal();
        AddToSavings add=new AddToSavings();
        add.addMoney( roundCalculator(accountFeed.Feed(min,max)),sg.getGoals());
        sg.getGoals();
    }


}
