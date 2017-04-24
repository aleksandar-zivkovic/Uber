package upm.softwaredesign.uber.utilities;

import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import io.ably.lib.types.BaseMessage;
import io.ably.lib.types.Message;
import io.ably.lib.types.PresenceMessage;
import upm.softwaredesign.uber.MainActivity;

/**
 * Created by Aleksandar on 24/04/2017.
 */

public class AblyAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    private MainActivity mainActivity;

    private String mTripStatus;

    public AblyAdapter(MainActivity mainActivity, String tripStatus) {
        this.mainActivity = mainActivity;
        this.layoutInflater = mainActivity.getLayoutInflater();
        this.mTripStatus = tripStatus;
    }

    public void changeTripStatus(String tripStatus) {
        this.mTripStatus = tripStatus;
        notifyChange();
    }

    private void notifyChange() {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
                Toast.makeText(mainActivity, "Your trip status is: " + mTripStatus ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getCount() {
        return 0;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
