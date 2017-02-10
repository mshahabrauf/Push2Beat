package com.attribes.push2beat.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.databinding.FragmentStatsBinding;
import com.attribes.push2beat.models.StatsData;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by android on 12/20/16.
 */

public class StatsFragment extends Fragment {

    private FragmentStatsBinding binding;

    private StatsData data;
    public StatsFragment() {}

    @SuppressLint("ValidFragment")
    public StatsFragment(StatsData model) {
        data = model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_stats,container,false);
        View view = binding.getRoot();
        initMapFragment();
        initUi();
        initListener();
        return view;

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
            View v1 = getActivity().getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

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

    private void initMapFragment() {
        MapFragment fragment = new MapFragment(DevicePreferences.getInstance().getLocation(), new MapListener() {
            @Override
            public void onMapReady() {
                getMapFragment().showRoute(data.getPath());
                getMapFragment().hideHeader();
            }
        });
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.stats_map_view,fragment, Constants.STATS_MAP_TAG).commit();
    }


    private MapFragment getMapFragment()
    {
        FragmentManager fm = getFragmentManager();
        MapFragment fragment = (MapFragment)fm.findFragmentByTag(Constants.STATS_MAP_TAG);
        return fragment;
    }


    public interface MapListener
    {
         void onMapReady();
    }
}
