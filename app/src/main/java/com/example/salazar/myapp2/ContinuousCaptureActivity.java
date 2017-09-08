package com.example.salazar.myapp2;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import com.example.salazar.myapp2.R;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;


/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class ContinuousCaptureActivity extends Activity{
    private static final String TAG = ContinuousCaptureActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private Button switchFlashlightButton;
    String[] androidVersionNames;
    private ProgressBar spinner;
    private Context ctx;
    Bundle b;

    public void setContext(Context ctx){
        this.ctx = ctx;
    }
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
         //   barcodeView.setStatusText(result.getText());
            beepManager.playBeepSoundAndVibrate();

            //Added preview of scanned barcode
            ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
            spinner.setVisibility(View.VISIBLE);
            Bitmap bm = result.getBitmapWithResultPoints(Color.YELLOW);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] ba = baos.toByteArray();
            String encodedImage = Base64.encodeToString(ba, Base64.DEFAULT);
           // barcodeView.setStatusText(encodedImage);
            barcodeView.setStatusText(result.getText());
           // Intent intent = new Intent();
           // intent.setClass(getApplicationContext(), httphandler.class);
           // startActivity(intent);
            b.putString("scan",result.getText());
            b.putString("image",encodedImage);
           httphandler job = new httphandler(result.getText(),getApplicationContext());
            job.execute();


        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }


    };

    private class httphandler extends AsyncTask<Void ,  Void,  Void>{

        String result;
        int code;
        private Context mContext;


        public httphandler(String result,Context context){
            this.result = result;
            mContext = context;
        }

        public int getcode(){
            return this.code;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String url,url2;
                url="http://porto-quest.herokuapp.com/api/v2/prodexists/";
                url2 = "http://porto-quest.herokuapp.com/api/v2/geting";
                url = url + "?scan="+result;

                URL object=new URL(url);
                URL object2=new URL(url2);
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                HttpURLConnection con2 = (HttpURLConnection) object2.openConnection();
                con.setConnectTimeout(10 * 1000);
                con.setReadTimeout(10 * 1000);
                con2.setConnectTimeout(10 * 1000);
                con2.setReadTimeout(10 * 1000);
                con2.setDoInput(true);
                int responseCode = con.getResponseCode();
                con.connect();
                con2.connect();
                InputStream in = new BufferedInputStream(con2.getInputStream());
                StringBuilder total = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                String line;

                int j =0;
                while ((line = rd.readLine()) != null) {
                    total.append(line);
                }
                String news = total.toString();
                news = news.replace("{", "");
                news = news.replace("}", "");
                androidVersionNames = news.split(",");

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


            }
            else{
                Intent intenti = new Intent(ContinuousCaptureActivity.this, NotFound.class);
                b.putStringArray("androidVersionNames", androidVersionNames);
                intenti.putExtras(b);
                startActivity(intenti);
                /*
                setContentView(R.layout.notfound);
                // initiate a MultiAutoCompleteTextView
                MultiAutoCompleteTextView simpleMultiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.simpleMultiAutoCompleteTextView);
// set adapter to fill the data in suggestion list
                ArrayAdapter<String> versionNames = new ArrayAdapter<String>(ContinuousCaptureActivity.this, android.R.layout.simple_list_item_1, androidVersionNames);
                simpleMultiAutoCompleteTextView.setAdapter(versionNames);

// set threshold value 1 that help us to start the searching from first character
                simpleMultiAutoCompleteTextView.setThreshold(1);
// set tokenizer that distinguish the various substrings by comma
                simpleMultiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(ctx, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(ctx);
                }
                builder.setTitle("Produto não encontrado")
                        .setMessage("Este produto não existe na nossa base de dados. Por favor insira-o manualmente.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
*/
            }

        }

    }


 //   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   // public void begin(){
        setContentView(R.layout.continuous_scan);
        b = new Bundle();

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);

        switchFlashlightButton = (Button)findViewById(R.id.switch_flashlight);
        if (!hasFlash()) {
            switchFlashlightButton.setVisibility(View.GONE);
        }

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight(View view) {
        if (getString(R.string.turn_on_flashlight).equals(switchFlashlightButton.getText())) {
            barcodeView.setTorchOn();
            switchFlashlightButton.setText(R.string.turn_off_flashlight);
        } else {
            barcodeView.setTorchOff();
            switchFlashlightButton.setText(R.string.turn_on_flashlight);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }
/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
     //   return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
    */
}
