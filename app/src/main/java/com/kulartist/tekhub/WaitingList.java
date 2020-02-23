package com.kulartist.tekhub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    JSONObject itemListObject=new JSONObject();







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        getLayout(R.layout.activity_waiting_list);
        getMenuIcon(R.drawable.waitinglist,R.id.waiting_list);
        // setActionBarTitle("My Profile");



        LoginActivity loginActivity=new LoginActivity();
        //loginActivity.setActionBarTitle("Borrowed Items");

        itmName=findViewById(R.id.item_name);


        progressDialog=new ProgressDialog(this);

        new GetWaitingList().execute();














    }









    public void getRecyclerData(final JSONArray mainArray) throws JSONException {

        final String[] orderId=new String[mainArray.length()];
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
                itemListArray=obj.getJSONArray("WaitingList");


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


            //Toast.makeText(ScoreCard.this,"Login Successful - " +userStatus,Toast.LENGTH_SHORT).show();
            progressDialog.hide();
            super.onPostExecute(result);
        }
    }















}







