package upm.softwaredesign.uber.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import upm.softwaredesign.uber.LoginActivity;
import upm.softwaredesign.uber.fragments.Signup1Fragment;
import upm.softwaredesign.uber.fragments.Signup2Fragment;

import static upm.softwaredesign.uber.R.id.start;

/**
 * Created by Aleksandar on 12/03/2017.
 */

public class HttpManager {

    private Context mContext;
    private HttpURLConnection httpCon;

    private Integer mTripId = -99;
    private String mTripStatus = "Nothing.";
    public static String RegisterToken = "";
    public static String requestlogin="";

    public HttpManager(Context context){

        mContext = context;
    }

    public void sendPosition(LatLng start, LatLng destination){

        if(!isConnected()){
            new AlertDialog.Builder(mContext)
                    .setTitle("Error !")
                    .setMessage("No internet connection")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        //Create JSONObject
        try{
            JSONObject cabRequestJsonObject = new JSONObject();
            cabRequestJsonObject.put("user_id", 1); // TODO: change this value when we get more users

            String startLatitude = new Double(start.latitude).toString();
            String startLongitude = new Double(start.longitude).toString();
            String destinationLatitude = new Double(destination.latitude).toString();
            String destinationLongitude = new Double(destination.longitude).toString();

            JSONObject pickupJsonObject = new JSONObject();
            pickupJsonObject.put("lat", startLatitude);
            pickupJsonObject.put("long", startLongitude);

            JSONObject dropoffJsonObject = new JSONObject();
            dropoffJsonObject.put("lat", destinationLatitude);
            dropoffJsonObject.put("long", destinationLongitude);

            cabRequestJsonObject.put("pickup", pickupJsonObject);
            cabRequestJsonObject.put("dropoff", dropoffJsonObject);

            new RequestCab().execute(cabRequestJsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public void sendRegisteration(){

        if(!isConnected()){
            new AlertDialog.Builder(mContext)
                    .setTitle("Error !")
                    .setMessage("No internet connection")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        //Create JSONObject
        try{
            String email = Signup1Fragment.account;
            String password = Signup1Fragment.pw1;
            String fn = Signup2Fragment.firstname;
            String ln = Signup2Fragment.lastname;
            String pn = Signup2Fragment.phonenumber;

            JSONObject registerJson = new JSONObject();
            registerJson.put("email",email);
            registerJson.put("password",password);
            registerJson.put("first_name",fn);
            registerJson.put("last_name",ln);
            registerJson.put("phone_number",pn);
            new RequestRegisterToken().execute(registerJson);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public void sendLogin(){

        if(!isConnected()){
            new AlertDialog.Builder(mContext)
                    .setTitle("Error !")
                    .setMessage("No internet connection")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        //Create JSONObject
        try{
            String em= LoginActivity.login_account;
            String ep = LoginActivity.login_password;

            JSONObject loginJson = new JSONObject();
            loginJson.put("username",em);
            loginJson.put("password",ep);
            new RequestLogin().execute(loginJson);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private class RequestRegisterToken extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {


            JSONObject jsonData = (JSONObject) params[0];

            try {
                httpCon = (HttpURLConnection) ((new URL (Constants.Register_URL).openConnection()));
                httpCon.setDoOutput(true);
                httpCon.setRequestProperty("Content-Type", "application/json");
                httpCon.setRequestProperty("Accept", "application/json");
                httpCon.setRequestMethod("POST");
                httpCon.connect();

                //Write
                OutputStream os = httpCon.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonData.toString());
                writer.close();
                os.close();

                //Read
                int status = httpCon.getResponseCode();
                if (status == 200) {
                    Toast toast = Toast.makeText(mContext, "Sign up Successfully!",Toast.LENGTH_SHORT);
                    toast.show();
                    InputStream is = httpCon.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    br.close();
                    RegisterToken = sb.toString();


                } else if (status == 400) {
                    RegisterToken = "Error 400 - Email has already registered before";
                } else if (status == 404) {
                    InputStream error = httpCon.getErrorStream();
                    RegisterToken = "Error 404";
                } else {
                    RegisterToken = "Error!";
                }
            }
            catch (Exception e) {
                e.getStackTrace();
            }

            return RegisterToken;
        }
    }

    private class RequestLogin extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            JSONObject jsonData = (JSONObject) params[0];
            try {
                httpCon = (HttpURLConnection) ((new URL (Constants.Login_URL).openConnection()));
                httpCon.setDoOutput(true);
                httpCon.setRequestProperty("Content-Type", "application/json");
                httpCon.setRequestProperty("Accept", "application/json");
                httpCon.setRequestMethod("POST");
                httpCon.connect();

                //Write
                OutputStream os = httpCon.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonData.toString());
                writer.close();
                os.close();

                //Read
                int status = httpCon.getResponseCode();
                if (status == 200) {
                    InputStream is = httpCon.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    br.close();
                    requestlogin = sb.toString();

                } else if (status == 400) {
                    requestlogin = "Error 400";
                } else if (status == 404) {
                    InputStream error = httpCon.getErrorStream();
                    requestlogin = "Error 404";
                } else {
                    requestlogin = "Error!";
                }
            }
            catch (Exception e) {
                e.getStackTrace();
            }

            return requestlogin;
        }
    }


    private class RequestCab extends AsyncTask {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
            pDialog.setTitle("Requesting a cab");
            pDialog.setMessage("Requesting a cab...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            String tripIdJsonString = "";
            JSONObject jsonData = (JSONObject) params[0];

            try {
                httpCon = (HttpURLConnection) ((new URL (Constants.REQUEST_TRIP_URL).openConnection()));
                httpCon.setDoOutput(true);
                httpCon.setRequestProperty("Content-Type", "application/json");
                httpCon.setRequestProperty("Accept", "application/json");
                httpCon.setRequestMethod("POST");
                httpCon.connect();

                //Write
                OutputStream os = httpCon.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonData.toString());
                writer.close();
                os.close();

                //Read
                int status = httpCon.getResponseCode();
                if (status == 200) {
                    InputStream is = httpCon.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    br.close();
                    tripIdJsonString = sb.toString();

                } else if (status == 400) {
                    tripIdJsonString = "Error 400 while requesting a cab";
                } else if (status == 404) {
                    InputStream error = httpCon.getErrorStream();
                    tripIdJsonString = "Error 404 while requesting a cab";
                } else {
                    tripIdJsonString = "Error - Something went wrong while requesting a cab";
                }
            }
            catch (Exception e) {
                e.getStackTrace();
            }

            return tripIdJsonString;
        }

        @Override
        protected void onPostExecute(Object o) {
            pDialog.dismiss();

            String tripIdJsonString = (String) o;
            if (tripIdJsonString.contains("Error")) {
                Toast.makeText(mContext, tripIdJsonString ,Toast.LENGTH_LONG).show();
            }

            Integer tripID = -1;
            try {
                JSONObject tripIdJsonObject = new JSONObject(tripIdJsonString);
                tripID = (Integer) tripIdJsonObject.get("trip_id");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(mContext, "Your trip ID is: " + tripID.toString() ,Toast.LENGTH_LONG).show();
            mTripId = tripID;

            checkTripStatus(tripID);
        }
    }

    public void checkTripStatus(Integer tripID) {
        new CheckTripStatus().execute(tripID);
    }

    private class CheckTripStatus extends AsyncTask {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
            pDialog.setTitle("Trip status");
            pDialog.setMessage("Checking trip status");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            String tripStatusJson = "";
            Integer tripID = (Integer) params[0];
            String tripStatusUrl = Constants.TRIP_STATUS_URL + tripID.toString();

            try {
                httpCon = (HttpURLConnection) ((new URL(tripStatusUrl).openConnection()));
                httpCon.setRequestMethod("GET");
                httpCon.connect();
                //Read
                int status = httpCon.getResponseCode();
                if (status == 200) {
                    InputStream is = httpCon.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    br.close();
                    tripStatusJson = sb.toString();

                } else if (status == 400) {
                    tripStatusJson = "Error 400 while requesting trip status";
                } else if (status == 404) {
                    InputStream error = httpCon.getErrorStream();
                    tripStatusJson = "Error 404 while requesting trip status";
                } else {
                    tripStatusJson = "Error - Something went wrong while requesting trip status";
                }
            }
            catch (Exception e) {
                e.getStackTrace();
            }

            return tripStatusJson;
        }

        @Override
        protected void onPostExecute(Object o) {
            pDialog.dismiss();
            String tripStatusJson = (String) o;
            if (tripStatusJson.contains("Error")) {
                Toast.makeText(mContext, tripStatusJson ,Toast.LENGTH_LONG).show();
            }

            String tripStatus = "";
            try {
                JSONObject tripStatusJsonObject = new JSONObject(tripStatusJson);
                tripStatus = (String) tripStatusJsonObject.get("status");
            } catch (Exception e) {
                e.printStackTrace();
            }

            new Handler().postDelayed(new Runnable() {
                public void run() {

                }
            }, 5000);

            Toast.makeText(mContext, "Your trip status is: " + tripStatus ,Toast.LENGTH_LONG).show();


        }
    }
}