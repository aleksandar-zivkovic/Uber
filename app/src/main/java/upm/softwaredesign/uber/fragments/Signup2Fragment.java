package upm.softwaredesign.uber.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import upm.softwaredesign.uber.LoginActivity;
import upm.softwaredesign.uber.R;
import upm.softwaredesign.uber.SignUpActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Signup2Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Signup2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Signup2Fragment extends Fragment {
    EditText etfn,etln,etphone;
    static String firstname,lastname,phonenumber;
    static String register_json;
    private OnFragmentInteractionListener mListener;

    public Signup2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Signup2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Signup2Fragment newInstance(String param1, String param2) {
        Signup2Fragment fragment = new Signup2Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup2, container, false);

        //added onclick listener for back button
        View backButton = view.findViewById(R.id.signup_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        //added onclick listener for sign up button
        View createButton = view.findViewById(R.id.signup_signup_button);
        etfn = (EditText)view.findViewById(R.id.signup_fragment_firstname);
        etln = (EditText)view.findViewById(R.id.signup_fragment_lastname);
        etphone = (EditText)view.findViewById(R.id.signup_fragment_phone);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstname = etfn.getText().toString();
                lastname = etln.getText().toString();
                phonenumber = etphone.getText().toString();
                register_json = "{"+"\"email\""+":\""+Signup1Fragment.account+"\","+
                        "\"password\""+":\""+Signup1Fragment.pw1+"\","+
                        "\"first_name\""+":\""+firstname+"\","+
                        "\"last_name\""+":\""+lastname+"\","+
                        "\"phone_number\""+":\""+phonenumber+"\","+
                        "}";
                //TODO: send this json to the server.

                Intent intent = new Intent();
                intent.setClass(getActivity(),LoginActivity.class);
                startActivity(intent);

                Toast toast = Toast.makeText(getActivity(), "Sign up Successfully!",Toast.LENGTH_SHORT);
                toast.show();
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
