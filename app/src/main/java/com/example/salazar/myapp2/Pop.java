package com.example.salazar.myapp2;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Salazar on 9/7/2017.
 */

public class Pop extends Activity{
    Bundle c;
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop);
        c = this.getIntent().getExtras();
        DisplayMetrics dm =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

      /*  LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(LinearLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
*/      getWindow().setLayout((int) (width*0.8),(int) (height * 0.6));
        TextView t=(TextView)findViewById(R.id.textView3);
        TextView t2=(TextView)findViewById(R.id.textView2);
        t.setText(c.getString("comp"));
        t2.setText("cenas");

        t.setTextSize(20);
        t.setTextColor(Color.WHITE);

        httpdo temp = new httpdo();
        temp.execute();

  //      t.setLayoutParams(params);
   //     t2.setLayoutParams(params);

         // RelativeLayout rl = (RelativeLayout)findViewById(R.id.lay);
          //rl.setBackgroundColor(Color.RED);

    }

    private class httpdo extends AsyncTask<Void ,  Void,  Void>{


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
           try{
               String url = "http://9d4ae7fe.ngrok.io/api";
                       url += "/ingredient/" + c.getString("comp");

               URL object=new URL(url);
               HttpURLConnection con = (HttpURLConnection) object.openConnection();
               final JSONObject p;
          //     con.setConnectTimeout(10 * 1000);
           //    con.setReadTimeout(10 * 1000);
               con.setDoInput(true);
               int responseCode = con.getResponseCode();
               con.connect();
               if(responseCode == HttpURLConnection.HTTP_OK) {
                   InputStream in = con.getInputStream();
                   BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                   StringBuilder responseStrBuilder = new StringBuilder();

                   String inputStr;
                   while ((inputStr = streamReader.readLine()) != null)
                       responseStrBuilder.append(inputStr);
                   p = new JSONObject(responseStrBuilder.toString());
               }
               else{
                   final GradientDrawable drawable = new GradientDrawable();
                   drawable.setShape(GradientDrawable.RECTANGLE);
                   drawable.setStroke(5, Color.GREEN);
                   drawable.setColor(Color.BLACK);
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           getWindow().setBackgroundDrawable(drawable);
                       }
                   });
                   return null;

               }
                       try {
                           if (p.getString("danger").equals("VD")) {
                               final GradientDrawable drawable = new GradientDrawable();
                               drawable.setShape(GradientDrawable.RECTANGLE);
                               drawable.setStroke(5, Color.RED);
                               drawable.setColor(Color.BLACK);
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       TextView t=(TextView)findViewById(R.id.textView3);
                                       TextView t2=(TextView)findViewById(R.id.textView2);
                                       t.setText(c.getString("comp"));
                                       try {
                                           t2.setText(p.getString("description"));
                                       } catch (JSONException e) {
                                           e.printStackTrace();
                                       }
                                       getWindow().setBackgroundDrawable(drawable);
                                   }
                               });
                           }
                           else if (p.getString("danger").equals("D")){
                               GradientDrawable drawable = new GradientDrawable();
                               drawable.setShape(GradientDrawable.RECTANGLE);
                               drawable.setStroke(5, Color.rgb(255,69,0));
                               drawable.setColor(Color.BLACK);
                               getWindow().setBackgroundDrawable(drawable);
                           }
                           else if(p.getString("danger").equals("T")){
                               GradientDrawable drawable = new GradientDrawable();
                               drawable.setShape(GradientDrawable.RECTANGLE);
                               drawable.setStroke(5, Color.YELLOW);
                               drawable.setColor(Color.BLACK);

                               getWindow().setBackgroundDrawable(drawable);
                           }
                           else {
                               GradientDrawable drawable = new GradientDrawable();
                               drawable.setShape(GradientDrawable.RECTANGLE);
                               drawable.setStroke(5, Color.GREEN);
                               drawable.setColor(Color.BLACK);
                               getWindow().setBackgroundDrawable(drawable);

                           }

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

           }
           catch (Exception e){
               e.printStackTrace();
           }


            return null;
        }
    }

}
