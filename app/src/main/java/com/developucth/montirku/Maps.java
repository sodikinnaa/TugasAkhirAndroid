package com.developucth.montirku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.AsyncListUtil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class Maps extends AppCompatActivity {


    //Initialize variable
    Spinner spyType;
    Button btFind;
    SupportMapFragment supportMapFragment;
    GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat = 0, currentLong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // assign variable
        spyType = findViewById(R.id.sp_type);
        btFind = findViewById(R.id.bt_find);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_maps);

        //initialize aray of place type
        String[] placeTypeList = {"Bengkel"};
        //initialize array of place name
        String[] placeNameList = {"Bengkel"};

        //set adapter
        spyType.setAdapter(new ArrayAdapter<>(Maps.this, android.R.layout.simple_spinner_dropdown_item, placeNameList));

        //initializate fused location provider client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Check permtion
        if (ActivityCompat.checkSelfPermission(Maps.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permissin granted
            //call method

            getCurrentLocation();
        } else {
            //When permission denied
            //Request permission
            ActivityCompat.requestPermissions(Maps.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        btFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get select postition of spinner
                int i = spyType.getSelectedItemPosition();
                //Initial url
                String url = "https://maps.googleapis.com/maps/api/..." + "?location=" + currentLat + "," + currentLong + "&radius=5000" + "&type=" + placeTypeList[i] + "&sensor=true" + "&key=" + getResources().getString(R.string.google_maps_key);

                //Execute task
                // new placeTask().execute(url);
            }
        });

    }

    private void getCurrentLocation() {
        //initialize task location TAMBAHAN BUTKU
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //when succes
                if(location != null){
                    //when location is not equal to null
                    //Get current Latitude
                    currentLat = location.getLatitude();
                    //get current longitude
                    currentLong = location.getLongitude();
                    //sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            //when map ready
                            map = googleMap;
                            //zoom current location on map
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLat,currentLong), 10
                            ));
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //when permsison granted
                getCurrentLocation();
            }
        }
    }

    private class placeTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            //initializate date
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            //Excute parse
            new ParseTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        // Initializate url
        URL url = new URL(string);
        //initializate connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //Connection connec
        connection.connect();
        //Initializate input stream
        InputStream stram = connection.getInputStream();
        //Initalize buffer reader
        BufferedReader reader = new BufferedReader(new InputStreamReader(stram));
        //initializate String builder
        StringBuilder builder = new StringBuilder();
        //initalizate string variable
        String line = "";
        //use while loop
        while((line = reader.readLine()) != null){
            builder.append(line);
        }
        //get append data
        String data = builder.toString();
        //close reader
        reader.close();
        return data;
    }

    private class ParseTask extends AsyncTask<String,Integer,List<HashMap<String,String>>>{
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {

            //Create json parse
            JsonParsers jsonParsers = new JsonParsers();
            //Initial map list
            List<HashMap<String,String >> mapList = null;

            JSONObject object = null;
            //Initial json objekc
            try {
                object = new JSONObject(strings[0]);
                //parse json o
                mapList = jsonParsers.parseResule(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //return maplis

            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
//            //clear
//            map.clear();
//            //user for loop
//            for (int i=0; i<hashMaps.size(); i++){
//                //initialize map
//                HashMap<String ,String > hashMapsLit = hashMaps.get(i);
//                //get Lataitude
//                double lat = Double.parseDouble(hashMapsLit.get("lat"));
//                //get long atitude
//                double lng = Double.parseDouble(hashMapsLit.get("lat"));
//                //get name
//                String name = hashMapsLit.get("name");
//                //contact lat lang
//                LatLng latLng = new LatLng(lat,lng);
//                //initial marker option
//                MarkerOptions option = new MarkerOptions();
//                //set position
//                option.position(latLng);
//                //set title
//                option.title(name);
//                //add marker on map
//                map.addMarker(option);
//            }
        }
    }
}