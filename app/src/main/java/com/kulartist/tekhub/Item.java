package com.kulartist.tekhub;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

public class Item extends AppCompatActivity {

    TextView availability;
    Button waiting_button,checkAvailButton,borrowButton;
    TextView itmCond,itmAvail,itmName,itmDesc,borrowTimes,avgRating;
    ListView   itemListView;;
    JSONArray itemListArray= new JSONArray();
    ProgressDialog progressDialog;
    RelativeLayout waitingCardButton;
    JSONObject itemObject;
    String itemJsonString,itemId,itemAvailableDate,borrowNum="0";

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
        avgRating=findViewById(R.id.avg_rating);

        setActionBarTitle("Item Details");
        progressDialog=new ProgressDialog(this);


   //get Item details from ItemDetailsObject
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
            borrowNum=itemObject.getString("borrowNum");

            if(itemObject.getString("isAvailable").equals("1"))
            itmAvail.setText("Yes");
            else {
                itmAvail.setText("No");
                itemAvailableDate=itemObject.getString("availableDate");
            }

            itmDesc.setText(itemObject.getString("itemDesc"));
            itmName.setText(itemObject.getString("itemname"));
            borrowTimes.setText(itemObject.getString("borrowNum")+" times");
            if(itemObject.getString("avgRating").isEmpty()||itemObject.getString("avgRating").equals("null"))
                avgRating.setText(itemObject.getString("_")+"/5");
            else
            avgRating.setText(itemObject.getString("avgRating")+"/5");
            if(itemObject.getString("avgRating").length()>3)
                avgRating.setText(itemObject.getString("avgRating").substring(0,3)+"/5");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        new getFeedbackList().execute();

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


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    public void checkAvailableDate(View view) {
        Toast.makeText(this,"Available On : "+itemAvailableDate,Toast.LENGTH_LONG).show();
    }


    public void addToWaitingList(View view) throws JSONException {
        DatabaseObjects.waitingList=new JSONArray("[]");
       new AddToWaitingList(LoginActivity.currentUser,itemId).execute();
    }


    public void borrowSelectedItem(View view) {
        Intent in = new Intent(getBaseContext(), Order.class);
        in.putExtra("ItemId",itemId);
        in.putExtra("borrowNum",borrowNum);
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

            progressDialog.hide();
            super.onPostExecute(result);
        }
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
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


    private class AddToWaitingList extends AsyncTask<Void, Void, Void> {

        String userId,itemId;

        public AddToWaitingList(String userId, String itemId) {
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
                url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/WaitingItem/addWaitingItem&"+userId+"&"+itemId);

                HttpURLConnection client = null;
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();
                System.out.println("\n Sending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);

            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result){
            Toast.makeText(Item.this,"Added to waiting item",Toast.LENGTH_SHORT).show();
            progressDialog.hide();
            super.onPostExecute(result);
        }
    }


}
























