package com.example.lostfoundapp;

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
        // 获取当前项的数据
        Item item = getItem(position);

        // 声明一个视图变量来存储列表项视图
        View listItemView = convertView;

        // 检查列表项视图是否为空，如果为空，则从布局资源加载视图
        if (listItemView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            listItemView = inflater.inflate(mResource, parent, false);
        }

        // 找到布局中的 TextView，并将数据设置到其中
        TextView textView = listItemView.findViewById(R.id.textView);
        textView.setText(String.format("%s %s %s %s %s", item.getType(), item.getName(), item.getDescription(), item.getDate(), item.getLocation()));

        return listItemView;
    }
}
