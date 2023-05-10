package com.example.sbank_test;
import android.os.AsyncTask;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

public class AddToSavings extends AsyncTask<MainActivity, String, String> {

    private static HttpsURLConnection conn;
    public static GenerateUID uuID = new GenerateUID();
    public static String addMoney() throws Exception {
        BufferedReader reader;
        String line;
        StringBuffer response = new StringBuffer();

        try {
            URL url = new URL("https://api-sandbox.starlingbank.com/api/v2/account/6c8ab727-2778-4eb7-8be2-06c5308a5a86/savings-goals/"+ MainActivity.getGoaluid()+"/add-money/"+uuID.getUUID());
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization","Bearer "+ uuID.baerer);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            JSONObject info =new JSONObject();
            info.put("currency","GBP");
            info.put("minorUnits",MainActivity.getWeeklysaving());
            JSONObject finalobj=new JSONObject();
            JSONObject amount=finalobj.put("amount",info);

            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(amount.toString());
            out.flush();
            out.close();

            int status = conn.getResponseCode();

            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

            }
            return String.valueOf(status);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException a) {
            a.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return null;
    }



    @Override
    protected String doInBackground(MainActivity... mainActivities) {
        try {
            return addMoney();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
