package com.kulartist.tekhub;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kulartist.tekhubandroid.LoginActivity;
import com.kulartist.tekhubandroid.R;
import com.kulartist.tekhubandroid.RegistrationActivity;

import androidx.appcompat.app.AppCompatActivity;


public class Items extends AppCompatActivity {


    RadioGroup radioGroup1;
    RadioButton deals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);


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

                        in=new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.waiting_list:
                        Log.i("matching", "matching inside1 watchlistAdapter" + checkedId);

                        in = new Intent(getBaseContext(), RegistrationActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);

                        break;
                    case R.id.itemlist:
                        Log.i("matching", "matching inside1 rate" + checkedId);

                        in = new Intent(getBaseContext(), LoginActivity.class);//buddies
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.orderlist:
                        Log.i("matching", "matching inside1 listing" + checkedId);
                        in = new Intent(getBaseContext(), LoginActivity.class);//Restaurant
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.settings:
                        Log.i("matching", "matching inside1 deals" + checkedId);
                        in = new Intent(getBaseContext(), LoginActivity.class);//settings
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    default:
                        break;
                }
            }
        });
    }

//    public void setActionBarTitle(String title) {
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        TextView textView = new TextView(this);
//        textView.setText(title);
//        textView.setTextSize(20);
//        textView.setTypeface(null, Typeface.BOLD);
//        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(getResources().getColor(R.color.white));
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(textView);
//    }



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



