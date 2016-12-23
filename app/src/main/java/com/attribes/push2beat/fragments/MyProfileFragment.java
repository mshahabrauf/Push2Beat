package com.attribes.push2beat.fragments;

import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.attribes.push2beat.R;
import com.attribes.push2beat.databinding.FragmentMyProfileBinding;
import com.attribes.push2beat.models.Response.MyProfileResponse;
import com.attribes.push2beat.network.DAL.GetProfileDAL;
import com.attribes.push2beat.network.interfaces.ProfileDataArrivalListner;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Maaz on 12/11/2016.
 */
public class MyProfileFragment extends android.support.v4.app.Fragment {

     FragmentMyProfileBinding mpBinding;

    public MyProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile,container,false);
        View view = mpBinding.getRoot();

        getMyProfileData();
        return view;
    }

    private void getMyProfileData() {
        GetProfileDAL.getProfileData("48", new ProfileDataArrivalListner() {
            @Override
            public void onDataRecieved(MyProfileResponse.Data data) {
                mpBinding.userProfileName.setText("" + data.getFirst_name());
                mpBinding.userProfile.userNameTv.setText("" + data.getFirst_name());
                mpBinding.userProfile.userMailTv.setText("" + data.getEmail());
                makeAddressFromLatLong(data.getLattitude(),data.getLongitude());
                setProfileImage(data.getProfile_image());
            }

            @Override
            public void onEmptyData(String msg) {

            }
        });
    }

    private void setProfileImage(String profileImage) {

        if(profileImage == null) {
            mpBinding.profileImage.setBackgroundResource(R.drawable.mz);
        }else{
            Picasso.with(getActivity()).load(profileImage).placeholder(R.drawable.mz).into(mpBinding.profileImage);
        }
    }

    private void makeAddressFromLatLong(String lattitude, String longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(lattitude),Double.parseDouble(longitude), 1);

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            String completeAddress = " "+address+", "+" "+country;
            mpBinding.userProfile.userLocTv.setText(""+completeAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
