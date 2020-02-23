package com.kulartist.tekhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kulartist.tekhub.Feedback;
import com.kulartist.tekhubandroid.R;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetails extends AppCompatActivity {

    String itemJsonString,itemId;
    TextView itmCond,orderDate,itmName,pickupDate,returnDate;
    JSONObject orderObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itmName=findViewById(R.id.order_name_text);
        itmCond=findViewById(R.id.order_condition_text);
        orderDate=findViewById(R.id.order_date_text);
        pickupDate=findViewById(R.id.pickup_date_text);
        returnDate=findViewById(R.id.return_date_text);




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

        } catch (JSONException e) {
            e.printStackTrace();
        }




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
