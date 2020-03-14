package com.kulartist.tekhub;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kulartist.tekhubandroid.R;

public class AboutUs extends BottomMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayout(R.layout.activity_about_us);
        getMenuIcon(R.drawable.aboutinn,R.id.aboutUs);
        setActionBarTitle("About US");
    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setCustomView(textView);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AboutUs.this, ItemList.class);
        startActivity(i);
        finish();
    }

}
