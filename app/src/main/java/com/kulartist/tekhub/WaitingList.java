package com.kulartist.tekhub;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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


        if(DatabaseObjects.waitingList.toString().equals("[]") || DatabaseObjects.waitingList.toString().equals("")) {
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


    public void getRecyclerData(final JSONArray mainArray) throws JSONException {

        final String[] itemID=new String[mainArray.length()];
        final String[] itemName=new String[mainArray.length()];
        final String[] availableDate=new String[mainArray.length()];

        for(int j=0;j<mainArray.length();j++)
        {
            JSONObject a =new JSONObject();
            a=mainArray.getJSONObject(j);
            itemID[j]=a.getString("itemId");
            itemName[j]=a.getString("itemname");
            availableDate[j]=a.getString("availableDate");
        }


        itemListView = (ListView)findViewById(R.id.simpleListView);
        WaitingListAdapter customAdapter = new WaitingListAdapter(getApplicationContext(), itemName,availableDate);
        itemListView.setAdapter(customAdapter);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                {
                }

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


//    private class DeleteFromWaitingList extends AsyncTask<Void, Void, Void> {
//
//        String userId,itemId;
//        java.sql.Date pickupDate,  returnDate;
//
//
//        public DeleteFromWaitingList(String userId, String itemId) {
//            this.userId=userId;
//            this.itemId=itemId;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//
//            progressDialog.setMessage("Loading...");
//            progressDialog.show();
//
//            super.onPreExecute();
//        }
//
//
//        @Override
//        protected Void doInBackground(Void... params){
//
//            URL url = null;
//
//            try {
//
//                url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/WaitingItem/addWaitingItem&"+userId+"&"+itemId);
//                // url = new URL("http://192.168.2.250:8080/OnlineQuiz/mad312group2/quizuser/registerUser&"+mailAdd+"&"+firstName+"&"+lastName+"&"+passwrd);
//
//                HttpURLConnection client = null;
//
//                client = (HttpURLConnection) url.openConnection();
//
//                client.setRequestMethod("GET");
//
//                int responseCode = client.getResponseCode();
//
//
//                System.out.println("\n Sending 'GET' request to URL : " + url);
//
//                System.out.println("Response Code : " + responseCode);
//
//
//            }
//            catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//
//        }
//
//        @Override
//        protected void onPostExecute(Void result){
//            Toast.makeText(WaitingList.this,"Added to waiting item",Toast.LENGTH_SHORT).show();
//            EditText fname,lname,pass,email;
//            // LoginActivity.currentUser=mailAdd;
//
//            progressDialog.hide();
//            super.onPostExecute(result);
//
//        }
//    }


}







