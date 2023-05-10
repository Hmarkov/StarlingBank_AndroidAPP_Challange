package com.company;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/*
Class  with a function to fetch all the transactions from the feed
 */
public class AccountFeed {
    private static HttpsURLConnection conn;
    static GenerateUID uuID = new GenerateUID();

    public static String Feed(String min, String max) {
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        String responseBody = null;

        try {
            //region GET request to fetch all transaction from the trasaction feed
            //NOTE url account/+ACCOUNT HOLDER UID+/category/+DEFAULT CATEGORY UID+/transactions-between[subquery for (mintransactiontimestamp to maxtransactiontimestamp)]
            URL url = new URL("https://api-sandbox.starlingbank.com/api/v2/feed/account/6c8ab727-2778-4eb7-8be2-06c5308a5a86/category/b581456d-ba76-4c7f-a1b4-fb1ff57dc0a8/transactions-between?minTransactionTimestamp="+min+"T00:00:05.715Z&maxTransactionTimestamp="+max+"T23:59:00.715Z");
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization","Bearer "+uuID.baerer);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            //endregion

            //handle response based on status
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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return responseBody;
    }

}
