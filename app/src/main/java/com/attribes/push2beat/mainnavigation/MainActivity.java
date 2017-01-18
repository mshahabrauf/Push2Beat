package com.attribes.push2beat.mainnavigation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.adapter.SectionsPagerAdapter;
import com.attribes.push2beat.databinding.ActivityMainBinding;
import com.attribes.push2beat.models.CatchMeModel;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ActivityMainBinding binding;
    public boolean activityResult = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            boolean isCatcheMe = bundle.getBoolean("fromcatch");
            boolean isFromNotification = bundle.getBoolean("fromNotification");
            boolean isEndRun = bundle.getBoolean("fromendrun");

            if(isEndRun)
            {
                String message = bundle.getString("text");
                showEndingDialog(message);
            }
            if(isCatcheMe)
            {
                Common.getInstance().setCatchMeFromUser(isCatcheMe);
                Common.getInstance().setRunType(3);

                CatchMeModel data = new CatchMeModel();
                data.setId(bundle.getString("id"));
                data.setEmail(bundle.getString("email"));
                data.setUsername(bundle.getString("username"));
                data.setLatitude(Double.parseDouble(bundle.getString("latitude")));
                data.setLongitude(Double.parseDouble(bundle.getString("longitude")));

                Common.getInstance().setOppData(data);

            }
            else if(isFromNotification)
            {

                Common.getInstance().setCatchMeFromNotification(isFromNotification);
                Common.getInstance().setRunType(3);

                CatchMeModel data = new CatchMeModel();
                data.setId(bundle.getString("id"));
                data.setEmail(bundle.getString("email"));
                data.setUsername(bundle.getString("username"));
                data.setLatitude(Double.parseDouble(bundle.getString("latitude")));
                data.setLongitude(Double.parseDouble(bundle.getString("longitude")));
                Common.getInstance().setOppData(data);

            }



        }


        Common.getInstance().initializeQBInstance(getApplicationContext());

        binding.appbar.backButton.setOnClickListener(new BackButtonListener());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);

        binding.tabs.setupWithViewPager(mViewPager);
        setAppBarTitle(mViewPager.getCurrentItem());
        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setAppBarTitle(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        addTabsIcons();


    }

    private void showEndingDialog(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Catch me if you can");
        builder.setMessage(message);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog  = builder.create();
        alertDialog.show();
    }


    private void addTabsIcons() {

        binding.tabs.getTabAt(0).setIcon(R.drawable.ic_gps);
        binding.tabs.getTabAt(1).setIcon(R.drawable.ic_music);
        binding.tabs.getTabAt(2).setIcon(R.drawable.ic_stats);
        binding.tabs.getTabAt(3).setIcon(R.drawable.ic_action_name);

    }

    private void setAppBarTitle(int currentItem)
    {
     switch (currentItem) {
         case 0:  binding.appbar.text.setText("GPS");
             break;
         case 1:  binding.appbar.text.setText("My Music");
             break;
         case 2:  binding.appbar.text.setText("My Stats");
             break;
         case 3:  binding.appbar.text.setText("My Profile");
             break;
     }
     }

    public void changeTitle(String title)
    {
        binding.appbar.text.setText(title);
    }

    @Override
    public void onBackPressed() {
        Common.getInstance().setCatchMeFromUser(false);
        Common.getInstance().setCatchMeFromNotification(false);
        super.onBackPressed();
    }

    private class BackButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK ) {
            Uri uri = data.getData();
            DevicePreferences.getInstance().saveMusicTrackPath(uri.toString());
            activityResult = true;
        }
        else {
            activityResult = false;
        }
    }

}
