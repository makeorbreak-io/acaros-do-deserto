package com.example.salazar.myapp2;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
        TextView t=(TextView)findViewById(R.id.textView3);
        t.setText(c.getString("comp"));
        DisplayMetrics dm =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*0.8),(int) (height * 0.6));


        //  RelativeLayout rl = (RelativeLayout)findViewById(R.id.lay);
      //  rl.setBackgroundColor(Color.RED);
    }
}