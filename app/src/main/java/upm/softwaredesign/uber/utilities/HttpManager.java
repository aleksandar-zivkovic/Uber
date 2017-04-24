package upm.softwaredesign.uber.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import upm.softwaredesign.uber.LoginActivity;
import upm.softwaredesign.uber.MainActivity;
import upm.softwaredesign.uber.SignUpActivity;

/**
 * Created by Aleksandar on 12/03/2017.
 */

public class HttpManager {

    private Context mContext;
    private HttpURLConnection httpCon;
    public static String token = "";
    public static String RegisterStatusJson = "";
    public static String loginStatusJson = "";
    public static int LoginStatus = 0;
    private Integer mTripId;
    private String mTripStatus;
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

            String startLatitude = new Double(start.latitude).toString();
            String startLongitude = new Double(start.longitude).toString();
            String destinationLatitude = new Double(destination.latitude).toString();
            String destinationLongitude = new Double(destination.longitude).toString();

            JSONObject pickupJsonObject = new JSONObject();
            pickupJsonObject.put("lat", startLatitude);
            pickupJsonObject.put("lon", startLongitude);

            JSONObject dropoffJsonObject = new JSONObject();
            dropoffJsonObject.put("lat", destinationLatitude);
            dropoffJsonObject.put("lon", destinationLongitude);

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
            String email = SignUpActivity.account;
            String password = SignUpActivity.pw1;
            String fn = SignUpActivity.firstname;
            String ln = SignUpActivity.lastname;
            String pn = SignUpActivity.phonenumber;

            JSONObject registerJson = new JSONObject();
            registerJson.put("email",email);
            registerJson.put("password",password);
            registerJson.put("first_name",fn);
            registerJson.put("last_name",ln);
            registerJson.put("phone_number",pn);

            RequestRegister requestRegister = new RequestRegister();
            requestRegister.execute(registerJson);



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
            String em = LoginActivity.login_account;
            String ep = LoginActivity.login_password;

            JSONObject loginJson = new JSONObject();
            loginJson.put("email",em);
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
    public class RequestRegister extends AsyncTask {

       // private ProgressDialog pDialog;
        @Override
        public Object doInBackground(Object[] params) {


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

                if (status == 201) {
                    RegisterStatusJson = "Sign up Successfully!";
                } else if (status == 403) {
                    RegisterStatusJson = "Error 403 - Email has already registered before";
                } else if (status == 404) {
                    InputStream error = httpCon.getErrorStream();
                    RegisterStatusJson = "Error 404";
                } else {
                    RegisterStatusJson = "Error!";
                }
            }
            catch (Exception e) {
                e.getStackTrace();
            }
            return RegisterStatusJson;
        }
    }

    public class RequestLogin extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            JSONObject jsonData = (JSONObject) params[0];
            try {
                httpCon = (HttpURLConnection) ((new URL (Constants.Login_URL).openConnection()));
                httpCon.setDoOutput(true);
                httpCon.setRequestProperty("Content-Type", "application/json");
                //httpCon.setRequestProperty("Accept", "application/json");
                httpCon.setRequestMethod("POST");
                //httpCon.setRequestProperty("Authorization", token);
                httpCon.connect();

                OutputStream os = httpCon.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonData.toString());
                writer.close();
                os.close();

                //Read
                LoginStatus = httpCon.getResponseCode();
                if (LoginStatus == 200) {
                    BufferedReader buf =  new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
                    token = buf.readLine();
                    //TODO:Save the token



                    System.out.println("The token is : "+token);
                    buf.close();
                } else if (LoginStatus == 400) {
                    loginStatusJson = "Error 400 - Invalid credentials";
                    InputStream error = httpCon.getErrorStream();
                }  else {
                    loginStatusJson = "Error!";
                }
            }
            catch (Exception e) {
                e.getStackTrace();
            }
            return loginStatusJson;
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
                httpCon.setRequestProperty ("Authorization", token);
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
                } else if (status == 507) {
                    tripIdJsonString = "Response status 507: Insufficient storage -> No free cars available!";
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
            String tripStatus = "EMPTY";
            try {
                JSONObject tripIdJsonObject = new JSONObject(tripIdJsonString);
                tripID = (Integer) tripIdJsonObject.get("id");
                tripStatus = (String) tripIdJsonObject.get("status");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Toast.makeText(mContext, "Your trip ID is: " + tripID.toString() ,Toast.LENGTH_LONG).show();
            //Toast.makeText(mContext, "Your trip status is: " + tripStatus ,Toast.LENGTH_LONG).show();
            mTripId = tripID;
            mTripStatus = tripStatus;

            Intent intent = new Intent(mContext, MainActivity.class).setFlags(Constants.TRIP_STATUS_INTENT_FLAG);
            intent.putExtra(Constants.TRIP_ID, String.valueOf(mTripId));
            intent.putExtra(Constants.TRIP_STATUS, mTripStatus);
            mContext.startActivity(intent);


        }
    }


}