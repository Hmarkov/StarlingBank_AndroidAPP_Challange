package com.example.sbank_test;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity{

    public static String goaluid;

    public static String getGoaluid() {
        return goaluid;
    }

    public static void setGoaluid(String goaluid) {
        MainActivity.goaluid = goaluid;
    }

    public static int getWeeklysaving() {
        return weeklysaving;
    }

    public static void setWeeklysaving(int weeklysaving) {
        MainActivity.weeklysaving = weeklysaving;
    }

    public static int weeklysaving;
    private AddToSavings add;
    private AccountFeed accfeed;
    private SavingsGoal save;
    Button btnadd;
    public static TextView lbl_num,lbl_quest;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lbl_num=findViewById(R.id.lbl_savings);
        lbl_quest=findViewById(R.id.lbl_question);
        btnadd=findViewById(R.id.btn_transfer);
        save=new SavingsGoal();
        accfeed=new AccountFeed();
        add=new AddToSavings();
        accfeed.execute();
        btnadd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                try {
                    save.execute();
                    add.execute();
                    btnadd.setText("Thank you for using StarlingBank");
                    lbl_num.setText("0");
                    lbl_quest.setText("Transaction completed");
                    btnadd.setEnabled(false);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}