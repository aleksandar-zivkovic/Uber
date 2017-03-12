package upm.softwaredesign.uber.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by Aleksandar on 12/03/2017.
 */

public class HttpManager {

    private String url = "http://test.com";
    public Context ctx;

    public HttpManager(String url, Context ctx){
        this.url = url;
        this.ctx = ctx;
    }

    public void sendPosition(LatLng start, LatLng destination){

        if(!isConnected()){
            new AlertDialog.Builder(ctx)
                    .setTitle("Error !")
                    .setMessage("No internet connection")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        //Create JSONObject
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("start_latitude", new Double(start.latitude).toString());
            jsonParam.put("start_longitude", new Double(start.longitude).toString());
            jsonParam.put("destination_latitude", new Double(destination.latitude).toString());
            jsonParam.put("destination_longitude", new Double(destination.longitude).toString());
            new GetData().execute(jsonParam);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


    private class GetData extends AsyncTask<JSONObject, Void, Boolean> {

        HttpURLConnection httpCon;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ctx, "Sending Locations", "Loading...");
        }

        @Override
        protected Boolean doInBackground(JSONObject... params) {

            try{
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                httpCon.setUseCaches(false);
                httpCon.setRequestProperty( "Content-Type", "application/json" );
                httpCon.setRequestProperty("Accept", "application/json");
                httpCon.setRequestMethod("POST");
                httpCon.connect();

                OutputStream os = httpCon.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(params[0].toString());
                osw.flush();
                osw.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }

            // modify this for the return from server (maybe not returning anything)
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if(!result){
                new AlertDialog.Builder(ctx)
                        .setTitle("Error !")
                        .setMessage("Could not send data to server")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return;
            }
        }
    }
}