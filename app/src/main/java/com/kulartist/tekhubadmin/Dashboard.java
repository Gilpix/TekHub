package com.kulartist.tekhubadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.kulartist.tekhubandroid.LoginActivity;
import com.kulartist.tekhubandroid.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import static com.kulartist.tekhubandroid.SplashScreen.currentIP;


public class Dashboard extends AppCompatActivity {


    private Button item,students,issue,adminOrderDetails;
    private ImageView logout;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        item=(Button)findViewById(R.id.manageItems);
        students=(Button)findViewById(R.id.manageStudents);
        issue=(Button)findViewById(R.id.resolveIssue);
        logout=(ImageView)findViewById(R.id.adminLogout);
        adminOrderDetails=(Button) findViewById(R.id.orderDetails);
        progressDialog = new ProgressDialog(this);

        getSupportActionBar().hide();

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this,AdminItems.class);
                startActivity(i);
            }
        });

        students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this,StudentList.class);
                startActivity(i);
            }
        });


        issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this,ResolveIssue.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dashboard.adminLogout().execute();


            }
        });

        adminOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this,AdminOrderDetails.class);
                startActivity(i);
            }
        });

    }


    @Override
    public void onBackPressed() {
        alertBoxDisplay();
    }


    public void alertBoxDisplay()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        moveTaskToBack(true);
                        Process.killProcess(Process.myPid());
                        System.exit(1);
                        Dashboard.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null);
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("TekHub");
        //alert.setCustomTitle(textView);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        alert.setIcon(R.drawable.tehublogo);
        alert.show();
        // alert.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.color.white);
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(null, Typeface.BOLD);
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setPadding(20,20,20,20);
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        //alert.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource((R.drawable.circular_button_white));

    }


    private class adminLogout extends AsyncTask<Void, Void, Void> {

        String userStatus;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {

            URL url = null;

            try {

                url = new URL("http://" + currentIP + ":8080/TekHubWebCalls/webcall/admin/logout");
                HttpURLConnection client = null;
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();
                System.out.println("\n Sending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);
                InputStreamReader myInput = new InputStreamReader(client.getInputStream());

                BufferedReader in = new BufferedReader(myInput);
                String inputLine;
                StringBuffer response = new StringBuffer();


                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());

                JSONObject obj = new JSONObject(response.toString());
                userStatus = "" + obj.getString("Status");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            if(userStatus.equals("Ok")){
                Toast.makeText(Dashboard.this,"You are logged out",Toast.LENGTH_LONG).show();
                Intent i = new Intent(Dashboard.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                progressDialog.hide();
            }
            else
                Toast.makeText(Dashboard.this,"Unable to logout",Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
        }
    }

}
