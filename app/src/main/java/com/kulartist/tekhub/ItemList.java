package com.kulartist.tekhub;


import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kulartist.tekhubandroid.R;


public class ItemList extends BottomMenu {

    private FloatingActionButton fab_main, fab1_mail, fab2_share;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView textview_mail, textview_share;

    Boolean isOpen = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        getLayout(R.layout.activity_item_list);
        getMenuIcon(R.drawable.home,R.id.itemlist);
        // setActionBarTitle("My Profile");







        fab_main = findViewById(R.id.fab);
//        fab1_mail = findViewById(R.id.fab1);
//        fab2_share = findViewById(R.id.fab2);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        textview_mail = (TextView) findViewById(R.id.textview_mail);
        textview_share = (TextView) findViewById(R.id.textview_share);





    }


    public void filterByRatings(View view) {
        Toast.makeText(getApplicationContext(), "filterByRatings", Toast.LENGTH_SHORT).show();

    }

    public void filterByNewestItemsAdded(View view) {
        Toast.makeText(getApplicationContext(), "filterByNewestItemsAdded", Toast.LENGTH_SHORT).show();

    }

    public void fliterOptions(View view) {
        if (isOpen) {

            textview_mail.setVisibility(View.INVISIBLE);
            textview_share.setVisibility(View.INVISIBLE);
            textview_share.startAnimation(fab_close);
            textview_mail.startAnimation(fab_close);
            fab_main.startAnimation(fab_anticlock);
            textview_share.setClickable(false);
            textview_mail.setClickable(false);
            isOpen = false;
        } else {
            textview_mail.setVisibility(View.VISIBLE);
            textview_share.setVisibility(View.VISIBLE);
            textview_share.startAnimation(fab_open);
            textview_mail.startAnimation(fab_open);
            fab_main.startAnimation(fab_clock);
            textview_share.setClickable(true);
            textview_mail.setClickable(true);
            isOpen = true;
        }
    }
}



