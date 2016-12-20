package com.attribes.push2beat.adapter;

/**
 * Created by Muhammad Shahab on 12/7/16.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.attribes.push2beat.fragments.*;
import com.attribes.push2beat.mainnavigation.MainActivity;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
//        return PlaceholderFragment.newInstance(position + 1);
        switch (position) {
            case 0:
                Timer_GpsFragment timer_gpsFragment = new Timer_GpsFragment();
                return timer_gpsFragment;
            case 1:
                MusicFragment musicFragment = new MusicFragment();
                return musicFragment;
            case 2:
                MyStatsFragment myStatsFragment = new MyStatsFragment();
                return myStatsFragment;
            case 3:
                MyProfileFragment myProfileFragment = new MyProfileFragment();
                return myProfileFragment;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "GPS";
            case 1:
                return "MUSIC";
            case 2:
                return "Stats";
            case 3:
                return "Profile";
        }
        return null;
    }
}
