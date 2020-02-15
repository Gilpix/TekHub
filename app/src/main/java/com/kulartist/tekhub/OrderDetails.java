package com.kulartist.tekhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kulartist.tekhub.Feedback;
import com.kulartist.tekhubandroid.R;

public class OrderDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
    }

    public void provideFeedback(View view) {
        Intent i = new Intent(this.getApplicationContext(), Feedback.class);
        startActivity(i);
        finish();
    }
}
