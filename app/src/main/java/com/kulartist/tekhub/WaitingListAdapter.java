package com.kulartist.tekhub;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.kulartist.tekhubandroid.R;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class WaitingListAdapter extends BaseAdapter {
        Context context;
    private ArrayList<String> itemNames,itemAvable;

        LayoutInflater inflter;

        public WaitingListAdapter(Context applicationContext, ArrayList<String> categoryList,ArrayList<String> itemAvable) {
            this.context = context;
            this.itemNames = categoryList;
            this.itemAvable=itemAvable;

            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return itemNames.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.activity_waiting_list_adapter, null);
            TextView name = (TextView) view.findViewById(R.id.item_name);
            TextView avable = (TextView) view.findViewById(R.id.item_avaibl);
            Date date=Date.valueOf(itemAvable.get(i));
            try {
                if(date.before(getCurrentDate()))
                {
                    avable.setTextColor(Color.GREEN);
                    avable.setText("Available");
                }
                else
                    avable.setText("Available On : "+itemAvable.get(i));


            } catch (ParseException e) {
                e.printStackTrace();
            }

            name.setText("NAME : "+itemNames.get(i));
//            avable.setText("Available On : "+itemAvable.get(i));

            return view;
        }

    public java.util.Date getCurrentDate() throws ParseException {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return formatter.parse(formatter.format(date));
    }

    }
