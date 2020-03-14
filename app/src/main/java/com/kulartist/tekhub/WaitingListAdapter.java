package com.kulartist.tekhub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.kulartist.tekhubandroid.R;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class WaitingListAdapter extends BaseAdapter {
        Context context;
    private ArrayList<String> itemNames,itemAvable,itemImage;

        LayoutInflater inflter;

        public WaitingListAdapter(Context applicationContext, ArrayList<String> categoryList,ArrayList<String> itemAvable,ArrayList<String> itmImage) {
            this.context = context;
            this.itemNames = categoryList;
            this.itemAvable=itemAvable;
            this.itemImage=itmImage;

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
            ImageView item_image=(ImageView) view.findViewById(R.id.item_image);



            byte[] decodedString = Base64.decode(itemImage.get(i), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            item_image.setImageBitmap(decodedByte);


            String dt = itemAvable.get(i);  // Start date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DATE, 2);  // number of days to add
            dt = sdf.format(c.getTime());  // dt is now the new date



            try {
                if(date.before(getCurrentDate()))
                {
                    avable.setTextColor(Color.GREEN);
                    avable.setText("Available");
                }
                else
                    avable.setText("Available On : "+dt);


            } catch (ParseException e) {
                e.printStackTrace();
            }

            name.setText(""+itemNames.get(i));
//            avable.setText("Available On : "+itemAvable.get(i));

            return view;
        }

    public java.util.Date getCurrentDate() throws ParseException {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return formatter.parse(formatter.format(date));
    }

    }
