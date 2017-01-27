package com.attribes.push2beat.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.attribes.push2beat.Utils.OnSignUpSuccess;
import com.attribes.push2beat.Utils.ReverseGeoLocationTask;
import com.attribes.push2beat.databinding.FragmentMyProfileBinding;
import com.attribes.push2beat.mainnavigation.MainActivityStart;
import com.attribes.push2beat.models.BodyParams.UpdateProfileParams;
import com.attribes.push2beat.models.Response.MyProfileResponse;
import com.attribes.push2beat.network.DAL.GetProfileDAL;
import com.attribes.push2beat.network.DAL.UpdateProfileDAL;
import com.attribes.push2beat.network.interfaces.ProfileDataArrivalListner;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Maaz on 12/11/2016.
 */
public class MyProfileFragment extends android.support.v4.app.Fragment {

    private static int PROFILE_PIC_COUNT = 0 ;
    FragmentMyProfileBinding mpBinding;
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 4;
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
        mpBinding.userProfile.editName.setOnClickListener(new NameEditListner());
        mpBinding.logoutUserBtn.setOnClickListener(new LogOutListener());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case REQUEST_CAMERA:
                    if(resultCode == RESULT_OK) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");

                       String base =  convertBitmaptoBase64(photo);
                        updateProfilePic(base);

                        mpBinding.profileImage.setImageBitmap(photo);
                    }
                break;
            case SELECT_FILE:

                    Uri selectedImagefile = data.getData();
                String encodedProfileImageString = conversionInBase64Format(selectedImagefile);
                    updateProfilePic(encodedProfileImageString);
                    mpBinding.profileImage.setImageURI(selectedImagefile);

                break;
        }
    }

    private String convertBitmaptoBase64(Bitmap photo) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    private void updateProfilePic(String selectedImage) {

        profileParams.setUser_id(DevicePreferences.getInstance().getuser().getId());
        profileParams.setProfile_image(selectedImage);
        UpdateProfileDAL.updateProfile(profileParams, new OnSignUpSuccess() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure() {

            }
        });

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
        GetProfileDAL.getProfileData(DevicePreferences.getInstance().getuser().getId(), new ProfileDataArrivalListner() {
            @Override
            public void onDataRecieved(MyProfileResponse.Data data) {
                mpBinding.userProfileName.setText("" + data.getFirst_name());
                mpBinding.userProfile.userNameTv.setText("" + data.getFirst_name());
                mpBinding.userProfile.userMailTv.setText("" + data.getEmail());
                makeAddressFromLatLong(data.getLattitude(),data.getLongitude());
               if(data.getProfile_image() != null) {
                   if (data.getProfile_image().contains("http")) {
                       mpBinding.profileImage.setImageURI(Uri.parse(data.getProfile_image()));
                   } else {
                       mpBinding.profileImage.setImageBitmap(decodeImage(data.getProfile_image()));
                   }

                   setProfileImage(data.getProfile_image());
                   Common.getInstance().setProfile_image(data.getProfile_image());

               }
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


        try {

            String pick_location= new ReverseGeoLocationTask(getActivity()).execute(DevicePreferences.getInstance().getLocation().getLatitude(),DevicePreferences.getInstance().getLocation().getLongitude()).get();

            if(pick_location.contains("null"))
            {
              pick_location =  pick_location.replace("null","");
                mpBinding.userProfile.userLocTv.setText(""+pick_location);
            }
            else {
                mpBinding.userProfile.userLocTv.setText("" + pick_location);
            }
            } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
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
            mpBinding.userProfile.userNameEdittv.setOnFocusChangeListener(new EditListner());
        }
    }

    private class EditListners
    {


    }


    private class LogOutListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            DevicePreferences.getInstance().saverememberme(false);
            DevicePreferences.getInstance().removeUserObject();
            Intent intent = new Intent(getActivity(), MainActivityStart.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
    }


    public Bitmap decodeImage(String code)
    {
        byte[] decodedString = Base64.decode(code, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    private class EditListner implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean b) {
            if(!b && !mpBinding.userProfile.userNameEdittv.getText().toString().equals("")) {

                String editedName = mpBinding.userProfile.userNameEdittv.getText().toString();
                UpdateProfileParams params = new UpdateProfileParams();
                params.setUser_id(DevicePreferences.getInstance().getuser().getId());
                params.setFirst_name(editedName);
                params.setLast_name("");
                params.setProfile_image(Common.getInstance().getProfile_image());
                UpdateProfileDAL.updateProfile(params, new OnSignUpSuccess() {
                    @Override
                    public void onSuccess() {
                        getMyProfileData();
                    }

                    @Override
                    public void onFailure() {

                    }
                });


            }
            else if(!b){
                mpBinding.userProfile.userNameTv.setVisibility(View.VISIBLE);
                mpBinding.userProfile.userNameEdittv.setVisibility(View.GONE);
                mpBinding.userProfile.userNameEdittv.setOnFocusChangeListener(null);
            }

        }
    }
}
