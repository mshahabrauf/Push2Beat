package com.attribes.push2beat.adapter;

/**
 * Created by Muhammad Shahab on 12/7/16.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.fragments.GpsFragment;
import com.attribes.push2beat.fragments.MusicFragment;
import com.attribes.push2beat.fragments.MyProfileFragment;
import com.attribes.push2beat.fragments.MyStatsFragment;
import com.attribes.push2beat.fragments.SelectFragment;

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
        switch (position)
        {
            case 0:

                if(Common.getInstance().getRunType() == 3)
                {
                    GpsFragment gpsFragment = new GpsFragment();
                    return gpsFragment;

                }
                else {
                    SelectFragment selectFragment = new SelectFragment();
                    return selectFragment;
                }

//
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


     //   return PlaceholderFragment.newInstance(position + 1);
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
                return "Music";
            case 2:
                return "Stats";
            case 3:
                return "Profile";
        }
        return null;
    }


}
