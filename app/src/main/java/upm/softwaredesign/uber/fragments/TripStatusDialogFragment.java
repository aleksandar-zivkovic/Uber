package upm.softwaredesign.uber.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import upm.softwaredesign.uber.R;

import static upm.softwaredesign.uber.R.id.trip_status_image_view;

/**
 * Created by a.thanjira on 3/26/2017.
 */

public class TripStatusDialogFragment extends DialogFragment {

    private TextView mTripIdTextView;
    private TextView mTripStatusTextView;
    private ImageView mImageView;

    public static TripStatusDialogFragment newInstance(int tripId, String tripStatus) {
        TripStatusDialogFragment frag = new TripStatusDialogFragment();
        Bundle args = new Bundle();
        args.putInt("trip_id", tripId);
        args.putString("trip_status", tripStatus);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_status, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTripIdTextView = (TextView) view.findViewById(R.id.trip_id_text_view);
        mTripStatusTextView = (TextView) view.findViewById(R.id.trip_status_text_view);
        mImageView = (ImageView) view.findViewById(trip_status_image_view);

        getDialog().setTitle("Trip Status Dialog");

        //set trip status image
        mImageView.setImageResource(R.mipmap.ic_launcher);

        // Fetch arguments from bundle and set title
        int trip_id = getArguments().getInt("trip_id");
        mTripIdTextView.setText(Integer.toString(trip_id));
        //mTripIdTextView.setText(trip_id);

        // Fetch arguments from bundle and set title
        String trip_status = getArguments().getString("trip_status");
        mTripStatusTextView.setText(trip_status);

    }

    @Override
    public void onResume() {

        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();

        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        // Call super onResume after sizing
        super.onResume();
    }
}
