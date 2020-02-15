package com.kulartist.tekhub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.kulartist.tekhubandroid.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Order extends AppCompatActivity {
    String dates="";
    Calendar startCalendar,endCalender;
    EditText pickDate,returnDate;
    DatePickerDialog.OnDateSetListener startDate,endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        startCalendar = Calendar.getInstance();
        endCalender = Calendar.getInstance();

        pickDate= (EditText) findViewById(R.id.pickup_date);
        returnDate= (EditText) findViewById(R.id.return_date);





    }


    private void updateLabelStartDate() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        pickDate.setText(sdf.format(startCalendar.getTime()));
    }


    private void updateLabelEndDate() {
        String myFormat = "MM/dd/yy"; //In which you need put here
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


        AlertDialog.Builder builder = new AlertDialog.Builder(this);


                //Uncomment the below code to Set the message and title from the strings.xml file
//                builder.setMessage("Welcome to Alert Dialog") .setTitle("Javatpoint Alert Dialog");

                //Setting message manually and performing action on button click
                builder.setMessage("Congratulation - Your order has been placed successfully...")
                        .setCancelable(false)
                        .setPositiveButton("Home", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent in = new Intent(getBaseContext(), ItemList.class);//Restaurant
                                startActivity(in);
                                finish();
                            }
                        })
                        .setNegativeButton("Details", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                Intent in = new Intent(getBaseContext(), OrderDetails.class);//Restaurant
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
}
