package com.example.salazar.myapp2;

        import android.content.DialogInterface;
        import android.content.Intent;
        import android.hardware.Camera;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.ActionBarActivity;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.Toast;

        import com.google.zxing.client.android.Intents;
        import com.google.zxing.integration.android.IntentIntegrator;
        import com.google.zxing.integration.android.IntentResult;
        import com.journeyapps.barcodescanner.CameraPreview;

        import com.example.salazar.myapp2.R;



public class MainActivity extends AppCompatActivity {

    CustomAdapter adapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        adapter = new CustomAdapter(this);
        viewPager.setAdapter(adapter);

    }


    public void scanContinuous(View view) {
        Intent intent = new Intent(this, ContinuousCaptureActivity.class);
      //  ContinuousCaptureActivity temp = new ContinuousCaptureActivity();
      //  temp.setContext(this);
      //  temp.begin();
       startActivity(intent);
    }

    public void scanWithTimeout(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setTimeout(8000);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void settings(View view) {
        Intent intent = new Intent(this, EditSettings.class);
        startActivity(intent);
    }

}
