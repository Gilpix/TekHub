package com.kulartist.tekhubadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kulartist.tekhubandroid.R;

import java.util.ArrayList;

public class IssueListAdapter extends BaseAdapter {


    private ArrayList<String> feedId;
    private ArrayList<String> itemId;
    private ArrayList<String> itemName;
    private ArrayList<String> msg;

    private LayoutInflater inflter;

    public IssueListAdapter(Context context, ArrayList<String> feedId, ArrayList<String> itemId, ArrayList<String> itemName, ArrayList<String> message) {
        this.feedId = feedId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.msg = message;
        inflter = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemId.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflter.inflate(R.layout.issue_list_adapter, null);
        TextView id1 = (TextView) view.findViewById(R.id.adapterFeedbackId);
        TextView id2 = (TextView) view.findViewById(R.id.adapterItemId);
        TextView name = (TextView) view.findViewById(R.id.adapterItemName);
        TextView message = (TextView) view.findViewById(R.id.adapterMessage);






        id1.setText(feedId.get(i));
        id2.setText(itemId.get(i));
        name.setText(itemName.get(i));
        message.setText(msg.get(i));




        return view;
    }
}
