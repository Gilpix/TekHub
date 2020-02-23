package com.kulartist.tekhub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kulartist.tekhubandroid.LoginActivity;
import com.kulartist.tekhubandroid.R;
import com.kulartist.tekhubandroid.RegistrationActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.kulartist.tekhubandroid.LoginActivity.currentIP;

public class Feedback extends AppCompatActivity {

    TextView msg;
    Switch issue;
    String rateNum;
    String isIssue="0";
    RatingBar ratingbar;
    String rating;

    JSONObject itemObject;
    String itemJsonString,itemId;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        msg=findViewById(R.id.feed_message);
        issue=findViewById(R.id.is_issue_radio_button);
        //rating=findViewById(R.id.ratingBar);
        ratingbar=(RatingBar)findViewById(R.id.ratingBar);


        Intent i=getIntent();
        itemId=i.getStringExtra("itemId");

        LoginActivity loginActivity=new LoginActivity();
        //loginActivity.setActionBarTitle("Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //rateNum =rating.getRating()+"";

        //itemJsonString=i.getStringExtra("ItemDetailsObject");





    }






    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void addFeedback(View view) {

        rating=String.valueOf(ratingbar.getRating());
        System.out.println("################"+itemId+"######"+ratingbar.getNumStars());

        if(issue.isChecked())
            isIssue="1";
        else
            isIssue="0";


        if(rating.isEmpty() || msg.getText().toString().isEmpty())
            Toast.makeText(Feedback.this,"Please enter all fields",Toast.LENGTH_SHORT).show();
        else
        new addFeedback(LoginActivity.currentUser,itemId,rating,msg.getText().toString(),isIssue).execute();

    }


    private class addFeedback extends AsyncTask<Void, Void, Void> {

        String userId,itemId,  rating,  message,  isIssue;

        public addFeedback(String userId, String itemId, String rating, String message,String isIssue) {
            this.userId=userId;
            this.itemId=itemId;
            this.rating=rating;
            this.message=message;
            this.isIssue=isIssue;

        }

        @Override
        protected Void doInBackground(Void... params){

            URL url = null;

            try {

                url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/feedback/addFeedback&"+userId+"&"+itemId+"&"+rating+"&"+message+"&"+isIssue);
                // url = new URL("http://192.168.2.250:8080/OnlineQuiz/mad312group2/quizuser/registerUser&"+mailAdd+"&"+firstName+"&"+lastName+"&"+passwrd);

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
            Toast.makeText(Feedback.this,"Thanks For feedback",Toast.LENGTH_SHORT).show();
            EditText fname,lname,pass,email;

            Intent i = new Intent(Feedback.this, OrderList.class);

            startActivity(i);

            super.onPostExecute(result);
        }
    }





}
