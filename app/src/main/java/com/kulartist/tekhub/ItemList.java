package com.kulartist.tekhub;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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


public class ItemList extends BottomMenu {

    private FloatingActionButton fab_main;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView filter_newest_fab, filter_popularity_fab,filter_available_fab;
    Boolean isOpen = false;
    ProgressDialog progressDialog;
    SearchView searchView;
    TextView itmName;
    ListView   itemListView;;
    JSONArray itemListArray= new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayout(R.layout.activity_item_list);
        getMenuIcon(R.drawable.home,R.id.itemlist);

        itmName=findViewById(R.id.item_name);
        searchView=findViewById(R.id.search_bar);

        fab_main = findViewById(R.id.fab);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        filter_newest_fab = (TextView) findViewById(R.id.filter_newest_fab);
        filter_popularity_fab = (TextView) findViewById(R.id.filter_popularity_fab);
        filter_available_fab=findViewById(R.id.filter_available_fab);
        progressDialog=new ProgressDialog(this);

        if(DatabaseObjects.itemList.toString().equals("[]") || DatabaseObjects.itemList.toString().equals("") || DatabaseObjects.itemList.toString().isEmpty())
        {
            new UpdateItemAvailability().execute();
            new GetItemList().execute();
        }
        else
        {
            try {
                getRecyclerData(DatabaseObjects.itemList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            DatabaseObjects db=new DatabaseObjects();


            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    if(db.sortArrayListBySearch(query).equals("[]")){
                        Toast.makeText(ItemList.this, "No Match found",Toast.LENGTH_LONG).show();
                    }else{
                        callSearch(query);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    db.sortedJsonArrayBySearch=new JSONArray();
                    getRecyclerData(db.sortArrayListBySearch(newText));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return true;
            }

            public void callSearch(String query) throws JSONException {
                //Do searching
                db.sortedJsonArrayBySearch=new JSONArray();
                getRecyclerData(db.sortArrayListBySearch(query));
            }

        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        alertBoxDisplay();
        //super.onBackPressed();
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
                    }
                })
                .setNegativeButton("No", null);
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("TekHub");
        //alert.setCustomTitle(textView);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        alert.setIcon(R.drawable.tehublogo);
        alert.show();
        // alert.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.color.white);
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(null, Typeface.BOLD);
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setPadding(20,20,20,20);
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        //alert.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource((R.drawable.circular_button_white));

    }

    public void filterByAvailablity(View view) throws JSONException {
        Toast.makeText(getApplicationContext(), "filterByAvailablity", Toast.LENGTH_SHORT).show();
        DatabaseObjects db=new DatabaseObjects();
        if(DatabaseObjects.sortedJsonArrayByPopularity.toString().equals("[]") || DatabaseObjects.sortedJsonArrayByPopularity.toString().equals("")) {
            db.sortArrayListByAvailability();
            getRecyclerData(DatabaseObjects.sortedJsonArrayByAvailability);

        }
        else
        {
            try {
                getRecyclerData(DatabaseObjects.sortedJsonArrayByAvailability);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void filterByPopularity(View view) throws JSONException {

        Toast.makeText(getApplicationContext(), "filterByPopularity", Toast.LENGTH_SHORT).show();

        DatabaseObjects db=new DatabaseObjects();
        if(DatabaseObjects.sortedJsonArrayByPopularity.toString().equals("[]") || DatabaseObjects.sortedJsonArrayByPopularity.toString().equals("")) {
            db.parseArrayFromJson();
            db.sortArrayListByPopularity();
            db.storeSortedArrayInList(DatabaseObjects.sortedJsonArrayByPopularity);
            getRecyclerData(DatabaseObjects.sortedJsonArrayByPopularity);

        }
        else
        {
            try {
                getRecyclerData(DatabaseObjects.sortedJsonArrayByPopularity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //new GetItemListByPopularity().execute();

    }

    public void filterByNewestItemsAdded(View view) throws JSONException {
        Toast.makeText(getApplicationContext(), "filterByNewestItemsAdded", Toast.LENGTH_SHORT).show();
        //new getItemListByNewestItem().execute();
        DatabaseObjects db=new DatabaseObjects();
        if(DatabaseObjects.sortedJsonArrayByDate.toString().equals("[]") || DatabaseObjects.sortedJsonArrayByDate.toString().equals("")) {
            db.parseArrayFromJson();
            db.sortArrayListByDate();
            db.storeSortedArrayInList(DatabaseObjects.sortedJsonArrayByDate);
            getRecyclerData(DatabaseObjects.sortedJsonArrayByDate);

        }
        else
        {
            try {
                getRecyclerData(DatabaseObjects.sortedJsonArrayByDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void fliterOptions(View view) {
        if (isOpen) {

            filter_newest_fab.setVisibility(View.INVISIBLE);
            filter_available_fab.setVisibility(View.INVISIBLE);
            filter_popularity_fab.setVisibility(View.INVISIBLE);
            filter_popularity_fab.startAnimation(fab_close);
            filter_available_fab.startAnimation(fab_close);
            filter_newest_fab.startAnimation(fab_close);
            fab_main.startAnimation(fab_anticlock);
            filter_popularity_fab.setClickable(false);
            filter_available_fab.setClickable(false);
            filter_newest_fab.setClickable(false);
            isOpen = false;
        } else {
            filter_newest_fab.setVisibility(View.VISIBLE);
            filter_available_fab.setVisibility(View.VISIBLE);
            filter_popularity_fab.setVisibility(View.VISIBLE);
            filter_popularity_fab.startAnimation(fab_open);
            filter_available_fab.startAnimation(fab_open);
            filter_newest_fab.startAnimation(fab_open);
            fab_main.startAnimation(fab_clock);
            filter_popularity_fab.setClickable(true);
            filter_available_fab.setClickable(true);
            filter_newest_fab.setClickable(true);
            isOpen = true;
        }
    }


    public void getRecyclerData(final JSONArray mainArray) throws JSONException {

        final String[] itemName=new String[mainArray.length()];
        final String[] itemAvailable=new String[mainArray.length()];
       final String[] itemID=new String[mainArray.length()];

        final String[] itemDesc=new String[mainArray.length()];
        final String[] availableDate=new String[mainArray.length()];
        final String[] itemCondition=new String[mainArray.length()];
        final String[] borrowNum=new String[mainArray.length()];
        final String[] addedDate=new String[mainArray.length()];
        final String[] avgRating=new String[mainArray.length()];


        for(int j=0;j<mainArray.length();j++)
        {
            avgRating[j]="0";
            JSONObject a =new JSONObject();
            a=mainArray.getJSONObject(j);
            itemName[j]=a.getString("itemname");
            itemAvailable[j]=a.getString("isAvailable");
            itemID[j]=a.getString("itemId");
            itemDesc[j]=a.getString("itemDesc");
            availableDate[j]=a.getString("availableDate");
            itemCondition[j]=a.getString("itemCondition");
            borrowNum[j]=a.getString("borrowNum");
            addedDate[j]=a.getString("addedDate");
            avgRating[j]=a.getString("avgRating");
            if(avgRating[j]==null)
                avgRating[j]="0";
        }


        itemListView = (ListView)findViewById(R.id.simpleListView);
        ItemListAdapter customAdapter = new ItemListAdapter(getApplicationContext(), itemName,itemAvailable);
        itemListView.setAdapter(customAdapter);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                {
                    Intent in = new Intent(ItemList.this, Item.class);
                    try {
                        in.putExtra("ItemId",mainArray.getJSONObject(position).getString("itemId"));
                        in.putExtra("ItemDetailsObject",mainArray.getJSONObject(position).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);

                }

            }
        });

    }


    private class GetItemList extends AsyncTask<Void, Void, Void> {

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
                url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/item/getItemList");

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
                itemListArray=obj.getJSONArray("itemLists");
                DatabaseObjects.itemList=itemListArray;

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


    private class UpdateItemAvailability extends AsyncTask<Void, Void, Void> {

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
                url = new URL("http://"+currentIP+":8080/TekHubWebCalls/webcall/item/updateItemAvailability");

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

            try {
                getRecyclerData(itemListArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.hide();
            super.onPostExecute(result);
        }
    }


    @Override
    protected void onStop() {
        Intent i = new Intent();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        super.onStop();
    }

}







