package com.example.amalysheva.viewsapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BalloonsListAdapter extends ArrayAdapter<MapBalloonItem> {

    private static class ViewHolder {
        TextView id;
        TextView address;
    }

    public BalloonsListAdapter(Context context,
                               List<MapBalloonItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.balloon_layout, parent, false);
            viewHolder.id = (TextView) convertView.findViewById(R.id.textId);
            viewHolder.address = (TextView) convertView.findViewById(R.id.textAddress);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MapBalloonItem item = getItem(position);
        Integer id = item.getId()+1;
        viewHolder.id.setText(id.toString());
        viewHolder.address.setText(item.getAddress());
        return convertView;
    }

    public void sort(SortEnum sortEnum) {
        switch (sortEnum){
            case BY_ID:
                sort((a, b) -> Integer.compare(a.getId(), b.getId()));
                break;
            case BY_NAME:
                sort((a, b) -> a.getAddress().compareTo(b.getAddress()));
                break;

        }
    }


    public void add(MapBalloonItem item, SortEnum sortEnum){
        add(item);
        sort(sortEnum);
    }

    public void remove(MapBalloonItem item, SortEnum sortEnum){
        super.remove(item);
        sort(SortEnum.BY_ID);
        for (int i = 0; i< getCount(); i++ ){
            getItem(i).setId(i);
        }
        sort(sortEnum);
    }

}
