package com.zebia.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.zebia.model.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TweetArrayAdapter extends ArrayAdapter<Item> {
    static final int DELTA = 60 * 60000;
    static final int COLOR_STATUS = Color.parseColor("#a9f300"); //  Integer.parseInt("00ab13", 16);
    static final int COLOR_OWNER = Color.parseColor("#ffb200"); //Integer.parseInt("ffb200", 16);

    Context context;
    int layoutResourceId;
    List<Item> data = new ArrayList<Item>();

    public TweetArrayAdapter(Context context, int layoutResourceId, Item[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data.addAll(Arrays.asList(data));
    }

    public TweetArrayAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ItemHolder();
            //holder.txtItemTitle = (TextView) row.findViewById(R.id.tx_item);
//            holder.txtItemStatus = (TextView) row.findViewById(R.id.tx_item_status);
//            holder.txtItemOwner = (TextView) row.findViewById(R.id.tx_item_owner);

            row.setTag(holder);
        } else {
            holder = (ItemHolder) row.getTag();
        }

        holder.fromItem(getItem(position));

        return row;
    }

    static class ItemHolder {

        TextView txtItemTitle;

        public void fromItem(Item item) {
            // TITLE
            txtItemTitle.setText(item.toString());

//            // DONE
//            txtItemTitle.getPaint().setStrikeThruText(item.isDone());
//            txtItemTitle.setTextColor(item.isDone() ? Color.GRAY : item.getGroup().getColor());
//
//            // STARRED
//            txtItemTitle.setTypeface(item.isImportant() ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
//
//            // STATUS
//            if (item.getTimestamp().getTime() > ((new Date().getTime() - DELTA))) {
//                txtItemStatus.setText("New");
//                txtItemStatus.setVisibility(View.VISIBLE);
//                txtItemStatus.setTextColor(item.isDone() ? Color.GRAY : COLOR_STATUS);
//                txtItemStatus.getPaint().setStrikeThruText(item.isDone());
//            } else {
//                txtItemStatus.setVisibility(View.GONE);
//            }
//
//            // OWNER
//            if (item.getOwner() != 0) {
//                txtItemOwner.setText("By " + item.getOwner());  // TODO
//                txtItemOwner.setVisibility(View.VISIBLE);
//                txtItemOwner.setTextColor(item.isDone() ? Color.GRAY : COLOR_OWNER);
//                txtItemOwner.getPaint().setStrikeThruText(item.isDone());
//            } else {
//                txtItemOwner.setVisibility(View.INVISIBLE);
//            }

        }
    }

    @Override
    public void clear() {
        this.data.clear();
        super.clear();
    }

    @Override
    public void add(Item item) {
        this.data.add(item);
        super.add(item);
    }

    @Override
    public void addAll(Item... items) {
        super.addAll(items);
        this.data.addAll(Arrays.asList(items));
    }

    @Override
    public void addAll(Collection<? extends Item> collection) {
        super.addAll(collection);
        this.data.addAll(collection);
    }

    public List<Item> getData() {
        return data;
    }
}
