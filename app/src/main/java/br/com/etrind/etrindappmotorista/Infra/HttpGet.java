package br.com.etrind.etrindappmotorista.Infra;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@SuppressWarnings({"Convert2Lambda", "CommentedOutCode", "deprecation"})
public class HttpGet extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... par) {
        String result;
        String url = par[0];
        String authToken =  "";

        if(par.length > 1){
            authToken =  par[1];
        }

        try{
            result = downloadContent(url, authToken);
        }catch (Exception ex){
            result = ex.getMessage();
        }

        return result;
    }

    private String downloadContent(String strUrl, String authToken) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");

            if(!authToken.equals(""))
                conn.setRequestProperty("Authorization","Bearer " + authToken);

            conn.setDoInput(true);
            conn.connect();
            //int response = conn.getResponseCode();
            //Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            return convertInputStreamToString(is);
        }catch (Exception ex){
            return "RequestError: " + ex.getMessage();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String convertInputStreamToString(InputStream stream) throws IOException {
        Reader reader;
        reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        StringBuilder result = new StringBuilder();
        String s;

        BufferedReader buffer = new BufferedReader(reader);
        while ((s = buffer.readLine()) != null) {
            result.append(s);
        }

        return result.toString();
    }
}
