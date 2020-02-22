package com.kulartist.tekhub;





import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kulartist.tekhubandroid.R;


public class feedbackListAdapter extends BaseAdapter {
    Context context;
    String itemNames[],itemAvable[],feedMsg[];

    LayoutInflater inflter;




    public feedbackListAdapter(Context applicationContext, String[] categoryList,String[] itemAvable,String[] feedMsg) {
        this.context = context;
        this.itemNames = categoryList;
        this.itemAvable=itemAvable;
        this.feedMsg=feedMsg;

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return itemNames.length;
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
        view = inflter.inflate(R.layout.feed_adapter, null);
        TextView userName = (TextView) view.findViewById(R.id.item_review_by);
        TextView itemRating = (TextView) view.findViewById(R.id.item_rating);
        TextView itemMsg = (TextView) view.findViewById(R.id.cust_feedback);

        userName.setText("by "+itemNames[i]);
        itemRating.setText(itemAvable[i]+"/5");
        itemMsg.setText(""+feedMsg[i]);



        return view;
    }
}
