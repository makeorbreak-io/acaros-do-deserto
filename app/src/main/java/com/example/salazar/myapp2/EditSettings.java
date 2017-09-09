package com.example.salazar.myapp2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.salazar.myapp2.R.layout.settings;
import static java.lang.Thread.sleep;

/**
 * Created by caesar on 9/8/17.
 */

public class EditSettings extends Activity {
    ArrayList<String> ingredientsArray;
    Bundle b;
    ProgressDialog pd;
    ViewGroup checkboxContainer;

    protected void onCreate(Bundle savedInstanceState) {
        ingredientsArray = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        b=this.getIntent().getExtras();
        checkboxContainer = (ViewGroup) findViewById(R.id.checkbox_container);
        new JsonTask().execute("http://9d4ae7fe.ngrok.io/api/allergens");
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(EditSettings.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            ArrayList<String> tempArray = new ArrayList<String>();
            String tempString = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    String[] tokens = line.split(",");
                    for (int i=0; i<tokens.length;i++) {
                        if(tokens[i].toLowerCase().contains("name")) {
                            tempString = tokens[i].substring(8);
                            tempString = tempString.substring(0, tempString.length() - 1);
                            ingredientsArray.add(tempString);
                        }
                    }
                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            for (int i = 0; i < ingredientsArray.size(); i++) {
                CheckBox checkBox = new CheckBox(EditSettings.this);
                checkBox.setText(ingredientsArray.get(i));
                checkboxContainer.addView(checkBox);
            }

        }
    }

    public void saveSettings(View view) {
        ArrayList<String> output = new ArrayList<String>();
        int z= checkboxContainer.getChildCount();
        for (int i=0; i< checkboxContainer.getChildCount(); i++)
        {

                CheckBox checkBox = (CheckBox) checkboxContainer.getChildAt(i);
                if (checkBox.isChecked()) {
                    output.add(checkBox.getText().toString());
                }

        }
        saveAllergies(output);
    }



    public boolean saveAllergies(ArrayList<String> sKey)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(EditSettings.this);
        SharedPreferences.Editor mEdit1 = sp.edit();
    /* sKey is an array */
        mEdit1.putInt("Status_size", sKey.size());

        for(int i=0;i<sKey.size();i++)
        {
            mEdit1.remove("Status_" + i);
            mEdit1.putString("Status_" + i, sKey.get(i));
        }

        return mEdit1.commit();
    }
}

