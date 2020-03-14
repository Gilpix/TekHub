package com.kulartist.tekhub;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
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

import static com.kulartist.tekhubandroid.SplashScreen.currentIP;

public class Feedback extends AppCompatActivity {

    TextView msg;
    Switch issue;
    RatingBar ratingbar;
    String isIssue="0",rating,itemId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        msg=findViewById(R.id.feed_message);
        issue=findViewById(R.id.is_issue_radio_button);
        ratingbar=(RatingBar)findViewById(R.id.ratingBar);

        Intent i=getIntent();
        itemId=i.getStringExtra("itemId");
        setActionBarTitle("Feedback");

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


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    public static void hideKeyboardwithoutPopulate(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e)
        {

        }
    }


    public void addFeedback(View view) throws JSONException {
        hideKeyboardwithoutPopulate(Feedback.this);
        DatabaseObjects.itemList = new JSONArray("[]");

        rating=String.valueOf(ratingbar.getRating());

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

                HttpURLConnection client = null;
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("GET");

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
            Intent i = new Intent(Feedback.this, OrderList.class);
            startActivity(i);
            super.onPostExecute(result);
        }
    }

}
