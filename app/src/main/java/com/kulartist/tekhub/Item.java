package com.kulartist.tekhub;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

public class Item extends AppCompatActivity {

    ListView   itemListView;;
    JSONArray itemListArray= new JSONArray();
    JSONObject itemListObject=new JSONObject();

    ProgressDialog progressDialog;

    RelativeLayout waitingCardButton;
    TextView availability;
    Button waiting_button,checkAvailButton,borrowButton;

    TextView itmCond,itmAvail,itmName,itmDesc,borrowTimes;
    JSONObject itemObject;
    String itemJsonString,itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);


        itmName=findViewById(R.id.item_name_text);
        itmDesc=findViewById(R.id.item_desc_text);
        itmAvail=findViewById(R.id.item_availability);
        itmCond=findViewById(R.id.item_condition);
        borrowTimes=findViewById(R.id.item_borrowed_no);

        checkAvailButton=findViewById(R.id.check_availability_button);
        waitingCardButton=findViewById(R.id.waiting_realative);
        availability=findViewById(R.id.item_availability);
        waiting_button=findViewById(R.id.waiting_button);
        borrowButton=findViewById(R.id.borrow_button);

        LoginActivity loginActivity=new LoginActivity();
       // loginActivity.setActionBarTitle("Item Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog=new ProgressDialog(this);









        Intent i=getIntent();
        itemJsonString=i.getStringExtra("ItemDetailsObject");
        itemId=i.getStringExtra("ItemId");





        try {
            itemObject=new JSONObject(itemJsonString);

            if(itemObject.getString("isAvailable").equals("1")) {
                waiting_button.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                checkAvailButton.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }
            else if(itemObject.getString("isAvailable").equals("0"))
            {
                borrowButton.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }

            itmCond.setText(itemObject.getString("itemCondition"));

            if(itemObject.getString("isAvailable").equals("1"))
            itmAvail.setText("Yes");
            else
                itmAvail.setText("No");

            itmDesc.setText(itemObject.getString("itemDesc"));
            itmName.setText(itemObject.getString("itemname"));
            borrowTimes.setText(itemObject.getString("borrowNum")+" times");





        } catch (JSONException e) {
            e.printStackTrace();
        }


        new getFeedbackList().execute();
        System.out.println("###############"+itemId);


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    public void borrowSelectedItem(View view) {
        Intent in = new Intent(getBaseContext(), Order.class);
        in.putExtra("ItemId",itemId);
        //System.out.println("###########################"+itemId);
        startActivity(in);
    }








    public void getRecyclerData(final JSONArray mainArray) throws JSONException {

        final String[] name=new String[mainArray.length()];
        final String[] rating=new String[mainArray.length()];
        final String[] message=new String[mainArray.length()];

        final String[] feedDate=new String[mainArray.length()];






        for(int j=0;j<mainArray.length();j++)
        {
            JSONObject a =new JSONObject();
            a=mainArray.getJSONObject(j);
            name[j]=a.getString("name");
            rating[j]=a.getString("rating");
            message[j]=a.getString("message");

            feedDate[j]=a.getString("feedDate");

        }


        itemListView = (ListView)findViewById(R.id.simpleListView);
        feedbackListAdapter customAdapter = new feedbackListAdapter(getApplicationContext(), name,rating,message);
        itemListView.setAdapter(customAdapter);
        setListViewHeightBasedOnChildren(itemListView);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                {


                }

            }
        });



    }




    private class getFeedbackList extends AsyncTask<Void, Void, Void> {

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

                url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/feedback/getFeedbackList&"+itemId);
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
                itemListArray=obj.getJSONArray("FeedbackList");


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



    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }



}
























