package com.company;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

/*
Class related to saving the total sum of savings in the savings-goal subsection
 */

public class AddToSavings {
    private static HttpsURLConnection conn;
    public static GenerateUID uuID = new GenerateUID();
    public static void addMoney(int money,String uid) throws Exception {
        BufferedReader reader;
        String line;
        StringBuffer response = new StringBuffer();

        try {
            //region PUT request to add money to savings account
            //NOTE url account/+ACCOUNT HOLDERU UID+/savings-goal/+GOAL UID+/add-money/+Generated UID
            URL url = new URL("https://api-sandbox.starlingbank.com/api/v2/account/6c8ab727-2778-4eb7-8be2-06c5308a5a86/savings-goals/"+ uid+"/add-money/"+uuID.getUUID());
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization","Bearer "+ uuID.baerer);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            //endregion

            //create a JSON object based on the API documentation
            JSONObject info =new JSONObject();
            info.put("currency","GBP");
            info.put("minorUnits",money);
            JSONObject finalobj=new JSONObject();
            JSONObject amount=finalobj.put("amount",info);

            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(amount.toString());
            out.flush();
            out.close();

            //handle response based on status
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException a) {
            a.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }
}
