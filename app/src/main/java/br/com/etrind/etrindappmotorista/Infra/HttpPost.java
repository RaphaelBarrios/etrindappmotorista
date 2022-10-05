package br.com.etrind.etrindappmotorista.Infra;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@SuppressWarnings({"Convert2Lambda", "CommentedOutCode", "deprecation"})
public class HttpPost extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... par) {

        String url = par[0];
        String strJson = "";
        if(par.length > 1){
            strJson =  par[1];
        }
        String authToken =  "";
        if(par.length > 2){
             authToken =  par[2];
        }

        try {
            return downloadContent(url, strJson, authToken);
        } catch (IOException e) {
            e.printStackTrace();
            return "RequestError: " + e.getMessage();
        }
    }

    private String downloadContent(String strUrl, String strJson, String authToken) throws IOException {
        OutputStream os = null;
        InputStream is = null;

        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");

            if(!authToken.equals(""))
                conn.setRequestProperty("Authorization","Bearer " + authToken);

            conn.setDoOutput(true);
            conn.setDoInput(true);

            os = conn.getOutputStream();

            if(!strJson.equals("") ) {
                os.write(strJson.getBytes(StandardCharsets.UTF_8));
            }

            is = new BufferedInputStream(conn.getInputStream());

            return convertInputStreamToString(is);

        } finally {
            if (is != null) {
                is.close();
            }

            if(os != null){
                os.close();
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
