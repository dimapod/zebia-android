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

public class SongArrayAdapter extends ArrayAdapter<Item> {
    static final int DELTA = 60 * 60000;
    static final int COLOR_STATUS = Color.parseColor("#a9f300");
    static final int COLOR_OWNER = Color.parseColor("#ffb200");

    Context context;
    int layoutResourceId;

    public SongArrayAdapter(Context context) {
        super(context, R.layout.song_list);
        this.layoutResourceId = R.layout.song_list;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SongHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new SongHolder();
            holder.txtItemText = (TextView) row.findViewById(R.id.tx_song_text);
            holder.txtItemFromUserName = (TextView) row.findViewById(R.id.tx_song_from_user);

            row.setTag(holder);
        } else {
            holder = (SongHolder) row.getTag();
        }

        holder.fromSong(getItem(position));

        return row;
    }

    static class SongHolder {
        TextView txtItemText;
        TextView txtItemFromUserName;

        public void fromSong(Item item) {
            txtItemText.setText(item.getText());
            txtItemFromUserName.setText(item.getFromUserName());
        }
    }

}
