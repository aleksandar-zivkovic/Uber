package upm.softwaredesign.uber.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
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
import upm.softwaredesign.uber.ProfileActivity;
import upm.softwaredesign.uber.SignUpActivity;

import static android.R.attr.fitsSystemWindows;
import static android.R.attr.password;
import static upm.softwaredesign.uber.R.id.profile_name;

/**
 * Created by Aleksandar on 12/03/2017.
 */

public class HttpManager {

    private Context mContext;
    private HttpURLConnection httpCon;
    public static String token = "";

    private Integer mTripId;
    private String mCarId;
    private String mTripStatus;
    public HttpManager(Context context){

        mContext = context;
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public void sendRegistration(String email, String password, String firstname, String lastname, String phone) {
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

            JSONObject registerJson = new JSONObject();
            registerJson.put("email",email);
            registerJson.put("password",password);
            registerJson.put("first_name",firstname);
            registerJson.put("last_name",lastname);
            registerJson.put("phone_number",phone);

            RequestRegister requestRegister = new RequestRegister();
            requestRegister.execute(registerJson);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public class RequestRegister extends AsyncTask<Object, Void, Void>{

        private ProgressDialog pDialog;
        private boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
            pDialog.setTitle("Sign-up");
            pDialog.setMessage("Creating your account...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        public Void doInBackground(Object[] params) {

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

                switch (status){
                    case 201:
                        System.out.println("Successfully created !");
                        break;
                    case 403:
                        error = true;
                        errorMessage = "Error 403 - Email has already registered before";
                        break;
                    case 404:
                        error = true;
                        errorMessage = "Error 404";
                        break;
                    default:
                        error = true;
                        errorMessage = "Error "+status+" - Unknown Error";
                }
            }
            catch (Exception e) {
                e.getStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            pDialog.dismiss();
            if (error) {
                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
            } else {
                ((Activity)mContext).finish();
            }
        }
    }

    public void sendLogin(String email, String password){

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
            JSONObject loginJson = new JSONObject();
            loginJson.put("email",email);
            loginJson.put("password",password);

            new RequestLogin().execute(loginJson);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public class RequestLogin extends AsyncTask<Object, Void, Void>{
        private ProgressDialog pDialog;
        boolean error = false;
        String errorMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
            pDialog.setTitle("Connecting");
            pDialog.setMessage("Logging in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Object[] params) {
            JSONObject jsonData = (JSONObject) params[0];
            try {
                httpCon = (HttpURLConnection) ((new URL(Constants.Login_URL).openConnection()));
                httpCon.setDoOutput(true);
                httpCon.setRequestProperty("Content-Type", "application/json");
                httpCon.setRequestMethod("POST");
                httpCon.connect();

                OutputStream os = httpCon.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonData.toString());
                writer.close();
                os.close();

                //Read
                int loginStatus = httpCon.getResponseCode();
                if (loginStatus == 200) {
                    BufferedReader buf = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
                    token = buf.readLine();
                    System.out.println("The token is : " + token);
                    buf.close();
                    requestUserInfo();
                } else if (loginStatus == 400) {
                    error = true;
                    errorMessage = "Error 400 - Incorrect creditentials";
                } else {
                    error = true;
                    errorMessage = "Error !";
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            pDialog.dismiss();
            if (error) {
                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
            } else {
                LoginActivity.loginInstance.saveToken(token);
                mContext.startActivity(new Intent(mContext, MainActivity.class));
            }
        }
    }

    public void sendLogout(){
        new RequestLogout().execute();
    }
    public class RequestLogout extends AsyncTask<Void, Void, Void>{

        private ProgressDialog pDialog;
        private boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
            pDialog.setTitle("Logging out");
            pDialog.setMessage("Disconnecting...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        public Void doInBackground(Void... params) {
            try {
                httpCon = (HttpURLConnection) ((new URL (Constants.Logout_URL).openConnection()));
                //httpCon.setDoOutput(true);
                httpCon.setRequestProperty("Content-Type", "application/json");
                httpCon.setRequestProperty("Authorization", token);
                //httpCon.setRequestProperty("Accept", "application/json");
                httpCon.setRequestMethod("POST");
                httpCon.connect();

                /*Write
                OutputStream os = httpCon.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonData.toString());
                writer.close();
                os.close();*/

                //Read
                int status = httpCon.getResponseCode();

                switch (status){
                    case 200:
                        System.out.println("Logout Successful");
                        LoginActivity.loginInstance.saveToken("");
                        break;
                    case 401:
                        error = true;
                        errorMessage = "Error 401 - Unauthorized (user not logged in)";
                        break;
                    default:
                        error = true;
                        errorMessage = "Error "+status+" - Unknown Error";
                }
            }
            catch (Exception e) {
                e.getStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            pDialog.dismiss();
            if (error) {
                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
            }
            else {
                ((Activity)mContext).finish();
            }
        }
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
            String carID = "-1AAA";
            String tripStatus = "EMPTY";
            try {
                JSONObject tripIdJsonObject = new JSONObject(tripIdJsonString);
                tripID = (Integer) tripIdJsonObject.get("id");

                JSONObject carJsonObject = (JSONObject) tripIdJsonObject.get("car");
                carID = (String) carJsonObject.get("id");

                tripStatus = (String) tripIdJsonObject.get("status");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Toast.makeText(mContext, "Your trip ID is: " + tripID.toString() ,Toast.LENGTH_LONG).show();
            //Toast.makeText(mContext, "Your trip status is: " + tripStatus ,Toast.LENGTH_LONG).show();
            mTripId = tripID;
            mCarId = carID;
            mTripStatus = tripStatus;

            Intent intent = new Intent(mContext, MainActivity.class).setFlags(Constants.TRIP_STATUS_INTENT_FLAG);
            intent.putExtra(Constants.TRIP_ID, String.valueOf(mTripId));
            intent.putExtra(Constants.TRIP_STATUS, mTripStatus);
            intent.putExtra(Constants.CAR_ID, mCarId);
            mContext.startActivity(intent);


        }
    }

    public void requestUserInfo(){

        if(!isConnected()){
            new AlertDialog.Builder(mContext)
                    .setTitle("Error !")
                    .setMessage("No internet connection")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        try{
            new RequestUserInfo().execute();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public class RequestUserInfo extends AsyncTask<Object, Void, Void>{
        private ProgressDialog pDialog;
        boolean error = false;
        String errorMessage = "";
        JSONObject jsonObject = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
            pDialog.setTitle("Connecting");
            pDialog.setMessage("Logging in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Object[] params) {
            try {
                httpCon = (HttpURLConnection) ((new URL(Constants.UserInfo_URL).openConnection()));
                httpCon.setDoOutput(true);
                httpCon.setRequestProperty("Content-Type", "application/json");
                httpCon.setRequestProperty("Authorization", token);
                httpCon.setRequestMethod("GET");
                httpCon.connect();

                //Read
                int getUserInfoStatus = httpCon.getResponseCode();
                if (getUserInfoStatus == 200) {
                    BufferedReader buf = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = buf.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    buf.close();
                    jsonObject = new JSONObject(sb.toString());
                } else if (getUserInfoStatus == 401) {
                    error = true;
                    errorMessage = "Error 401 - Unauthorized (user was not logged in.)";
                } else {
                    error = true;
                    errorMessage = "Error !";
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            pDialog.dismiss();
            try {
                if (error) {
                    Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
                } else {

                        String firstName = jsonObject.getString("first_name");
                        String lastName = jsonObject.getString("last_name");
                        String email = jsonObject.getString("email");

                    if(ProfileActivity.profileInstance!=null) ProfileActivity.profileInstance.showUserInfo(firstName, lastName, email);
                    MainActivity.mainInstance.showAccountName(firstName, lastName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}