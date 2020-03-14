package com.kulartist.tekhubadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

public class AdminEditItems extends AppCompatActivity {

    private String id,name,desc,condition,userStatus;
    private EditText newName,newDesc;
    private RadioButton fresh,used;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_items);

        Intent intent = getIntent();
        id=intent.getStringExtra("id");
        name=intent.getStringExtra("name");
        desc=intent.getStringExtra("desc");
        condition=intent.getStringExtra("condition");

        newName=(EditText)findViewById(R.id.SelectedItemName);
        newDesc=(EditText)findViewById(R.id.SelectedItemDesc);
        submitButton=(Button)findViewById(R.id.updateItemButton);
        fresh=(RadioButton)findViewById(R.id.newRadio);
        used=(RadioButton)findViewById(R.id.usedRadio);

        getSupportActionBar().hide();


        newName.setText(name);
        newDesc.setText(desc);

        if(condition.equals("New"))
            fresh.setChecked(true);
        else if (condition.equals("Used"))
            used.setChecked(true);
        else
            fresh.setChecked(true);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=newName.getText().toString();
                desc=newDesc.getText().toString();
                if(fresh.isChecked())
                    condition="New";
                else if(used.isChecked())
                    condition="Used";


                new itemUpdate(id,name,desc,condition).execute();
            }
        });



    }

    public void backToAdminItem(View view) {
        Intent i=new Intent(AdminEditItems.this,AdminItems.class);
        startActivity(i);
        finish();
    }

    private class itemUpdate extends AsyncTask<Void, Void, Void> {

        String itemToBeUpdated,name,desc,condition;
        public itemUpdate(String id,String name,String desc,String condition) {

            this.itemToBeUpdated = id;
            this.name = name;
            this.desc = desc;
            this.condition = condition;
        }

        @Override
        protected void onPreExecute() {
            System.out.println(itemToBeUpdated);
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {

            URL url = null;

            try {


                url = new URL("http://" + currentIP + ":8080/TekHubWebCalls/webcall/admin/updateItem&"+ name+"&"+desc+"&"+condition+"&"+itemToBeUpdated);

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

            if(userStatus.equals("Ok")) {
                Intent i = new Intent(AdminEditItems.this, AdminItems.class);
                startActivity(i);
                Toast.makeText(AdminEditItems.this,"Item updated to database",Toast.LENGTH_SHORT).show();
            }
            else {
                Intent i = new Intent(AdminEditItems.this, AdminItems.class);
                startActivity(i);
                Toast.makeText(AdminEditItems.this, "Item updation failed", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }
}
