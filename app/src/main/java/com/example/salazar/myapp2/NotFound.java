package com.example.salazar.myapp2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static java.security.AccessController.getContext;

/**
 * Created by Salazar on 8/24/2017.
 */

public class NotFound extends Activity{
    String[] down;
    Bundle b;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=this.getIntent().getExtras();
        down=b.getStringArray("androidVersionNames");
        // public void begin(){
        setContentView(R.layout.notfound);
        // initiate a MultiAutoCompleteTextView
        MultiAutoCompleteTextView simpleMultiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.simpleMultiAutoCompleteTextView);
// set adapter to fill the data in suggestion list
        ArrayAdapter<String> versionNames = new ArrayAdapter<String>(NotFound.this, android.R.layout.simple_list_item_1, down);
        simpleMultiAutoCompleteTextView.setAdapter(versionNames);

// set threshold value 1 that help us to start the searching from first character
        simpleMultiAutoCompleteTextView.setThreshold(1);
// set tokenizer that distinguish the various substrings by comma
        simpleMultiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        Alerter alert = new Alerter();
         alert.show();



    }

    public void createprod(View v){
        MultiAutoCompleteTextView myEditText = (MultiAutoCompleteTextView) findViewById(R.id.simpleMultiAutoCompleteTextView);
        EditText edit = (EditText) findViewById(R.id.editText2);
        String name = edit.getText().toString();

        EditText edit2 = (EditText) findViewById(R.id.editText3);
        String supermercado = edit2.getText().toString();

        String ingredientes = myEditText.getText().toString();
        b.putString("ingredientes",ingredientes);
        uploadimage upload = new uploadimage(ingredientes,name,supermercado);
        upload.execute();



    }

    private class Alerter extends MainActivity{

        public void show(){
            final String text = "Hello toast!";

            runOnUiThread(new Runnable() {
                public void run()
                {
                 //   Toast.makeText(NotFound.this, text, Toast.LENGTH_SHORT).show();
                    final AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(NotFound.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(NotFound.this);
                    }
                    builder.setTitle("Produto não encontrado")
                            .setMessage("Este produto não foi encontrado na nossa base de dados. Contudo, pode adicioná-lo manualmente.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    setContentView(R.layout.continuous_scan);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });
        }

    }

    private class uploadimage extends AsyncTask<Void, Void, Void>{

        int code;
        String ingredientes;
        String supermercado;
        String name;

       public uploadimage(String ingredientes,String name,String supermercado){
            this.ingredientes = ingredientes;
           this.name = name;
           this.supermercado = supermercado;

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String android_id = Settings.Secure.getString(NotFound.this.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                 String urli;
                urli = "http://porto-quest.herokuapp.com/api/v2/uploadata/" + "?device="+ android_id + "&ingredientes=" + URLEncoder.encode(this.ingredientes,"UTF-8") + "&scan=" + b.getString("scan") + "&supermarket=" + URLEncoder.encode(this.supermercado,"UTF-8") + "&name=" + URLEncoder.encode(this.name,"UTF-8");
                // urli ="http://porto-quest.herokuapp.com/api/getusers";
                URL objecto=new URL(urli);
                HttpURLConnection cone = (HttpURLConnection) objecto.openConnection();
        //        cone.setDoInput(true);
                cone.setConnectTimeout(10 * 1000);
                cone.setReadTimeout(10 * 1000);
                cone.connect();
                int responseCode = cone.getResponseCode();
                this.code = responseCode;

            }
            catch (Exception e){
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(this.code == HttpURLConnection.HTTP_OK){
                Intent intenti = new Intent(NotFound.this,Found.class);
                intenti.putExtras(b);
                startActivity(intenti);
            }
            else{
                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        //   Toast.makeText(NotFound.this, text, Toast.LENGTH_SHORT).show();
                        final AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(NotFound.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(NotFound.this);
                        }
                        builder.setTitle("Erro")
                                .setMessage("Verifique a sua conexão à internet e tente novamente.")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                });

            }

        }
    }

}