package com.example.sbank_test;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;


import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
public class SavingsGoal extends AsyncTask<String, String, String> {

    private static HttpsURLConnection conn;
    public static GenerateUID uuID = new GenerateUID();
    public static void createGoal() throws Exception {
        BufferedReader reader;
        String line;
        StringBuffer response = new StringBuffer();

        try {
            URL url = new URL("https://api-sandbox.starlingbank.com/api/v2/account/6c8ab727-2778-4eb7-8be2-06c5308a5a86/savings-goals/");
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization","Baerer "+uuID.baerer);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            JSONObject 	target =new JSONObject();
            target.put("currency","GBP");
            target.put("minorUnits",0);
            JSONObject finalobj=new JSONObject();
            finalobj.put("name", "weekly savings");
            finalobj.put("currency", "GBP");
            finalobj.put("target",target);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(finalobj.toString());
            out.flush();
            out.close();

            int status = conn.getResponseCode();
            System.out.println(status);
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
            System.out.println(response.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException a) {
            a.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }
    public static String getGoals() throws Exception {
        BufferedReader reader;
        String line;
        StringBuffer response = new StringBuffer();
        String responseBody = null;
        GoalObj obj = null;
        try {
            URL url = new URL("https://api-sandbox.starlingbank.com/api/v2/account/6c8ab727-2778-4eb7-8be2-06c5308a5a86/savings-goals/");

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
            System.out.println(response);
            JSONObject full=new JSONObject(response.toString());
            JSONArray savingsGoalList= full.getJSONArray("savingsGoalList");
            JSONObject goalinfo=new JSONObject(savingsGoalList.getJSONObject(0).toString());
            JSONObject totalSaved= goalinfo.getJSONObject("totalSaved");
            obj=new GoalObj(goalinfo.getString("savingsGoalUid"),goalinfo.getString("name"), totalSaved.getString("currency"), totalSaved.getInt("minorUnits"));
            responseBody = response.toString();
            if (responseBody=="")
            {
                createGoal();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
            return obj.getGoaluid();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            return getGoals();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        MainActivity.setGoaluid(result);
    }
}
