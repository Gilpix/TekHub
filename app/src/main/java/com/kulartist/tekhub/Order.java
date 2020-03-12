package com.kulartist.tekhub;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.kulartist.tekhubandroid.LoginActivity;
import com.kulartist.tekhubandroid.R;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import static com.kulartist.tekhubandroid.SplashScreen.currentIP;

public class Order extends AppCompatActivity {
    Calendar startCalendar,endCalender;
    TextView pickDate,returnDate;
    DatePickerDialog.OnDateSetListener startDate,endDate;
    ProgressDialog progressDialog;
    String itemID,borrowNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setActionBarTitle("Order Item");

        startCalendar = Calendar.getInstance();
        endCalender = Calendar.getInstance();

        pickDate= (TextView) findViewById(R.id.pickup_date);
        returnDate= (TextView) findViewById(R.id.return_date);

        Intent i=getIntent();
        itemID=i.getStringExtra("ItemId");
        borrowNum=i.getStringExtra("borrowNum");

        progressDialog=new ProgressDialog(this);

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


    private void updateLabelStartDate() throws ParseException {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if(sdf.parse(sdf.format(startCalendar.getTime())).before(getCurrentDate()))
            Toast.makeText(this,"Please select valid pickup date ",Toast.LENGTH_LONG).show();
        else if(!returnDate.getText().toString().isEmpty()&&sdf.parse(sdf.format(startCalendar.getTime())).after(sdf.parse(returnDate.getText().toString())))
            Toast.makeText(this,"Please select valid pickup date ",Toast.LENGTH_LONG).show();
            else
                pickDate.setText(sdf.format(startCalendar.getTime()));
    }


    private void updateLabelEndDate() throws ParseException {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if(pickDate.getText().toString().isEmpty())
            Toast.makeText(this,"Please select pickup date ",Toast.LENGTH_LONG).show();
        else if(sdf.parse(sdf.format(endCalender.getTime())).before(getCurrentDate()) || sdf.parse(sdf.format(endCalender.getTime())).before(sdf.parse(pickDate.getText().toString())))
            Toast.makeText(this,"Please select valid return date ",Toast.LENGTH_LONG).show();
        else
        returnDate.setText(sdf.format(endCalender.getTime()));
    }


    public java.util.Date getCurrentDate() throws ParseException {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return formatter.parse(formatter.format(date));
    }


    public void selectPickupDate(View view) {
        startDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, monthOfYear);
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                try {
                    updateLabelStartDate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        };


        new DatePickerDialog(Order.this, startDate, startCalendar
                .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    public void selectReturnDate(View view) {

        endDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                endCalender.set(Calendar.YEAR, year);
                endCalender.set(Calendar.MONTH, monthOfYear);
                endCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                try {
                    updateLabelEndDate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        };

        new DatePickerDialog(Order.this, endDate, endCalender
                .get(Calendar.YEAR), endCalender.get(Calendar.MONTH),
                endCalender.get(Calendar.DAY_OF_MONTH)).show();
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    public void cancelItem(View view) {
        Intent in = new Intent(getBaseContext(), ItemList.class);
        startActivity(in);
        finish();
    }


    public void orderConfirmed(View view) throws JSONException {
        java.sql.Date picDate = Date.valueOf(pickDate.getText().toString()),  retDate=Date.valueOf(returnDate.getText().toString());

        DatabaseObjects.orderList=new JSONArray("[]");
        DatabaseObjects.itemList=new JSONArray("[]");

        PlaceOrder placeOrder=new PlaceOrder(LoginActivity.currentUser,itemID,picDate,retDate);
        placeOrder.execute();
    }


    public void alertBoxDisplay()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Congratulation - Your order has been placed successfully...")
                .setCancelable(false)
                .setPositiveButton("Home", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent in = new Intent(getBaseContext(), ItemList.class);
                        startActivity(in);
                        finish();
                    }
                })
                .setNegativeButton("OrderList", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        Intent in = new Intent(getBaseContext(), OrderList.class);
                        startActivity(in);
                        finish();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Order Confirmation");
        alert.show();
    }


private class PlaceOrder extends AsyncTask<Void, Void, Void> {

    String userId,itemId;
    java.sql.Date pickupDate,  returnDate;

    public PlaceOrder(String userId, String itemId, java.sql.Date pickupDate,java.sql.Date returnDate) {
        this.userId=userId;
        this.itemId=itemId;
        this.pickupDate=pickupDate;
        this.returnDate=returnDate;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(Void... params){

        URL url = null;

        try {
            url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/borrow/placeOrder&"+userId+"&"+itemId+"&"+pickupDate+"&"+returnDate+"&"+borrowNum);
            HttpURLConnection client = null;

            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");

            int responseCode = client.getResponseCode();
            System.out.println("\n Sending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void result){
        Toast.makeText(Order.this,"Order confirmed",Toast.LENGTH_SHORT).show();
        super.onPostExecute(result);
        alertBoxDisplay();
    }
}


}