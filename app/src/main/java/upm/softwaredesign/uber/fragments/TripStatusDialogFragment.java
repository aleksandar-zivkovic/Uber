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

    public static TripStatusDialogFragment newInstance(String tripID, String tripStatus) {
        TripStatusDialogFragment frag = new TripStatusDialogFragment();
        Bundle args = new Bundle();
        args.putString("tripID", tripID);
        args.putString("tripStatus", tripStatus);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.setCancelable(false);
        return inflater.inflate(R.layout.fragment_trip_status, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // getDialog().setTitle("Trip Status Dialog");

        mImageView = (ImageView) view.findViewById(trip_status_image_view);
        mImageView.setImageResource(R.mipmap.ic_launcher);

        mTripIdTextView = (TextView) view.findViewById(R.id.trip_id_text_view);
        mTripStatusTextView = (TextView) view.findViewById(R.id.trip_status_text_view);

        String tripID = getArguments().getString("tripID");
        mTripIdTextView.setText(tripID);

        String tripStatus = getArguments().getString("tripStatus");
        mTripStatusTextView.setText(tripStatus);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getView().post(new Runnable() {

            @Override
            public void run() {

                Window dialogWindow = getDialog().getWindow();

                // Make the dialog possible to be outside touch
                dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
                dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getView().invalidate();
            }
        });
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
        window.setLayout((int) (size.x * 0.8), (int) (size.y * 0.15));
        window.setGravity(Gravity.LEFT|Gravity.BOTTOM);


        // Call super onResume after sizing
        super.onResume();
    }

    public void updateTripStatus(String tripStatus) {
        mTripStatusTextView.setText(tripStatus);
    }
}
