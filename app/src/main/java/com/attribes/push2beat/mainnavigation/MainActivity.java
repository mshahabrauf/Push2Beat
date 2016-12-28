package com.attribes.push2beat.mainnavigation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.adapter.SectionsPagerAdapter;
import com.attribes.push2beat.databinding.ActivityMainBinding;

import java.util.Stack;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        Common.getInstance().setFragmentStack(new Stack<Fragment>());

        Common.getInstance().initializeQBInstance(getApplicationContext());

        binding.appbar.backButton.setOnClickListener(new BackButtonListener());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        binding.tabs.setupWithViewPager(mViewPager);


        addTabsIcons();


    }


    private void addTabsIcons() {

        binding.tabs.getTabAt(0).setIcon(R.drawable.ic_gps);
        //binding.tabs.getTabAt(0).setCustomView()
        binding.tabs.getTabAt(1).setIcon(R.drawable.ic_music);
        binding.tabs.getTabAt(2).setIcon(R.drawable.ic_stats);
        binding.tabs.getTabAt(3).setIcon(R.drawable.ic_action_name);

    }

    @Override
    public void onBackPressed() {
        Log.d("size",""+Common.getInstance().getFragmentStack().size());
        if (Common.getInstance().getFragmentStack().size() > 1) {
            binding.appbar.backButton.performClick();
        } else {
            super.onBackPressed();
        }
    }

    private class BackButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Common.getInstance().getFragmentStack().lastElement().onPause();
                ft.remove(Common.getInstance().getFragmentStack().pop());
                Common.getInstance().getFragmentStack().lastElement().onResume();
                ft.show(Common.getInstance().getFragmentStack().lastElement());
                ft.commit();
            }
            catch (IndexOutOfBoundsException ex)
            {
                Log.d("error",""+ ex);
            }

        }


    }
}
