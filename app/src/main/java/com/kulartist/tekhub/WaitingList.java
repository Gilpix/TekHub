package com.kulartist.tekhub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kulartist.tekhubadmin.ResolveIssue;
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
import java.util.ArrayList;

import static com.kulartist.tekhubandroid.SplashScreen.currentIP;


public class WaitingList extends BottomMenu {

    ProgressDialog progressDialog;
    TextView itmName;
    ListView itemListView;;
    JSONArray itemListArray= new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayout(R.layout.activity_waiting_list);
        getMenuIcon(R.drawable.waitinglist,R.id.waiting_list);
        setActionBarTitle("Waiting List");

        itmName=findViewById(R.id.item_name);

        progressDialog=new ProgressDialog(this);


        if(DatabaseObjects.waitingList.toString().equals("[]") || DatabaseObjects.waitingList.toString().equals("")|| DatabaseObjects.waitingList.toString().isEmpty() ){
            SharedPreferences sp = getSharedPreferences("saveUser" , Context.MODE_PRIVATE);
            LoginActivity.currentUser = sp.getString("userSavedId", "");
            new GetWaitingList().execute();
        }
        else
        {
            try {
                getRecyclerData(DatabaseObjects.waitingList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setCustomView(textView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(WaitingList.this, ItemList.class);
        startActivity(i);
        finish();
    }


    public void getRecyclerData(final JSONArray mainArray) throws JSONException {

        final ArrayList<String> itemID=new ArrayList<String>(mainArray.length());
        final ArrayList<String> itemName=new ArrayList<String>(mainArray.length());
        final ArrayList<String> availableDate=new ArrayList<String>(mainArray.length());

        for(int j=0;j<mainArray.length();j++)
        {
            JSONObject a =new JSONObject();
            a=mainArray.getJSONObject(j);
            itemID.add(a.getString("itemId"));
            itemName.add(a.getString("itemname"));
            availableDate.add(a.getString("availableDate"));
        }


        itemListView = (ListView)findViewById(R.id.simpleListView);
        final WaitingListAdapter customAdapter = new WaitingListAdapter(WaitingList.this, itemName,availableDate);
        itemListView.setAdapter(customAdapter);

        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final int which_position=position;
                new AlertDialog.Builder(WaitingList.this).setIcon(R.drawable.ic_delete).
                        setTitle("Edit Waiting List?").setMessage("Do you want to remove this item ?").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String number=itemID.get(position);
                                new DeleteFromWaitingList(LoginActivity.currentUser,number).execute();
                                itemName.remove(position);
                                 availableDate.remove(position);
                                customAdapter.notifyDataSetChanged();


                            }
                        }).setNegativeButton("No",null).show();
                return true;
            }
        });



    }


    private class GetWaitingList extends AsyncTask<Void, Void, Void> {

        String userStatus;

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
                url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/WaitingItem/getWaitingList&"+ LoginActivity.currentUser);

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
                itemListArray=obj.getJSONArray("WaitingList");
                DatabaseObjects.waitingList=obj.getJSONArray("WaitingList");

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

            try {
                getRecyclerData(itemListArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.hide();
            super.onPostExecute(result);
        }
    }


    private class DeleteFromWaitingList extends AsyncTask<Void, Void, Void> {

        String userId,itemId,userStatus;
        java.sql.Date pickupDate,  returnDate;


        public DeleteFromWaitingList(String userId, String itemId) {
            this.userId=userId;
            this.itemId=itemId;

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

                url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/WaitingItem/deleteWaitingItem&"+userId+"&"+itemId);
                // url = new URL("http://192.168.2.250:8080/OnlineQuiz/mad312group2/quizuser/registerUser&"+mailAdd+"&"+firstName+"&"+lastName+"&"+passwrd);

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
                Toast.makeText(WaitingList.this, "Deleted Sucessfully", Toast.LENGTH_SHORT).show();
                try {
                    DatabaseObjects.waitingList = new JSONArray("[]");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            progressDialog.hide();
            super.onPostExecute(result);

        }
    }


}







