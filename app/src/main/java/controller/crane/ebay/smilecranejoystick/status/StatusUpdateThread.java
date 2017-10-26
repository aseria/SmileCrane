package controller.crane.ebay.smilecranejoystick.status;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import controller.crane.ebay.smilecranejoystick.MainActivity;

/**
 * Created by kyunghwkim on 2017-10-25.
 */

public class StatusUpdateThread extends Thread {

    private MainActivity mainActivity;
    CRANE_STATUS currentStatus;

    public StatusUpdateThread(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.currentStatus = this.mainActivity.STATUS;

    }

    @NonNull
    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Override
    public void run() {
//        super.run();
        while(mainActivity.isRunning()) {
            try {

                Thread.sleep(100); //10fps
                String ip = "http://192.168.1.114";//http://192.168.61.140";
                String port = "5555";
                String url = ip + ":" + port + "/" + "get_status";

                URL requestUrl = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();
                urlConnection.connect(); //Connect
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String jsonString = readAll(reader);
//            Log.d("STATUS_THREAD", json);
                JSONObject json = new JSONObject(jsonString);
                int status = Integer.parseInt(json.getString("status"));
                CRANE_STATUS crane_status = CRANE_STATUS.values()[status];
                MainActivity.STATUS = crane_status;
                if(currentStatus != crane_status) {
                    Log.d("GET_STATUS", "STATUS is changed to " + crane_status);
                    currentStatus = crane_status;
                }
            } catch (Exception e) {
                Log.e("GET_STATUS", "EXCEPTION : " + e.getMessage());
            }
        }
    }
}
