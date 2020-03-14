package com.kulartist.tekhubadmin;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
public class AdminItems extends AppCompatActivity {

    private ImageView add;
    private ListView itemListView;
    private SearchView simpleSearchView;
    private ProgressDialog progressDialog;
    private JSONArray itemList;
    private String userStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_items);


        add=(ImageView)findViewById(R.id.adminAddItems);
        itemListView=(ListView)findViewById(R.id.adminItemListVIew);
        simpleSearchView = (SearchView) findViewById(R.id.itemSearchView);
        progressDialog = new ProgressDialog(this);

        getSupportActionBar().hide();

        new getItemListForAdmin().execute();

        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    new AdminItems.getItemListForAdmin().execute();                }
                else
                    callSearch(newText);
                return true;
            }

            public void callSearch(String query) {
                //Do searching
                new AdminItems.getItemListBySearch().execute();
            }

        });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminItems.this,addItem.class);
                startActivity(i);
            }
        });
    }


    public void backItemDashboard(View view) {
        Intent i=new Intent(AdminItems.this,Dashboard.class);
        startActivity(i);
    }


    private class getItemListForAdmin extends AsyncTask<Void, Void, Void> {

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

                url = new URL("http://" + currentIP + ":8080/TekHubWebCalls/webcall/admin/getItems");

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
                itemList = obj.getJSONArray("itemLists");


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
                    getRecyclerData(itemList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }
            else
                Toast.makeText(AdminItems.this,"Unable to fetch",Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
        }
    }


    private void getRecyclerData(final JSONArray mainArray) throws JSONException {
        final ArrayList<String> itemId = new ArrayList<String>(mainArray.length());
        final ArrayList<String> itemName = new ArrayList<String>(mainArray.length());
        final ArrayList<String> itemDesc = new ArrayList<String>(mainArray.length());
        final ArrayList<String> isAvailable = new ArrayList<String>(mainArray.length());
        final ArrayList<String> availableDate = new ArrayList<String>(mainArray.length());
        final ArrayList<String> itemCondi = new ArrayList<String>(mainArray.length());
        final ArrayList<String> borrowNum = new ArrayList<String>(mainArray.length());
        final ArrayList<String> addedDate = new ArrayList<String>(mainArray.length());

        for(int j=0;j<mainArray.length();j++)
        {
            JSONObject a =new JSONObject();
            a=mainArray.getJSONObject(j);

            itemId.add(a.getString("ItemId"));
            itemName.add(a.getString("ItemName"));
            itemDesc.add(a.getString("ItemDesc"));
            if (a.getString("isAvailable").equals("0")) isAvailable.add("No");
            else isAvailable.add("Yes");
            availableDate.add(a.getString("AvailableDate"));
            itemCondi.add(a.getString("ItemCondition"));
            borrowNum.add(a.getString("BorrowNumber"));
            addedDate.add(a.getString("AddedDate"));

        }

        itemListView=(ListView)findViewById(R.id.adminItemListVIew);
        final ItemListAdapter itemListAdapter=new ItemListAdapter(AdminItems.this,itemId,itemName,itemDesc,isAvailable,availableDate,itemCondi,borrowNum,addedDate);
        itemListView.setAdapter(itemListAdapter);

        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int which_position=position;
                new AlertDialog.Builder(AdminItems.this).setIcon(R.drawable.ic_delete).
                        setTitle("Are you sure?").setMessage("Do you want to remove this Item").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String number=""+itemId.get(which_position);
                                new AdminItems.itemDelete(number).execute();
                                itemId.remove(which_position);
                                itemName.remove(which_position);
                                itemDesc.remove(which_position);
                                isAvailable.remove(which_position);
                                availableDate.remove(which_position);
                                itemCondi.remove(which_position);
                                borrowNum.remove(which_position);
                                addedDate.remove(which_position);
                            }
                        }).setNegativeButton("No",null).show();
                return true;
            }
        });

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int which_position=position;
                Intent i = new Intent(AdminItems.this, AdminEditItems.class);
                i.putExtra("id",itemId.get(which_position));
                i.putExtra("name",itemName.get(which_position));
                i.putExtra("desc",itemDesc.get(which_position));
                i.putExtra("condition",itemCondi.get(which_position));
                i.putExtra("availability",isAvailable.get(which_position));
                startActivity(i);
            }
        });

    }


    private class itemDelete extends AsyncTask<Void, Void, Void> {

        String itemToBeDeleted;
        public itemDelete(String studentIdToDelete) {
            this.itemToBeDeleted = studentIdToDelete;
        }

        @Override
        protected void onPreExecute() {
            System.out.println(itemToBeDeleted);
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {

            URL url = null;

            try {
                url = new URL("http://" + currentIP + ":8080/TekHubWebCalls/webcall/admin/deleteItem&"+ itemToBeDeleted);

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
                Toast.makeText(AdminItems.this,"Item Removed from database",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(AdminItems.this,AdminItems.class);
                startActivity(i);
            }
            else
                Toast.makeText(AdminItems.this,"Removel failed",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(AdminItems.this,AdminItems.class);
            startActivity(i);

            super.onPostExecute(result);
        }
    }


    private class getItemListBySearch extends AsyncTask<Void, Void, Void> {
        String searchKey=simpleSearchView.getQuery().toString();

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
                url = new URL("http://" + currentIP + ":8080/TekHubWebCalls/webcall/admin/searchItem&"+searchKey);
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
                itemList = obj.getJSONArray("itemLists");


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
                try {
                    getRecyclerData(itemList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }
            else{
                progressDialog.hide();
                Toast.makeText(AdminItems.this,"Unable to fetch",Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);

        }
    }
}
