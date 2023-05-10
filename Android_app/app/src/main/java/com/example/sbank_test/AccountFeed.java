package com.example.sbank_test;
import android.os.AsyncTask;

import com.example.sbank_test.GenerateUID;
import com.example.sbank_test.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
public class AccountFeed extends AsyncTask<String, String, Integer>{
    public static GenerateUID uuID = new GenerateUID();
    @Override
    protected Integer doInBackground(String... strings) {
        HttpsURLConnection conn = null;

        String responseBody = null;

        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();

        try {
            URL url = new URL("https://api-sandbox.starlingbank.com/api/v2/feed/account/6c8ab727-2778-4eb7-8be2-06c5308a5a86/category/b581456d-ba76-4c7f-a1b4-fb1ff57dc0a8/transactions-between?minTransactionTimestamp=2022-02-09T20:17:19.715Z&maxTransactionTimestamp=2022-06-01T22:17:19.715Z");
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization","Bearer "+uuID.baerer);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            int status = conn.getResponseCode();
            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }
            responseBody = responseContent.toString();


            double res = 0.0;
            JSONObject obj = new JSONObject(responseBody);
            JSONArray feedItems = obj.getJSONArray("feedItems");

            for (int i = 0; i < feedItems.length(); i++) {
                JSONObject amount = (JSONObject) feedItems.getJSONObject(i).get("amount");
                Integer minorUnits = (Integer) amount.get("minorUnits");
                double convert= Double.valueOf(minorUnits)/100;
                res += (Math.ceil(convert))-convert;
            }
            int r=Integer.parseInt(String.valueOf(String.format("%.0f", res*100)));
            return Integer.parseInt(String.valueOf(String.format("%.0f", res*100)));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        MainActivity.setWeeklysaving(result);
        Double convert= Double.valueOf(result)/100;
        MainActivity.lbl_num.setText(convert.toString());
    }
}
