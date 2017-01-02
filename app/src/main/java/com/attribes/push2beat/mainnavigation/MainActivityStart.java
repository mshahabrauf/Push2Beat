package com.attribes.push2beat.mainnavigation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageButton;

import android.widget.TextView;
import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import org.w3c.dom.Text;

import static com.google.android.gms.location.LocationRequest.create;

public class MainActivityStart extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private ImageButton signinbtn;
    private ImageButton signupbtn;
    private GoogleApiClient apiClient;
    private LocationRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_start);
        initGoogleClient();
        initColorText();

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

    private void initColorText() {

        TextView pTob = (TextView)findViewById(R.id.p2b_tv);

        SpannableString text = new SpannableString("Push2Beat is the latest Hit (High Intensity interval training) app on the market for cycling. The ultimate experience in Music vs Excercise.");   // this is the text we'll be operating on
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary_pink)), 0, 9, 0);
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary_pink)), 121, 140, 1);
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
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.MY_PERMISSIONS_REQUEST_READ_LOCATION);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, request, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Common.getInstance().setLocation(location);
            }
        });
        Location location = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        Common.getInstance().setLocation(location);

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
        super.onResume();
        apiClient.connect();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_READ_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        Constants.MY_PERMISSIONS_REQUEST_READ_LOCATION);
            }
        }
    }

}
