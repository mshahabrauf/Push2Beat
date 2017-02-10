package com.attribes.push2beat.mainnavigation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.adapter.SectionsPagerAdapter;
import com.attribes.push2beat.databinding.ActivityMainBinding;
import com.attribes.push2beat.models.CatchMeModel;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import java.io.IOException;

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
    private String gpsTitle = "Select Your Workout";
    private View tabView;
    private MediaPlayer mPlayer;
    private boolean isMusicIconVisible = false;

    //public boolean activityResult = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setupTabs();


        DevicePreferences.getInstance().init(getApplicationContext());
        createChatService();

        checkBundles();

        Common.getInstance().initializeQBInstance(getApplicationContext());
        startListeners();

        addTabsIcons();


    }

    private void startListeners() {
        binding.appbar.backButton.setOnClickListener(new BackButtonListener());
        binding.tabs.addOnTabSelectedListener(new CustomTabListener());
    }

    private void setupTabs() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);

        binding.tabs.setupWithViewPager(mViewPager);
        setAppBarTitle(mViewPager.getCurrentItem());
        binding.appbar.pickMusic.setVisibility(View.GONE);
    }


    private void createChatService() {
        Common.getInstance().initializeQBInstance(getApplicationContext());
        final QBChatService chatService = QBChatService.getInstance();


        chatService.login(DevicePreferences.getInstance().getQbUser(), new QBEntityCallback() {
            @Override
            public void onSuccess(Object o, Bundle bundle) {
                Common.getInstance().setChatService(chatService);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("mChat", "" + e);
            }
        });

    }

    private void showEndingDialog(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Catch me if you can");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Common.getInstance().setRunType(0);
                restartThisActivity();

            }
        });

        AlertDialog alertDialog  = builder.create();
        alertDialog.show();
    }




    private void addTabsIcons() {
        tabView = LayoutInflater.from(this).inflate(R.layout.tab_item_layout,null);
        binding.tabs.getTabAt(0).setCustomView(tabView);


        tabView = LayoutInflater.from(this).inflate(R.layout.tab_item_layout_music,null);
        binding.tabs.getTabAt(1).setCustomView(tabView);

        //binding.tabs.getTabAt(0).setIcon(R.drawable.ic_gps);
        tabView = LayoutInflater.from(this).inflate(R.layout.tab_item_layout_stats,null);
        binding.tabs.getTabAt(2).setCustomView(tabView);

        tabView = LayoutInflater.from(this).inflate(R.layout.tab_item_layout_profile,null);
        binding.tabs.getTabAt(3).setCustomView(tabView);

    }




    private void setAppBarTitle(int currentItem)
    {
     switch (currentItem) {
         case 0:  binding.appbar.text.setText(gpsTitle);
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
        gpsTitle = title;
        binding.appbar.text.setText(title);
    }


    public void showMusicIcon()
    {
        binding.appbar.pickMusic.setVisibility(View.VISIBLE);
        isMusicIconVisible = true;
        binding.appbar.pickMusic.setOnClickListener(new PickMusicListener());
    }

    public void hideMusicIcon()
    {
        binding.appbar.pickMusic.setVisibility(View.GONE);
        isMusicIconVisible = false;
    }





    @Override
    public void onBackPressed() {
        Common.getInstance().setCatchMeFromUser(false);
        Common.getInstance().setCatchMeFromNotification(false);
        Common.getInstance().setRunType(0);

        if(Common.getInstance().isOnSaveState())
        {
            showDialog();
        }
        else if(Common.getInstance().getFragmentCount() > 0) {
            Common.getInstance().resetFragmentCounter();
            restartThisActivity();
        }
        else {
            super.onBackPressed();
        }
    }

    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure you want to quit without saving your track?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Common.getInstance().setOnSaveState(false);
                onBackPressed();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog  = builder.create();
        alertDialog.show();
    }

    private void restartThisActivity()
    {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private class BackButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK  && requestCode==5) {
            Uri uri = data.getData();
            DevicePreferences.getInstance().saveMusicTrackPath(uri.toString());
            playMusic();
        }
    }


    public void stopMusic()
    {
        if(mPlayer!=null && mPlayer.isPlaying()){
            mPlayer.stop();
        }
    }


    public void pauseMusic()
    {
        if (mPlayer!=null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }



    public void playMusic() {

        if(mPlayer !=null) {
            if (mPlayer.isPlaying()) {
                mPlayer.release();
                mPlayer = null;
            }
        }

        mPlayer = new MediaPlayer();
        Uri myUri = Uri.parse(DevicePreferences.getInstance().getMusicTrackPath());
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(this, myUri);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        } catch (SecurityException e) {
            e.printStackTrace();

        } catch (IllegalStateException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    private class PickMusicListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i,5);


        }
    }


    /**
     * This method will check if bundles have some data for catch me if you can
     */
    private void checkBundles() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            boolean isCatcheMe = bundle.getBoolean("fromcatch");
            boolean isFromNotification = bundle.getBoolean("fromNotification");
            boolean isEndRun = bundle.getBoolean("fromendrun");

            if(isEndRun)
            {
                String message = bundle.getString("text");
                showEndingDialog(message);
                Common.getInstance().resetFragmentCounter();
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
                Common.getInstance().resetFragmentCounter();

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
                Common.getInstance().resetFragmentCounter();

            }



        }

    }



    private class CustomTabListener implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            setAppBarTitle(tab.getPosition());
            switch (tab.getPosition())
            {
                case 0:
                    if(isMusicIconVisible)
                    {
                        binding.appbar.pickMusic.setVisibility(View.VISIBLE);
                    }
                    break;
                case 1:
                case 2:
                case 3:
                    if(isMusicIconVisible)
                    {
                        binding.appbar.pickMusic.setVisibility(View.GONE);
                    }
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }
}
