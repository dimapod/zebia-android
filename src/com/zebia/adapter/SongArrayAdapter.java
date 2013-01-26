package com.zebia.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.zebia.R;
import com.zebia.model.Item;

import java.util.ArrayList;
import java.util.List;

public class SongArrayAdapter extends ArrayAdapter<Item> {
    static final int DELTA = 60 * 60000;
    static final int COLOR_STATUS = Color.parseColor("#a9f300");
    static final int COLOR_OWNER = Color.parseColor("#ffb200");

    Context context;
    int layoutResourceId;
    List<Item> data = new ArrayList<Item>();

    public SongArrayAdapter(Context context) {
        super(context, R.layout.item_list);
        this.layoutResourceId = R.layout.item_list;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ItemHolder();
            holder.txtItemText = (TextView) row.findViewById(R.id.tx_item_text);
            holder.txtItemFromUserName = (TextView) row.findViewById(R.id.tx_item_from_user);

            row.setTag(holder);
        } else {
            holder = (ItemHolder) row.getTag();
        }

        holder.fromItem(getItem(position));

        return row;
    }

    static class ItemHolder {
        TextView txtItemText;
        TextView txtItemFromUserName;

        public void fromItem(Item item) {
            txtItemText.setText(item.getText());
            txtItemFromUserName.setText(item.getFromUserName());
        }
    }

}
