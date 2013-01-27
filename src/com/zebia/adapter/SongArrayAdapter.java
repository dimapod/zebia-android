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
import com.zebia.model.Song;

public class SongArrayAdapter extends ArrayAdapter<Song> {
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
            holder.txtSongTitle = (TextView) row.findViewById(R.id.tx_song_title);
            holder.txtSongArtist = (TextView) row.findViewById(R.id.tx_song_artist);
            holder.txtSongRelease = (TextView) row.findViewById(R.id.tx_song_release);

            row.setTag(holder);
        } else {
            holder = (SongHolder) row.getTag();
        }

        holder.fromSong(getItem(position));

        return row;
    }

    static class SongHolder {
        TextView txtSongTitle;
        TextView txtSongArtist;
        TextView txtSongRelease;

        public void fromSong(Song song) {
            txtSongTitle.setText(song.getTitle());
            txtSongArtist.setText(song.getArtist_name());
            txtSongRelease.setText(song.getRelease());
        }
    }

}
