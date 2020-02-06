package com.kulartist.tekhub;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulartist.tekhubandroid.R;

public class Item extends AppCompatActivity {

    RelativeLayout waitingCardButton;
    TextView availability;
    Button waiting_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        waitingCardButton=findViewById(R.id.waiting_realative);
        availability=findViewById(R.id.item_availability);
        waiting_button=findViewById(R.id.waiting_button);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        if(!availability.getText().toString().equals("Available"))
            waiting_button.setLayoutParams(new LinearLayout.LayoutParams(0, 0));








    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
