package com.attribes.push2beat.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.Utils.OnSignUpSuccess;
import com.attribes.push2beat.Utils.ReverseGeoLocationTask;
import com.attribes.push2beat.databinding.FragmentMyProfileBinding;
import com.attribes.push2beat.mainnavigation.MainActivity;
import com.attribes.push2beat.mainnavigation.MainActivityStart;
import com.attribes.push2beat.models.BodyParams.UpdateProfileParams;
import com.attribes.push2beat.models.Response.MyProfileResponse;
import com.attribes.push2beat.network.DAL.GetProfileDAL;
import com.attribes.push2beat.network.DAL.UpdateProfileDAL;
import com.attribes.push2beat.network.interfaces.ProfileDataArrivalListner;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
   private String username;

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
        mpBinding.userProfileName.setText(DevicePreferences.getInstance().getuser().getFirst_name());
        setProfileImage(DevicePreferences.getInstance().getuser().getProfile_image());
        mpBinding.userProfile.userNameTv.setText(DevicePreferences.getInstance().getuser().getFirst_name());
        mpBinding.userProfile.userMailTv.setText(DevicePreferences.getInstance().getuser().getEmail());


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
                        Uri uri = getImageUri(getContext(),photo);
                     //  String base =  convertBitmaptoBase64(photo);
                        updateProfilePic(uri);

                        mpBinding.profileImage.setImageURI(uri);
                    }
                break;
            case SELECT_FILE:
                if(resultCode == RESULT_OK) {
                    Uri selectedImagefile = data.getData();
                    updateProfilePic(selectedImagefile);
                    mpBinding.profileImage.setImageURI(selectedImagefile);
                }
                break;
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void updateProfilePic(Uri selectedImage) {

        profileParams.setUser_id(DevicePreferences.getInstance().getuser().getId());
        profileParams.setFirst_name(mpBinding.userProfile.userNameTv.getText().toString());
        profileParams.setLast_name("");
     //   profileParams.setProfile_image(selectedImage);


       File file = new File(getRealPathFromURI(getContext(),selectedImage));
        //File file = FileUtils.getFile(getContext(),selectedImage);


        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),file
                );


        MultipartBody.Part body =
                MultipartBody.Part.createFormData("e_pic1", file.getName(), requestFile);




        UpdateProfileDAL.updateProfile(profileParams,body, new OnSignUpSuccess() {
            @Override
            public void onSuccess() {

                Toast.makeText(getContext(), "Profile updated successfully ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "update failed ", Toast.LENGTH_SHORT).show();
            }
        });

    }



    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



    private void getMyProfileData() {
        GetProfileDAL.getProfileData(DevicePreferences.getInstance().getuser().getId(), new ProfileDataArrivalListner() {
            @Override
            public void onDataRecieved(MyProfileResponse.Data data) {
                username = "" + data.getFirst_name();
                mpBinding.userProfileName.setText("" + data.getFirst_name());
                mpBinding.userProfile.userNameTv.setText("" + data.getFirst_name());
                mpBinding.userProfile.userMailTv.setText("" + data.getEmail());
                makeAddressFromLatLong(data.getLattitude(),data.getLongitude());
               if(data.getProfile_image() != null) {

                   setProfileImage(data.getProfile_image());

//                   mpBinding.profileImage.setImageURI(Uri.parse(data.getProfile_image()));
                   Common.getInstance().setProfile_image(data.getProfile_image());

               }
               }

            @Override
            public void onEmptyData(String msg) {

            }
        });
    }

    private void setProfileImage(String profileImage) {
            if(profileImage == null)
            {

            }
        else {

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
        catch (NullPointerException e)
        {
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
            ((MainActivity)getActivity()).stopMusic();
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


//                File file = new File(selectedImage.getPath());
//                //File file = FileUtils.getFile(getContext(),selectedImage);
//
//
//                RequestBody requestFile =
//                        RequestBody.create(
//                                MediaType.parse(getContext().getContentResolver().getType(selectedImage)),
//                                file
//                        );
//
//
//                MultipartBody.Part body =  MultipartBody.Part.createFormData("e_pic1", file.getName(), requestFile);
//
//                params.setProfile_image(Common.getInstance().getProfile_image());
//                UpdateProfileDAL.updateProfile(params, body, new OnSignUpSuccess() {
//                    @Override
//                    public void onSuccess() {
//                        getMyProfileData();
//                    }
//
//                    @Override
//                    public void onFailure() {
//
//                    }
//                });


            }
            else if(!b){
                mpBinding.userProfile.userNameTv.setVisibility(View.VISIBLE);
                mpBinding.userProfile.userNameEdittv.setVisibility(View.GONE);
                mpBinding.userProfile.userNameEdittv.setOnFocusChangeListener(null);
            }

        }
    }
}
