package com.company;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;


/*
This class contains functions related to the savings-goal sub-section
createGoal method is used within the information retrieval if the goal already exists
getGoals method returns a goal generated UID
deleteGoal method deletes a specific goal when called*
* when i was doing the createGoal function i kind of overused it so i had to delete some of goals created
 */
public class SavingsGoal{

    private static HttpsURLConnection conn;
    public static GenerateUID uuID = new GenerateUID();
    public static void createGoal() throws Exception {
        BufferedReader reader;
        String line;
        StringBuffer response = new StringBuffer();
        try {
            //region PUT request to create a goal under the savings-goal section
            //NOTE url account/+ACCOUNT HOLDERU UID+/savings-goal/
            URL url = new URL("https://api-sandbox.starlingbank.com/api/v2/account/6c8ab727-2778-4eb7-8be2-06c5308a5a86/savings-goals/");
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization","Baerer "+uuID.baerer);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            //endregion


            //create a JSON object based on the API documentation
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

            //handle response based on status
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
            //region GET request to fetch information about all goals under subsection-savings-goal
            //NOTE url account/+ACCOUNT HOLDERU UID+/savings-goal/
            URL url = new URL("https://api-sandbox.starlingbank.com/api/v2/account/6c8ab727-2778-4eb7-8be2-06c5308a5a86/savings-goals/");
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

            //create a JSON object based on the API documentation
            JSONObject full=new JSONObject(response.toString());
            JSONArray savingsGoalList= full.getJSONArray("savingsGoalList");
            JSONObject goalinfo=new JSONObject(savingsGoalList.getJSONObject(0).toString());
            JSONObject totalSaved= goalinfo.getJSONObject("totalSaved");

            obj=new GoalObj(goalinfo.getString("savingsGoalUid"),goalinfo.getString("name"), totalSaved.getString("currency"), totalSaved.getInt("minorUnits"));
            responseBody = response.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        if (responseBody=="")
        {
            createGoal();
        }
        return obj.getGoaluid();
    }

    public static void deleteGoal() throws Exception {
        BufferedReader reader;
        String line;
        StringBuffer response = new StringBuffer();
        try {
            //region DELETE request to delete specific goal under subsection-savings-goal
            //NOTE url account/+ACCOUNT HOLDERU UID+/savings-goal/+GOAL UID
            URL url = new URL("https://api-sandbox.starlingbank.com/api/v2/account/6c8ab727-2778-4eb7-8be2-06c5308a5a86/savings-goals/d0e36322-01a4-42b3-a23e-179a41c60584");
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization","Bearer "+ uuID.baerer);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            //endregion

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
            System.out.println(response);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException a) {
            a.printStackTrace();
        } finally {
            conn.disconnect();
        }

    }

}
