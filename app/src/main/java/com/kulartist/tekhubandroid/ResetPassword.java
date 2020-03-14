package com.kulartist.tekhubandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static com.kulartist.tekhubandroid.SplashScreen.currentIP;

public class ResetPassword extends AppCompatActivity {
    private String mVerificationId;
    //firebase auth object
    private FirebaseAuth mAuth;
    Button resetPass;

    EditText newPassword,confirmPassword,studentId;
    private EditText editTextMobile,verifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        setActionBarTitle("Change Password");

        resetPass=findViewById(R.id.savePassword);
        editTextMobile = findViewById(R.id.phone_pass_reset);
        verifyCode=findViewById(R.id.code);
        newPassword=findViewById(R.id.newpassword);
        confirmPassword=findViewById(R.id.confirmPassword);
        studentId=findViewById(R.id.studentID);
         mAuth=FirebaseAuth.getInstance();

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText codee=findViewById(R.id.code);
                String code = codee.getText().toString().trim();
                if(code.isEmpty()||code.length()<6) {
                    verifyCode.setError("Enter Code...");
                    verifyCode.requestFocus();
                    return;
                }
                else if(!newPassword.getText().toString().equals(confirmPassword.getText().toString()))
                {
                    confirmPassword.setError("Password Not Match...");
                    confirmPassword.requestFocus();
                    return;
                }
                hideKeyboardwithoutPopulate(ResetPassword.this);
                verifyVerificationCode(code);
            }
        });

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


    public void getCode(View view) {

        String mobile = editTextMobile.getText().toString().trim();

        if(mobile.isEmpty() || mobile.length() < 9){
            editTextMobile.setError("Enter a valid mobile");
            editTextMobile.requestFocus();
            return;
        }

        hideKeyboardwithoutPopulate(ResetPassword.this);
        sendVerificationCode(mobile);
    }


    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+1" + mobile,60,
                TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallbacks);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                verifyCode.setText(code);
                //verifying the code
                //verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(ResetPassword.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(ResetPassword.this, "Code Sent", Toast.LENGTH_LONG).show();

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(ResetPassword.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this,"Code Matched",Toast.LENGTH_LONG).show();

                            new resetPassword().execute();

                        } else {

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            final String finalMessage = message;
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {Toast.makeText(ResetPassword.this, finalMessage +"",Toast.LENGTH_LONG).show();

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
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


    private class resetPassword extends AsyncTask<Void, Void, Void> {

        String userStatus;
        String stdId=studentId.getText().toString(),newPass=newPassword.getText().toString(),confirmPass=confirmPassword.getText().toString();;

        @Override
        protected Void doInBackground(Void... params){

            URL url = null;
            try {
                url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/user/resetPassword&"+stdId+"&"+newPass);

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
                Toast.makeText(ResetPassword.this, "Password Changed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ResetPassword.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else {
                Toast.makeText(ResetPassword.this, "User Does not exixts", Toast.LENGTH_LONG).show();
                studentId.setError("not found");
                studentId.requestFocus();
            }
            super.onPostExecute(result);
        }

    }


    @Override
    public void onBackPressed() {
        Intent i=new Intent(ResetPassword.this,LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        super.onBackPressed();
    }


    @Override
    public boolean onSupportNavigateUp(){
        Intent i=new Intent(ResetPassword.this,LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        return true;
    }
}