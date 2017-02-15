package com.attribes.push2beat.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ScrollingView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.databinding.FragmentStatsBinding;
import com.attribes.push2beat.models.StatsData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import android.graphics.Canvas;


import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.graphics.Bitmap.Config.ARGB_8888;

/**
 * Created by android on 12/20/16.
 */

public class StatsFragment extends Fragment implements OnMapReadyCallback {

    private FragmentStatsBinding binding;

    private StatsData data;
    private GoogleMap googleMap;
    private Marker start;
    private Marker end;
    private Polyline line;

    public StatsFragment() {}

    @SuppressLint("ValidFragment")
    public StatsFragment(StatsData model) {
        data = model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_stats,container,false);
        View view = binding.getRoot();

       // initMapFragment();
        initMap();
        initUi();
        initListener();
        return view;

    }

    private void initMap() {
        if(binding.statsMapView !=null) {
            binding.statsMapView.onCreate(null);
            binding.statsMapView.onResume();
            binding.statsMapView.getMapAsync(this);
            binding.statsMapView.setClickable(false);
        }
    }

    private void initListener() {
        binding.layoutGudjob.goodJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              getActivity().onBackPressed();

            }
        });


        binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               File image = takeScreenshot();
                if(image!= null) {
                    startShareIntent(openScreenshot(image));
                }
            }
        });
    }

    private void startShareIntent(Uri uri) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM,uri);
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    private void initUi() {



        binding.layoutUserStats.statsCalburntTv.setText(""+data.getCalories());
        binding.layoutUserStats.statsDistancetravelledTv.setText(String.valueOf(data.getTraveledDistance())+"km");
        binding.layoutUserStats.statsAveragespeedTv.setText(String.valueOf(data.getAverageSpeed())+"km/h");
        binding.layoutUserStats.statsTopspeedTv.setText(String.valueOf(data.getTopSpeed()+"km/h"));
        binding.layoutUser.trackNameTv.setText(data.getTrackname());
        binding.statsProfileName.setText(DevicePreferences.getInstance().getuser().getFirst_name());
       // takeScreenshot();
        setProfileImage(DevicePreferences.getInstance().getuser().getProfile_image());


    }


    private void setProfileImage(String profileImage) {


        Picasso.with(getActivity()).load(profileImage).placeholder(R.drawable.placeholder).into(binding.statsProfileImage);
    }


    private File takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        File imageFile = null;
        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture

            GoogleMap.SnapshotReadyCallback map = new GoogleMap.SnapshotReadyCallback() {
                @Override
                public void onSnapshotReady(Bitmap bitmap) {

                }
            };


            Bitmap bitmap = Bitmap.createBitmap(
                    binding.scrollView.getChildAt(0).getWidth(),
                    binding.scrollView.getChildAt(0).getHeight(),
                    Bitmap.Config.ARGB_8888);

            binding.scrollView.layout(0, 0, binding.scrollView.getLayoutParams().width, binding.scrollView.getLayoutParams().height);


            Canvas c = new Canvas(bitmap);
            binding.scrollView.getChildAt(0).draw(c);

            imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

           // openScreenshot(imageFile);

        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
        return imageFile;
    }



    private Uri openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        return uri;
    }

//    private void initMapFragment() {
//        MapFragment fragment = new MapFragment(DevicePreferences.getInstance().getLocation(), new MapListener() {
//            @Override
//            public void onMapReady() {
  //              getMapFragment().showRoute(data.getPath());
//                getMapFragment().hideHeader();
//            }
//        });
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.replace(R.id.stats_map_view,fragment, Constants.STATS_MAP_TAG).commit();
//    }


    private MapFragment getMapFragment()
    {
        FragmentManager fm = getFragmentManager();
        MapFragment fragment = (MapFragment)fm.findFragmentByTag(Constants.STATS_MAP_TAG);
        return fragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        showRoute(data.getPath());

    }

    public void showRoute(List<LatLng> track) {

        if(track != null){
            for (int i = 0; i < track.size() - 1; i++) {
                LatLng src = track.get(i);
                LatLng dest = track.get(i + 1);

                line = googleMap.addPolyline(
                        new PolylineOptions().add(
                                new LatLng(src.latitude, src.longitude),
                                new LatLng(dest.latitude,dest.longitude)
                        ).width(4).color(Color.RED).geodesic(true)
                );
            }
            addTrackMarker(track);
        }
           moveMapCamera(track.get(0).latitude,track.get(0).longitude);
    }

    public void moveMapCamera(double latitude, double longitude) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 16.0f));
    }


    private void addTrackMarker(List<LatLng> track) {
        if(track.size() > 0) {
            start = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(track.get(0)));
            end = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(track.get(track.size() - 1)));
        }
    }

    public interface MapListener
    {
         void onMapReady();
    }
}
