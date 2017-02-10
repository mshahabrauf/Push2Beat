package com.attribes.push2beat.mainnavigation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static com.google.android.gms.location.LocationRequest.create;

public class MainActivityStart extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private ImageButton signinbtn;
    private ImageButton signupbtn;
    private GoogleApiClient apiClient;
    private LocationRequest request;
    private boolean Isremember;
   private LocationManager lm;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_start);
        init();
        popupLocationPermission();

        initGoogleClient();
        initColorText();

        DevicePreferences.getInstance().init(getApplicationContext());
        Isremember= DevicePreferences.getInstance().isRemember();
        if(Isremember){
            Intent intent = new Intent(MainActivityStart.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        signinbtn=(ImageButton)findViewById(R.id.signin);

        signupbtn=(ImageButton)findViewById(R.id.signup);


        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        Intent intent = new Intent(MainActivityStart.this,SignIn.class);
        startActivity(intent);

            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityStart.this,SignUp.class);
                startActivity(intent);



            }
        });
    }

    private void init() {
        lm = (LocationManager)getApplicationContext().getSystemService(getApplicationContext().LOCATION_SERVICE);

    }

    private void checkLocationEnabled() {
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivityStart.this);
            dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });

            dialog.show();
        }
    }

    private void initColorText() {

        TextView pTob = (TextView)findViewById(R.id.p2b_tv);

        SpannableString text = new SpannableString("Push2Beat is the latest HIIT (High Intensity interval training) app on the market for cycling. The ultimate experience in Music vs Excercise.");   // this is the text we'll be operating on
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary_pink)), 0, 9, 0);
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary_pink)), 122, 141, 1);
        pTob.setText(text, TextView.BufferType.SPANNABLE);
    }

    private void initGoogleClient() {
        if(apiClient == null) {
            apiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        apiClient.connect();
        createLocationRequest();
    }


    private void createLocationRequest() {
        request = create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5 * 1000); //Request after every 5 second
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        else {
            LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, request, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    DevicePreferences.getInstance().saveLocation(location);
                }
            });
            Location location = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            DevicePreferences.getInstance().saveLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    public void onStart() {
        super.onStart();
        apiClient.connect();
    }

    @Override
    public void onResume() {

        checkLocationEnabled();
        apiClient.connect();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        apiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        apiClient.disconnect();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case Constants.MY_PERMISSIONS_REQUEST_READ_LOCATION:

                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // Permission is granted
                    } else {
                        showPermissionDialog();
                    }

                    break;



        }
    }

    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sorry");
        builder.setMessage("This application using location Api without this you can't use many feature of this app " +
                "\n Do you want to access those features? ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                popupLocationPermission();
            }
        });

        AlertDialog alertDialog  = builder.create();
        alertDialog.show();
    }



    public void popupLocationPermission()
    {
        ActivityCompat.requestPermissions(MainActivityStart.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                Constants.MY_PERMISSIONS_REQUEST_READ_LOCATION);
    }
}
