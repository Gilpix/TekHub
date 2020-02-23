package com.kulartist.tekhub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kulartist.tekhubandroid.R;

import androidx.appcompat.app.AppCompatActivity;


public class BottomMenu extends AppCompatActivity {


    RadioGroup radioGroup1;
    RadioButton deals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_menu);


        radioGroup1=(RadioGroup)findViewById(R.id.radioGroup1);
        deals = (RadioButton)findViewById(R.id.itemlist);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Intent in;
                Log.i("matching", "matching inside1 bro" + checkedId);
                switch (checkedId)
                {
                    case R.id.my_profile:
                        Log.i("matching", "matching inside1 matching" +  checkedId);

                        in=new Intent(getBaseContext(), Student.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.waiting_list:
                        Log.i("matching", "matching inside1 watchlistAdapter" + checkedId);

                        in = new Intent(getBaseContext(), WaitingList.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);

                        break;
                    case R.id.itemlist:
                        Log.i("matching", "matching inside1 rate" + checkedId);

                        in = new Intent(getBaseContext(), ItemList.class);//buddies
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.orderlist:
                        Log.i("matching", "matching inside1 listing" + checkedId);
                        in = new Intent(getBaseContext(), OrderList.class);//Restaurant
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.aboutUs:
                        Log.i("matching", "matching inside1 deals" + checkedId);
                       in = new Intent(getBaseContext(), AboutUs.class);
                       startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    default:
                        break;
                }
            }
        });
    }





    public void getMenuIcon(int menuIcon,int radiobutton )
    {

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(radiobutton);

        // Change the corresponding icon and text color on nav button click.

        rb.setCompoundDrawablesWithIntrinsicBounds( 0,menuIcon, 0,0);
        rb.setTextColor(Color.parseColor("#cccccc"));
    }

    public void getLayout( int layout)
    {
        LinearLayout dynamicContent,bottonNavBar;
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(layout, null);
        dynamicContent.addView(wizard);



    }





}



