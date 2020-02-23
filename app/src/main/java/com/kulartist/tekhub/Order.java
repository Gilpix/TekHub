package com.kulartist.tekhub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.kulartist.tekhubandroid.LoginActivity;
import com.kulartist.tekhubandroid.R;
import com.kulartist.tekhubandroid.RegistrationActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.kulartist.tekhubandroid.LoginActivity.currentIP;

public class Order extends AppCompatActivity {
    String dates="";
    Calendar startCalendar,endCalender;
    EditText pickDate,returnDate;
    DatePickerDialog.OnDateSetListener startDate,endDate;

    ProgressDialog progressDialog;
    String itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        startCalendar = Calendar.getInstance();
        endCalender = Calendar.getInstance();

        pickDate= (EditText) findViewById(R.id.pickup_date);
        returnDate= (EditText) findViewById(R.id.return_date);

        Intent i=getIntent();
        itemID=i.getStringExtra("ItemId");



        progressDialog=new ProgressDialog(this);





    }


    private void updateLabelStartDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        pickDate.setText(sdf.format(startCalendar.getTime()));
    }


    private void updateLabelEndDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        returnDate.setText(sdf.format(endCalender.getTime()));
    }





    public void selectPickupDate(View view) {

        startDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, monthOfYear);
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStartDate();
            }

        };


        new DatePickerDialog(Order.this, startDate, startCalendar
                .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }



    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
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
                updateLabelEndDate();
            }

        };

        new DatePickerDialog(Order.this, endDate, endCalender
                .get(Calendar.YEAR), endCalender.get(Calendar.MONTH),
                endCalender.get(Calendar.DAY_OF_MONTH)).show();
    }


    public void cancelItem(View view) {
        Intent in = new Intent(getBaseContext(), ItemList.class);//Restaurant
        startActivity(in);
    }




    public void orderConfirmed(View view) {
        java.sql.Date picDate = Date.valueOf(pickDate.getText().toString()),  retDate=Date.valueOf(returnDate.getText().toString());


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

            url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/borrow/placeOrder&"+userId+"&"+itemId+"&"+pickupDate+"&"+returnDate);
            // url = new URL("http://192.168.2.250:8080/OnlineQuiz/mad312group2/quizuser/registerUser&"+mailAdd+"&"+firstName+"&"+lastName+"&"+passwrd);

            HttpURLConnection client = null;

            client = (HttpURLConnection) url.openConnection();

            client.setRequestMethod("GET");

            int responseCode = client.getResponseCode();


            System.out.println("\n Sending 'GET' request to URL : " + url);

            System.out.println("Response Code : " + responseCode);

            System.out.println("############ Code : "  +userId+"&"+itemId+"&"+pickupDate+"&"+returnDate);

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
        EditText fname,lname,pass,email;
        // LoginActivity.currentUser=mailAdd;

        super.onPostExecute(result);
        alertBoxDisplay();
    }
}





}