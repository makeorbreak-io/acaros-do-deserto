package com.example.salazar.myapp2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

/**
 * Created by caesar on 9/9/17.
 */

public class MyList extends ListActivity {
    ArrayList<String> ingredientsArray;
    ProgressDialog pd;

    /** Called when the activity is first created. */
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        ingredientsArray = new ArrayList<String>();
        // create an array of Strings, that will be put to our ListActivity
        new MyList.JsonTask().execute("http://9d4ae7fe.ngrok.io/api/allergens");
    }

    private List<Model> getModel() {
        List<Model> list = new ArrayList<Model>();
        for (int i=0; i< ingredientsArray.size(); i++)
        {
            list.add(get(ingredientsArray.get(i)));
        }
        return list;
    }

    private Model get(String s) {
        return new Model(s);
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MyList.this);
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
            ArrayAdapter<Model> adapter = new InteractiveArrayAdapter(MyList.this,
                    getModel());
            setListAdapter(adapter);
        }
    }
}