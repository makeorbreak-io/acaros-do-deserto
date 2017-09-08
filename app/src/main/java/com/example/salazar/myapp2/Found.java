package com.example.salazar.myapp2;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Salazar on 8/30/2017.
 */

public class Found extends Activity {
    Bundle b;
    Bundle c;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = new Bundle();
        b = this.getIntent().getExtras();
        setContentView(R.layout.found);
        LinearLayout lView = (LinearLayout)findViewById(R.id.mylinearlayout);
        final SpannableString ss = new SpannableString(b.getString("ingredientes"));
        final String[] ingred = b.getString("ingredientes").split(",");
        int count = 0;
        int count2 = 0;
        TextView myText = new TextView(this);

        for(int y = 0;y <ingred.length;y++){
            count2 += ingred[y].length();
           ss.setSpan(new MyClickableSpan(ingred[y]), count, count2, 0);
        //    temp += ingred[y].length() + ";";
            count2 += 1;
            count += ingred[y].length()+1;
        }

    //    TextView myText = new TextView(this);
    //    myText.setText(b.getString("ingredientes"));
        myText.setMovementMethod(LinkMovementMethod.getInstance());
        myText.setLinksClickable(true);
        myText.setText(ss);
        myText.setTextSize(20);
        lView.addView(myText);

    }

    private class MyClickableSpan extends ClickableSpan {
        String mText;

        private MyClickableSpan(String text) {
            mText = text;
        }

        @Override
        public void onClick(View widget) {
           Intent temp = new Intent(Found.this,Pop.class);
            c.putString("comp",mText);
            temp.putExtras(c);
            startActivity(temp);
        }
    }
}
