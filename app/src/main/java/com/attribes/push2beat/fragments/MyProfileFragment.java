package com.attribes.push2beat.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.databinding.FragmentMyProfileBinding;
import com.attribes.push2beat.mainnavigation.MainActivityStart;
import com.attribes.push2beat.models.BodyParams.UpdateProfileParams;
import com.attribes.push2beat.models.Response.MyProfileResponse;
import com.attribes.push2beat.network.DAL.GetProfileDAL;
import com.attribes.push2beat.network.DAL.UpdateProfileDAL;
import com.attribes.push2beat.network.interfaces.ProfileDataArrivalListner;
import com.squareup.picasso.Picasso;

import java.io.*;
import java.util.List;
import java.util.Locale;

/**
 * Created by Maaz on 12/11/2016.
 */
public class MyProfileFragment extends android.support.v4.app.Fragment {

    private static int PROFILE_PIC_COUNT = 0 ;
    FragmentMyProfileBinding mpBinding;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    UpdateProfileParams profileParams = new UpdateProfileParams();

    public MyProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile,container,false);
        View view = mpBinding.getRoot();
        getMyProfileData();
        init();
        return view;
    }

    private void init() {
        mpBinding.profileImage.setOnClickListener(new ImageUploadListner());
        mpBinding.userProfile.userNameTv.setOnClickListener(new NameEditListner());
        mpBinding.logoutUserBtn.setOnClickListener(new LogOutListener());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 1:
                if(requestCode == REQUEST_CAMERA){
                    Uri selectedImage = data.getData();
                    updateProfilePic(selectedImage);
                    //mpBinding.profileImage.setImageURI(selectedImage);
                }

                break;
            case 2:
                if(requestCode == SELECT_FILE){
                    Uri selectedImage = data.getData();
                    updateProfilePic(selectedImage);
                    //mpBinding.profileImage.setImageURI(selectedImage);
                }
                break;
        }
    }

    private void updateProfilePic(Uri selectedImage) {
        String encodedProfileImageString = conversionInBase64Format(selectedImage);
        profileParams.setUser_id(DevicePreferences.getInstance().getusersocial().getId());
        profileParams.setProfile_image(encodedProfileImageString);
        UpdateProfileDAL.updateProfile(profileParams,getActivity());
        //TODO : Get the data of user from a centralized aspect
    }

    private String conversionInBase64Format(Uri selectedImage) {

        InputStream inputStream = null;//You can get an inputStream using any IO API
        try {
            inputStream = new FileInputStream(String.valueOf(selectedImage));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);

        return encodedString;
    }

    private void getMyProfileData() {
        GetProfileDAL.getProfileData(DevicePreferences.getInstance().getusersocial().getId(), new ProfileDataArrivalListner() {
            @Override
            public void onDataRecieved(MyProfileResponse.Data data) {
                mpBinding.userProfileName.setText("" + data.getFirst_name());
                mpBinding.userProfile.userNameTv.setText("" + data.getFirst_name());
                mpBinding.userProfile.userMailTv.setText("" + data.getEmail());
                makeAddressFromLatLong(data.getLattitude(),data.getLongitude());
                setProfileImage(data.getProfile_image());
                Common.getInstance().setProfile_image(data.getProfile_image());
            }

            @Override
            public void onEmptyData(String msg) {

            }
        });
    }

    private void setProfileImage(String profileImage) {

        if(profileImage == null) {
            mpBinding.profileImage.setBackgroundResource(R.drawable.placeholder);
        }else{
            Picasso.with(getActivity()).load(profileImage).placeholder(R.drawable.placeholder).into(mpBinding.profileImage);
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

    private void UpdateProfilePic() {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    PROFILE_PIC_COUNT = 1;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    PROFILE_PIC_COUNT = 1;
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    PROFILE_PIC_COUNT = 0;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private class ImageUploadListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            UpdateProfilePic();
        }
    }

    private class NameEditListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            mpBinding.userProfile.userNameTv.setVisibility(View.GONE);
            mpBinding.userProfile.userNameEdittv.setVisibility(View.VISIBLE);
            mpBinding.userProfile.userNameEdittv.setOnClickListener(new EditListner());
        }
    }

    private class EditListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
           String editedName = mpBinding.userProfile.userNameEdittv.getText().toString();
            UpdateProfileParams params = new UpdateProfileParams();
            params.setUser_id("48");
            params.setFirst_name(editedName);
            params.setProfile_image(Common.getInstance().getProfile_image());
            UpdateProfileDAL.updateProfile(params,getActivity());
        }
    }


    private class LogOutListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), MainActivityStart.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            DevicePreferences.getInstance().removeUserObject();
        }
    }
}
