package com.example.aleksandarx.foodfinder.network;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darko on 27.06.2016.
 */
public class HttpHelper {
    private static String localServer = "http://192.168.1.15:8081/";
    private static String herokuLive = "http://food-finder-app.herokuapp.com/";
    private static String server = herokuLive;
    public static String signUpHeroku(String username,String password)
    {
        String retStr = "";
        HttpURLConnection conn = null;
        try {

            conn = SetupConnection("http://food-finder-app.herokuapp.com/",10000,15000,"POST","application/json; charset=UTF-8","application/json");

            //JSONObject holder = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("Username", username);
            data.put("Password", password);


            /*Uri.Builder builder = new Uri.Builder()
                    //.appendQueryParameter("req", SEND_MY_PLACE)
                    //.appendQueryParameter("name", "ime")
                    .appendQueryParameter("payload", data.toString());
            String query = builder.build().getEncodedQuery();*/

            OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());

            wr.write(data.toString());
            wr.close();

            /*OutputStream os = conn.getOutputStream();
            os.write(data.toString().getBytes("UTF-8"));
            os.close();
            OutputStreamWriter wr= new OutputStreamWriter(os);
            wr.write(data.toString());*/
            /*BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data.toString());
            writer.flush();
            writer.close();
            os.close();*/
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                retStr = inputStreamToString(conn.getInputStream());

            }
            else{
                retStr = String.valueOf("Error "+responseCode+" Data sent:"+data.toString());
            }
            //conn.connect();



            conn.disconnect();
            return retStr;

        } catch (Exception e) {
            e.printStackTrace();
            //conn.disconnect();

            return "Exception:"+e.getMessage()+"\n URL:";
        }finally {
            if(conn != null)
                conn.disconnect();
        }

    }

    public static boolean loginHeroku(String username, String password)
    {
        HttpURLConnection conn = null;
        boolean ret = false;
        try {
            conn = SetupConnection("http://food-finder-app.herokuapp.com/signinMobile",10000,15000,"POST","application/json; charset=UTF-8","application/json");

            JSONObject data = new JSONObject();
            data.put("username", username);
            data.put("password", password);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data.toString());
            wr.close();


            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                ret = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(conn != null)
                conn.disconnect();
        }
        return ret;
    }

    public static List<MarkerOptions> findPlacesAroundYou(double lat, double lng)
    {
        List<MarkerOptions> positions = new ArrayList<>();

        HttpURLConnection conn = null;
        try {
            conn = SetupConnection("http://food-finder-app.herokuapp.com/placesAround",10000,15000,"POST","application/json; charset=UTF-8","application/json");

            JSONObject data = new JSONObject();
            data.put("lat", 43.541115);
            data.put("lng", 21.711991);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data.toString());
            wr.close();




            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                String jsonContent =  inputStreamToString(conn.getInputStream());
                JSONArray places = new JSONArray(jsonContent);
                for(int i = 0 ; i < places.length(); i++)
                {
                    JSONObject place = places.getJSONObject(i);
                    double latitude = place.getDouble("restaurant_latitude");
                    double longitude = place.getDouble("restaurant_longitude");
                    String name = place.getString("restaurant_name");

                    MarkerOptions options = new MarkerOptions();
                    options.position(new LatLng(latitude,longitude));
                    options.title(name);

                    positions.add(options);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        }finally {

            if(conn != null)
                conn.disconnect();
            return positions;
        }

    }


    public static String sendPicture(byte[] picture)
    {
        String attachmentName = "imageFile";
        String attachmentFileName = "bitmap.png";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";

        try{
            HttpURLConnection httpUrlConnection = null;
            URL url = new URL(server+"newFood");
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setDoOutput(true);

            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
            httpUrlConnection.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream request = new DataOutputStream(
                    httpUrlConnection.getOutputStream());

            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    attachmentName + "\";filename=\"" +
                    attachmentFileName + "\"" + crlf);
            request.writeBytes(crlf);

            request.write(picture);
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary +
                    twoHyphens + crlf);
            request.flush();
            request.close();

            //response
            InputStream responseStream = new
                    BufferedInputStream(httpUrlConnection.getInputStream());

            BufferedReader responseStreamReader =
                    new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            String response = stringBuilder.toString();
            responseStream.close();

            httpUrlConnection.disconnect();
            return "Successful";
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "Failed";
        }



    }

    private static HttpURLConnection SetupConnection(String url,int readTimeout,int connectionTimeout,String method,String contentType,String accept) throws IOException {

        URL serverUrl = new URL(url);


        HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
        conn.setReadTimeout(readTimeout);
        conn.setConnectTimeout(connectionTimeout);
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        //conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Content-Type", contentType);
        //conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Accept", accept);

        return conn;
    }

    public static String inputStreamToString(InputStream is)
    {
        String line = "";
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try{
            while((line = rd.readLine()) != null)
            {
                total.append(line);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return total.toString();
    }
}
