package com.zebia.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import com.zebia.R;
import com.zebia.model.Item;

public class SongDetailsFragment extends Fragment {
    private int index;
    private Item item;

    public SongDetailsFragment() {
    }

    public SongDetailsFragment(int index, Item item) {
        this.index = index;
        this.item = item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        if (item != null) {
            return inflater.inflate(R.layout.song_detail, container, false);
        } else {
            ScrollView scroller = new ScrollView(getActivity());
            TextView text = new TextView(getActivity());
            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    4, getActivity().getResources().getDisplayMetrics());
            text.setPadding(padding, padding, padding, padding);
            scroller.addView(text);
            text.setText("Chose a song...");
            return scroller;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (item != null) {
            TextView txFromUser = (TextView) getView().findViewById(R.id.tx_detail_song_from_user);
            TextView txText = (TextView) getView().findViewById(R.id.tx_detail_song_text);
            TextView txTextLong = (TextView) getView().findViewById(R.id.tx_detail_song_text_long);

            txFromUser.setText(item.getFromUserName());
            txText.setText(item.getText());
            txTextLong.setText(item.getTextLong());
        }
    }

    public int getIndex() {
        return index;
    }

    public Item getItem() {
        return item;
    }
}
