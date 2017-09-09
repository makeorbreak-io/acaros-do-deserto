package com.example.salazar.myapp2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import static com.example.salazar.myapp2.R.layout.settings;

/**
 * Created by caesar on 9/8/17.
 */

public class EditSettings extends Activity {
    Bundle b;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        b = new Bundle();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_meat:
                if (checked)
                b=b;
            else
                b=b;
                break;
            case R.id.checkbox_cheese:
                if (checked)
                b=b;
            else
                b=b;
                break;
            // TODO: Veggie sandwich
        }
    }
}
