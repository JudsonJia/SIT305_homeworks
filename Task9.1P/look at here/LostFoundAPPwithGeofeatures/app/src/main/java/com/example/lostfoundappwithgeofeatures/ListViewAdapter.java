package com.example.lostfoundappwithgeofeatures;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Item> {
    private Context mContext;
    private int mResource;

    public ListViewAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);

        View listItemView = convertView;

        if (listItemView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            listItemView = inflater.inflate(mResource, parent, false);
        }

        TextView textView = listItemView.findViewById(R.id.textView);
        textView.setText(String.format("%s %s %s %s %s", item.getType(), item.getName(), item.getDescription(), item.getDate(), item.getLocation()));

        return listItemView;
    }
}
