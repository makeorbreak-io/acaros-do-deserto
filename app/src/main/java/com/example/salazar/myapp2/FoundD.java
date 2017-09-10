package com.example.salazar.myapp2;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Salazar on 9/10/2017.
 */

public class FoundD extends Activity{

    Bundle f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found);
        f=this.getIntent().getExtras();
        getActionBar().setTitle("Ingredientes");
        httpingred temp = new httpingred();
        temp.execute();
    }

    private class httpingred extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String urli;
                urli = "http://porto-quest.herokuapp.com/api/product/"+ f.getString("scan");
                URL objecto=new URL(urli);
                final JSONObject p;
                HttpURLConnection cone = (HttpURLConnection) objecto.openConnection();
                cone.setDoInput(true);
                cone.setConnectTimeout(10 * 1000);
                cone.setReadTimeout(10 * 1000);
                cone.connect();
                int responseCode = cone.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = cone.getInputStream();
                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();

                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null)
                        responseStrBuilder.append(inputStr);
                    p = new JSONObject(responseStrBuilder.toString());


                }

            }
            catch (Exception e){
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


}
