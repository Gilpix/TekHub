package com.kulartist.tekhubandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kulartist.tekhub.ItemList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText usrID,password;

    private String userEmail,userPass;
    public static String currentUser, currentIP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       currentIP="192.168.0.102";
        //currentIP="172.20.10.5";

        usrID=findViewById(R.id.usrid);
        password=findViewById(R.id.password);
    }

    public void signUp(View view) {

        Intent i =new Intent(LoginActivity.this, RegistrationActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void resetPassword(View view) {
        Intent i =new Intent(LoginActivity.this, ResetPassword.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void signIn(View view) {
        hideKeyboardwithoutPopulate(LoginActivity.this);
        if(!usrID.getText().toString().isEmpty() && !password.getText().toString().isEmpty())
        {


            userEmail=usrID.getText().toString();
            userPass=password.getText().toString();

            new LoginActivity.SignInUser(userEmail, userPass).execute();
        }
        else
            Toast.makeText(LoginActivity.this,"Please Enter all fields",Toast.LENGTH_SHORT).show();
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





    private class SignInUser extends AsyncTask<Void, Void, Void> {

        String usrId,  passwrd,userStatus,usrActive;

        public SignInUser(String id, String password) {

            usrId=id;
            passwrd=password;
        }

        @Override
        protected Void doInBackground(Void... params){

            URL url = null;

            try {

                url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/user/userLogin&"+usrId+"&"+passwrd);
                // url = new URL("http://192.168.2.250:8080/OnlineQuiz/mad312group2/quizuser/userLogin&"+mailAdd+"&"+passwrd);

                HttpURLConnection client = null;

                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();


                System.out.println("\n Sending 'GET' request to URL : " + url);

                System.out.println("Response Code : " + responseCode);
                InputStreamReader myInput= new InputStreamReader(client.getInputStream());

                BufferedReader in = new BufferedReader(myInput);
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());

                JSONObject obj =new JSONObject(response.toString());
                userStatus=""+obj.getString("Status");
                usrActive=""+obj.getString("userActive");


            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result){
            String message="";

            if(userStatus.equals("ok")) {

                if(usrActive.equals("1")) {
                    currentUser = usrId;
                    new userIdSingleLoggedIn(usrId).execute();
                }
                else
                    Toast.makeText(LoginActivity.this,"User Already Logged in other device ",Toast.LENGTH_SHORT).show();

            }
            else
                Toast.makeText(LoginActivity.this,"Wrong Credentials - " +userStatus,Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
        }
    }









    private class userIdSingleLoggedIn extends AsyncTask<Void, Void, Void> {


        String usrId, userStatus;

        public userIdSingleLoggedIn(String id) {

            usrId=id;
        }

        @Override
        protected Void doInBackground(Void... params){

            URL url = null;

            try {

                url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/user/userIdAlreadyLoggedIn&"+usrId);
                // url = new URL("http://192.168.2.250:8080/OnlineQuiz/mad312group2/quizuser/userLogin&"+mailAdd+"&"+passwrd);

                HttpURLConnection client = null;

                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();


                System.out.println("\n Sending 'GET' request to URL : " + url);

                System.out.println("Response Code : " + responseCode);
                InputStreamReader myInput= new InputStreamReader(client.getInputStream());

                BufferedReader in = new BufferedReader(myInput);
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());

                JSONObject obj =new JSONObject(response.toString());
                userStatus=""+obj.getString("Status");
                //usrActive=""+obj.getString("userActive");


            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result){

            if(userStatus.equals("ok")) {
                Toast.makeText(LoginActivity.this,"Login Successful - " +userStatus,Toast.LENGTH_SHORT).show();

                Intent i = new Intent(LoginActivity.this, ItemList.class);
                //currentUser=usrId;
                startActivity(i);
            }
            else
                Toast.makeText(LoginActivity.this,"Wrong Credentials - " +userStatus,Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
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
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(textView);


    }





}










