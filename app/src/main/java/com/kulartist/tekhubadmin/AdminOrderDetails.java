package com.kulartist.tekhubadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

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

public class AdminOrderDetails extends AppCompatActivity {
    private ListView orderListView;
    private ProgressDialog progressDialog;
    private JSONArray orderListArray;
    //private String userStatus;
    private SearchView simpleSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_details);

        orderListView=(ListView)findViewById(R.id.adminOrderDertailsListView);
        //simpleSearchView = (SearchView) findViewById(R.id.adminOrderSearchBar);


        progressDialog = new ProgressDialog(this);

        new getOrderDetailsForAdmin().execute();



    }

    private class getOrderDetailsForAdmin extends AsyncTask<Void, Void, Void> {

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

                url = new URL("http://" + currentIP + ":8080/TekHub-WebCalls/webcall/admin/listOrders");

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
                orderListArray = obj.getJSONArray("orderList");


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
                try {
                    getRecyclerData(orderListArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }
            else
                Toast.makeText(AdminOrderDetails.this,"Unable to fetch",Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
        }
    }

    private void getRecyclerData(final JSONArray mainArray) throws JSONException {
        final ArrayList<String> orderId = new ArrayList<String>(mainArray.length());
        final ArrayList<String> itemId = new ArrayList<String>(mainArray.length());
        final ArrayList<String> itemname = new ArrayList<String>(mainArray.length());
        final ArrayList<String> userId = new ArrayList<String>(mainArray.length());
        final ArrayList<String> name = new ArrayList<String>(mainArray.length());
        final ArrayList<String> orderDate = new ArrayList<String>(mainArray.length());
        final ArrayList<String> pickupDate = new ArrayList<String>(mainArray.length());
        final ArrayList<String> returnDate = new ArrayList<String>(mainArray.length());

        for(int j=0;j<mainArray.length();j++)
        {
            JSONObject a =new JSONObject();
            a=mainArray.getJSONObject(j);
            orderId.add(a.getString("orderId"));
            itemId.add(a.getString("itemId"));
            itemname.add(a.getString("itemname"));
            userId.add(a.getString("userId"));
            name.add(a.getString("name"));
            orderDate.add(a.getString("orderDate"));
            pickupDate.add(a.getString("pickupDate"));
            returnDate.add(a.getString("returnDate"));

        }

        orderListView = (ListView)findViewById(R.id.adminOrderDertailsListView);
        final AdminOrderDetailsAdapter adminOrderDetailsAdapter=new AdminOrderDetailsAdapter(AdminOrderDetails.this,orderId,itemId,itemname,userId,name,orderDate,pickupDate,returnDate);
        orderListView.setAdapter(adminOrderDetailsAdapter);


    }

}
