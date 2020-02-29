package com.kulartist.tekhub;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.kulartist.tekhubandroid.LoginActivity;
import com.kulartist.tekhubandroid.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import static com.kulartist.tekhubandroid.LoginActivity.currentIP;


public class Student extends BottomMenu {
    TextView std_name, std_id,edit_save;
    EditText email, phone, gender, age;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayout(R.layout.activity_student);
        getMenuIcon(R.drawable.profile,R.id.my_profile);


        std_name = findViewById(R.id.student_name);
        std_id = findViewById(R.id.student_id);
        email = findViewById(R.id.student_email_text);
        phone = findViewById(R.id.student_phone_text);
        age = findViewById(R.id.student_age_text);
        gender = findViewById(R.id.student_gender_text);
        edit_save = findViewById(R.id.edit_profile_text);

        progressDialog=new ProgressDialog(this);
        setProfileDisabled();


        if(DatabaseObjects.studentProfile.toString().equals("{}") || DatabaseObjects.studentProfile.toString().equals("")) {
            displayUserProfile d = new displayUserProfile();
            d.execute();
        }
        else
            setProfileFields(DatabaseObjects.studentProfile);
    }


    public void setProfileFields(JSONObject obj)
    {
        try {
            std_id.setText(""+obj.getString("userId"));
            std_name.setText(""+obj.getString("name"));
            email.setText(""+obj.getString("email"));
            phone.setText(""+obj.getString("mobNo"));
            gender.setText(""+obj.getString("gender"));
            age.setText(""+obj.getString("age"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void editProfileEnabled() {
        email.setEnabled(true);
        email.setClickable(true);
        email.setFocusable(true);
        email.setFocusableInTouchMode(true);

        phone.setEnabled(true);
        phone.setClickable(true);
        phone.setFocusable(true);
        phone.setFocusableInTouchMode(true);

        gender.setEnabled(true);
        gender.setClickable(true);
        gender.setFocusable(true);
        gender.setFocusableInTouchMode(true);

        age.setEnabled(true);
        age.setClickable(true);
        age.setFocusable(true);
        age.setFocusableInTouchMode(true);
        age.setBackgroundResource(android.R.color.transparent);
    }


    public void setProfileDisabled() {
        gender.setEnabled(false);
        gender.setClickable(false);
        gender.setFocusable(false);
        gender.setFocusableInTouchMode(false);
        gender.setBackgroundResource(android.R.color.transparent);

        email.setEnabled(false);
        email.setClickable(false);
        email.setFocusable(false);
        email.setFocusableInTouchMode(false);
        email.setBackgroundResource(android.R.color.transparent);

        phone.setEnabled(false);
        phone.setClickable(false);
        phone.setFocusable(false);
        phone.setFocusableInTouchMode(false);
        phone.setBackgroundResource(android.R.color.transparent);

        age.setEnabled(false);
        age.setClickable(false);
        age.setFocusable(false);
        age.setFocusableInTouchMode(false);
        age.setBackgroundResource(android.R.color.transparent);
    }


    public void editSaveProfile(View view) {
        hideKeyboardwithoutPopulate(Student.this);

        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.isFocusable() == true) {
                    edit_save.setText("edit");
                    new editUserProfile().execute();
                    setProfileDisabled();
                    Toast.makeText(Student.this, " Profile Saved ", Toast.LENGTH_LONG).show();
                } else if (email.isFocusable() == false) {
                    edit_save.setText("save");
                    editProfileEnabled();
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideKeyboardwithoutPopulate(Student.this);
        Intent i = new Intent(Student.this, Student.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
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


    public void logOut(View view) throws JSONException {
        hideKeyboardwithoutPopulate(Student.this);
        DatabaseObjects.studentProfile=new JSONObject("{}");
        DatabaseObjects.itemList = new JSONArray("[]");
        DatabaseObjects.orderList = new JSONArray("[]");
        DatabaseObjects.waitingList = new JSONArray("[]");
        new userLogout().execute();

    }


    public class userLogout extends AsyncTask<Void, Void, Void> {

        String userStatus;

        @Override
        protected Void doInBackground(Void... params){

            URL url = null;

            try {
                url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/user/userLogout&"+LoginActivity.currentUser);
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

                JSONObject obj =new JSONObject(response.toString());
                userStatus=""+obj.getString("Status");

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

            if(userStatus.equals("ok"))
            {
                Intent i = new Intent(Student.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
            super.onPostExecute(result);
        }

    }


    private class displayUserProfile extends AsyncTask<Void, Void, Void> {

        String userStatus;
        String puserId,pname,pemail,pmobNo,page,pgender;

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
                url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/user/userProfile&"+LoginActivity.currentUser);

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

                DatabaseObjects.studentProfile=new JSONObject(response.toString());

                JSONObject obj =new JSONObject(response.toString());
                userStatus=""+obj.getString("Status");
                puserId=""+obj.getString("userId");
                pname=""+obj.getString("name");
                pemail=""+obj.getString("email");
                pmobNo=""+obj.getString("mobNo");
                page=""+obj.getString("age");
                pgender=""+obj.getString("gender");

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

            if(userStatus.equals("ok"))
            {
                std_id.setText(puserId);
                std_name.setText(pname);
                email.setText(pemail);
                phone.setText(pmobNo);
                gender.setText(pgender);
                age.setText(page);

            }

            progressDialog.hide();
            super.onPostExecute(result);
        }

    }


    private class editUserProfile extends AsyncTask<Void, Void, Void> {

        String userStatus;
        String pemail=email.getText().toString(),pmobNo=phone.getText().toString(),page=age.getText().toString(),pgender=gender.getText().toString();

        @Override
        protected Void doInBackground(Void... params){

            URL url = null;
            try {
                url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/user/editProfile&"+LoginActivity.currentUser+
                        "&"+pemail+"&"+pmobNo+"&"+page+"&"+pgender);

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

                JSONObject obj =new JSONObject(response.toString());
                userStatus=""+obj.getString("Status");

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

            if(userStatus.equals("ok"))
            {
                displayUserProfile d = new displayUserProfile();
                d.execute();
            }
            super.onPostExecute(result);
        }

    }


}


