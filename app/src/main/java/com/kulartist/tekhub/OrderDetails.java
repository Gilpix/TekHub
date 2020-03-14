package com.kulartist.tekhub;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kulartist.tekhubandroid.LoginActivity;
import com.kulartist.tekhubandroid.R;
import com.kulartist.tekhubandroid.SplashScreen;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetails extends AppCompatActivity {

    TextView itmCond,orderDate,itmName,pickupDate,returnDate,usrId;
    JSONObject orderObject;
    String itemJsonString,itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        setActionBarTitle("Order Details");

        itmName=findViewById(R.id.order_name_text);
        itmCond=findViewById(R.id.order_condition_text);
        orderDate=findViewById(R.id.order_date_text);
        pickupDate=findViewById(R.id.pickup_date_text);
        returnDate=findViewById(R.id.return_date_text);
        usrId=findViewById(R.id.order_user_id_text);


        Intent i=getIntent();
        itemId=i.getStringExtra("itemId");
        itemJsonString=i.getStringExtra("OrderDetailsObject");
        try {
            orderObject=new JSONObject(itemJsonString);

            itmName.setText(orderObject.getString("itemname"));
            itmCond.setText(orderObject.getString("itemCondition"));
            orderDate.setText(orderObject.getString("orderDate"));
            pickupDate.setText(orderObject.getString("pickupDate"));
            returnDate.setText(orderObject.getString("returnDate"));
            usrId.setText(LoginActivity.currentUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.LEFT);
        textView.setTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(textView);
    }


    public void provideFeedback(View view) {
        Intent i = new Intent(this.getApplicationContext(), Feedback.class);
        i.putExtra("itemId",itemId);
        startActivity(i);
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
