package com.attribes.push2beat.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

/**
 * Created by Maaz on 1/17/2017.
 */
public class ReverseGeoLocationTask extends AsyncTask<Double, Void, String>
        {
           private Context mContext;

            public ReverseGeoLocationTask(Context mContext) {
                this.mContext = mContext;
            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Double... params) {


                double latitude = params[0].doubleValue();
                double longitude = params[1].doubleValue();


                List<Address> addresses = null;
                String addressText = "";

                try {
                    Geocoder geocoder = new Geocoder(mContext);
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (Exception e) {
                    Log.e("crash", "geolocation:"  + e.getMessage());
                }

                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);

                    addressText = address.getAddressLine(0) + ", "
                            + address.getLocality() + ", "
                            + address.getAdminArea();

                } else {
                    addressText = "Not Found...";
                }

                return addressText;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }
