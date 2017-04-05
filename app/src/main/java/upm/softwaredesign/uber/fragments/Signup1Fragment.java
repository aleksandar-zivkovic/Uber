package upm.softwaredesign.uber.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import upm.softwaredesign.uber.R;
import upm.softwaredesign.uber.SignUpActivity;

import static android.R.attr.accountType;
import static android.R.attr.fragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Signup1Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Signup1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Signup1Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static String account,pw1,pw2;
    EditText email,pw,pwr;


    private OnFragmentInteractionListener mListener;

    public Signup1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Signup1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Signup1Fragment newInstance() {
        Signup1Fragment fragment = new Signup1Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup1, container, false);

        View nextButton = view.findViewById(R.id.signup_fragment_next_button);
        email = (EditText)view.findViewById(R.id.signup_fragment_email);
        pw = (EditText)view.findViewById(R.id.signup_fragment_password);
        pwr = (EditText)view.findViewById(R.id.signup_fragment_retype_password) ;
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = email.getText().toString();
                pw1=pw.getText().toString();
                pw2=pwr.getText().toString();

                if((pw1.equals("")&&pw2.equals(""))||(account.equals(""))){
                    Toast toast = Toast.makeText(getActivity(), "You must input these information",Toast.LENGTH_SHORT);
                    toast.show();
                }
                if(pw1.equals(pw2)&&(!account.equals(""))&&(!pw1.equals(""))){
                    Fragment fragment = new Signup2Fragment();
                    if (fragment != null) {

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.signup_frame, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
                else if((!pw1.equals(pw2))&&(!account.equals(""))){
                    Toast toast = Toast.makeText(getActivity(), "Your passwords are not the same. Please check them!",Toast.LENGTH_SHORT);
                    toast.show();
                }


            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
