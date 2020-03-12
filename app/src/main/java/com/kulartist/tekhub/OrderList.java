package com.kulartist.tekhub;

import android.app.ProgressDialog;
import android.content.Context;
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

import static com.kulartist.tekhubandroid.SplashScreen.currentIP;


public class OrderList extends BottomMenu {

    ProgressDialog progressDialog;
    TextView itmName;
    ListView   itemListView;;
    JSONArray itemListArray= new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayout(R.layout.activity_order_list);
        getMenuIcon(R.drawable.orderlist,R.id.orderlist);
        setActionBarTitle("Order List");

        itmName=findViewById(R.id.item_name);
        progressDialog=new ProgressDialog(this);

        System.out.println("#########################"+DatabaseObjects.orderList.toString());

        if(DatabaseObjects.orderList.toString().equals("[]") || DatabaseObjects.orderList.toString().equals("")|| DatabaseObjects.orderList.toString().isEmpty()) {
            SharedPreferences sp = getSharedPreferences("saveUser" , Context.MODE_PRIVATE);
            LoginActivity.currentUser = sp.getString("userSavedId", "");
            new GetOrderList().execute();
        }
        else
        {
            try {
                getRecyclerData(DatabaseObjects.orderList);
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
        Intent i = new Intent(OrderList.this, ItemList.class);
       // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }


    public void getRecyclerData(final JSONArray mainArray) throws JSONException {

        final String[] orderId=new String[mainArray.length()];
        final String[] itemID=new String[mainArray.length()];
        final String[] itemName=new String[mainArray.length()];
        final String[] itemDesc=new String[mainArray.length()];
        final String[] itemCondition=new String[mainArray.length()];
        final String[] orderDate=new String[mainArray.length()];
        final String[] pickupDate=new String[mainArray.length()];
        final String[] returnDate=new String[mainArray.length()];


        for(int j=0;j<mainArray.length();j++)
        {
            JSONObject a =new JSONObject();
            a=mainArray.getJSONObject(j);
            orderId[j]=a.getString("orderId");
            itemID[j]=a.getString("itemId");
            itemName[j]=a.getString("itemname");
            itemDesc[j]=a.getString("itemDesc");
            itemCondition[j]=a.getString("itemCondition");
            orderDate[j]=a.getString("orderDate");
            pickupDate[j]=a.getString("pickupDate");
            returnDate[j]=a.getString("returnDate");
        }

        itemListView = (ListView)findViewById(R.id.simpleListView);
        OrderListAdapter customAdapter = new OrderListAdapter(getApplicationContext(), itemName,returnDate);
        itemListView.setAdapter(customAdapter);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                {
                    Intent in = new Intent(OrderList.this, OrderDetails.class);
                    try {
                        in.putExtra("itemId",itemID[position]);
                        in.putExtra("OrderDetailsObject",mainArray.getJSONObject(position).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(in);
                }

            }
        });



    }


    private class GetOrderList extends AsyncTask<Void, Void, Void> {

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
                url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/borrow/getOrderList&"+ LoginActivity.currentUser);

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
                itemListArray=obj.getJSONArray("OrderList");
                DatabaseObjects.orderList=obj.getJSONArray("OrderList");
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


}







