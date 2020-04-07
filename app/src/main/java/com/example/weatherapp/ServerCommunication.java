package com.example.weatherapp;

import android.os.AsyncTask;

import com.example.weatherapp.Interfaces.ResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerCommunication {
    public static void getCurCond(String id, ResponseListener listener){
        String[] params = {Misc.GET_CURRENT_CONDITIONS + id + "?language=ru-ru&details=false&apikey=" + Misc.KEY};
        new Execute(listener).execute(params);
    }

    public static void searchCities(String q, ResponseListener listener){
        String[] params = {Misc.SEARCH_CITIES + "?language=ru-ru&apikey=" + Misc.KEY + "&q=" + q};
        new Execute(listener).execute(params);
    }

    private static class Execute extends AsyncTask<String, Integer, JSONObject> {
        private ResponseListener mListener;

        public Execute(ResponseListener mListener){
            this.mListener = mListener;
        }

        @Override
        protected void onPreExecute() {super.onPreExecute();}

        protected JSONObject doInBackground(String... strs) {
            BufferedReader in = null;
            //HttpsURLConnection conn = null;
            HttpURLConnection conn = null;
            StringBuffer response = new StringBuffer();
            int responseCode = -1;
            try {
                URL path = new URL(strs[0]);
                //conn = (HttpsURLConnection) path.openConnection();
                conn = (HttpURLConnection) path.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestProperty("Accept","application/json");
                responseCode = conn.getResponseCode();

                switch (responseCode){
                    case 200:
                        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        break;
                    default:
                        in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                        break;
                }

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

            } catch (Exception ex){
                ex.printStackTrace();
                return null;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                        conn.disconnect();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
            JSONObject result = new JSONObject();
            try {
                result.put("Response", new JSONArray(response.toString()));
                result.put("ResponseCode", responseCode);
            } catch (JSONException e) {
                try {
                    result.put("error_description", response.toString());
                    result.put("ResponseCode", responseCode);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(JSONObject data) {
            if (this.mListener != null) {
                if (data != null) {
                    try {
                        if (data.getInt("ResponseCode") == 200)
                            mListener.onSuccess(data);
                        else
                            mListener.onError(data.getString("error_description"));
                    } catch (JSONException e) {
                        mListener.onSuccess(data);
                    }
                } else {
                    mListener.onError("Ошибка! Не удалось соединиться с сервером.");
                }
            }
        }
    }
}
