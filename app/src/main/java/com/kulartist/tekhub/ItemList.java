package com.kulartist.tekhub;


import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kulartist.tekhubandroid.R;


public class ItemList extends BottomMenu {


    RadioGroup radioGroup1;
    RadioButton deals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bottom_menu);




        getLayout(R.layout.activity_item_list);
        getMenuIcon(R.drawable.home,R.id.itemlist);
        // setActionBarTitle("My Profile");

    }



}



