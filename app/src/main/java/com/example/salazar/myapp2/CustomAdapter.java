package com.example.salazar.myapp2;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Salazar on 8/18/2017.
 */

public class CustomAdapter extends PagerAdapter{

    private int[] img = {R.drawable.icon, R.drawable.icon,R.drawable.icon,R.drawable.icon};
    private LayoutInflater inflater;
    private Context ctx;

    public CustomAdapter(Context ctx){

        this.ctx = ctx;

    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.swipe,container,false);

        ImageView im = (ImageView)v.findViewById(R.id.imageView);
        TextView tx = (TextView)v.findViewById(R.id.textView);
        im.setImageResource(img[position]);
        tx.setText("Image: " + position);
        container.addView(v);
        return v;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
