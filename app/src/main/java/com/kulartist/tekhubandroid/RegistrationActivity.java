package com.kulartist.tekhubandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
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

import static com.kulartist.tekhubandroid.SplashScreen.currentIP;


public class RegistrationActivity extends AppCompatActivity {

    EditText  stdid,stdname,  stdemail,  stdpassword,  stdmobno,stdage;
    RadioButton rMale,rFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        stdid =findViewById(R.id.signup_input_student_id);
        stdname =findViewById(R.id.signup_input_name);
        stdemail =findViewById(R.id.signup_input_email);
        stdpassword =findViewById(R.id.signup_input_password);
        stdmobno =findViewById(R.id.signup_input_phone);
        stdage =findViewById(R.id.signup_input_age);
        rMale =findViewById(R.id.male_radio_btn);
        rFemale =findViewById(R.id.female_radio_btn);

    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(RegistrationActivity.this,LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    public void signInAlreadyAccount(View view) {
        hideKeyboardwithoutPopulate(RegistrationActivity.this);
        Intent i =new Intent(RegistrationActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void SignUpResult(View view) {
        hideKeyboardwithoutPopulate(RegistrationActivity.this);
        String id,name,  email,  password,  mobno,age,gender="";

        if(!stdid.getText().toString().isEmpty() && !stdname.getText().toString().isEmpty() &&
                !stdemail.getText().toString().isEmpty() && !stdpassword.getText().toString().isEmpty() &&
                !stdmobno.getText().toString().isEmpty() && !stdage.getText().toString().isEmpty())
        {
            if(rMale.isChecked())
                gender="male";
            else if(rFemale.isChecked())
                gender="female";

            id=stdid.getText().toString();
            name=stdname.getText().toString();
            email=stdemail.getText().toString();
            password=stdpassword.getText().toString();
            mobno=stdmobno.getText().toString();
            age=stdage.getText().toString();

            if(id.length()!=7){
                stdid.setError("enter valid id");
                stdid.requestFocus();
            }
            else if(!email.substring(email.length()-10).equals("@gmail.com")){
                stdemail.setError("enter valid email");
                stdemail.requestFocus();
            }
            else if(password.length()<5){
                stdpassword.setError("enter valid password");
                stdpassword.requestFocus();
            }
            else if(mobno.length()<9 ||mobno.length()>11){
                stdmobno.setError("enter valid mobile no.");
                stdmobno.requestFocus();
            }
            else if(Integer.parseInt(age)<20 ||Integer.parseInt(age)>35){
                stdage.setError("enter valid age");
                stdage.requestFocus();
            }
            else
            new SignUpUser(id,name, email, password, mobno,age,gender).execute();

        }
        else
            Toast.makeText(RegistrationActivity.this,"Please Enter all fields",Toast.LENGTH_SHORT).show();

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


    private class SignUpUser extends AsyncTask<Void, Void, Void> {

        String id,name,  email,  password,  mobno,age,gender,userStatus;

        public SignUpUser(String id, String name, String mail, String password,String num, String age,String gender) {
            this.id=id;
            this.name=name;
            email=mail;
            this.password=password;
            mobno=num;
            this.age=age;
            this.gender=gender;
        }

        @Override
        protected Void doInBackground(Void... params){

            URL url = null;

            try {
                url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/user/registerUser&"+id+"&"+name+"&"+email+"&"+password+"&"+mobno+"&"+age+"&"+gender);
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
            if(userStatus.equals("ok")) {
                Toast.makeText(RegistrationActivity.this, "Profile Saved", Toast.LENGTH_SHORT).show();
                LoginActivity.currentUser = stdid.getText().toString();
                Intent i = new Intent(RegistrationActivity.this, ItemList.class);
                startActivity(i);
            }
            else
                Toast.makeText(RegistrationActivity.this, "Error - UserId already registered", Toast.LENGTH_SHORT).show();


            super.onPostExecute(result);
        }
    }


}