package com.attribes.push2beat.mainnavigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.Utils.OnSignUpSuccess;
import com.attribes.push2beat.Utils.OnSocialSignInSuccess;
import com.attribes.push2beat.Utils.OnSocialSignUpSuccess;
import com.attribes.push2beat.fragments.LoaderFragment;
import com.attribes.push2beat.models.BodyParams.SignInParams;
import com.attribes.push2beat.models.BodyParams.UserLoginDetailParams;
import com.attribes.push2beat.models.Response.SocialSignUp.SocialSignUpResponse;
import com.attribes.push2beat.models.Response.UserSignUp.SocialSignInResponse;
import com.attribes.push2beat.models.UserProfile;
import com.attribes.push2beat.network.DAL.LoginDAL;
import com.attribes.push2beat.network.DAL.SocialSignIn;
import com.attribes.push2beat.network.DAL.SocialSignup;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class SignIn extends AppCompatActivity {
    ImageView signin;
    EditText username;
    EditText password;
    Boolean onsuccess = false;
    AVLoadingIndicatorView progress;
    QBChatService chatService;
    QBUser opponent;

    private ImageButton loginButton1;
    private CallbackManager callbackManager1;
    CheckBox remember;
    public SignInParams data1;
    private String facebook_id, f_name, m_name, l_name, gender, profile_image, full_name, email_id;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        DevicePreferences.getInstance().init(getApplicationContext());
        setContentView(R.layout.activity_sign_in);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Common.getInstance().initializeQBInstance(getApplicationContext());
        loginButton1 = (ImageButton) findViewById(R.id.login_button1);


        signin = (ImageView) findViewById(R.id.signinuser);
        username = (EditText) findViewById(R.id.usersignin);
        password = (EditText) findViewById(R.id.userpassword);
        progress = (AVLoadingIndicatorView) findViewById(R.id.progress_wheel);
        remember = (CheckBox) findViewById(R.id.checkBox);
        callbackManager1 = CallbackManager.Factory.create();
        //



        loginButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                LoginManager.getInstance().logInWithReadPermissions(SignIn.this, Arrays.asList("public_profile", "user_friends", "email"));
                facebook_id = f_name = m_name = l_name = gender = profile_image = full_name = email_id = "";

                LoginManager.getInstance().registerCallback(callbackManager1, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        Profile fbProfile = Profile.getCurrentProfile();
                        final UserProfile userProfile = new UserProfile();
                        if (fbProfile != null) {

                            userProfile.setSocial_token(loginResult.getAccessToken().getToken());
                            userProfile.setFirstname(fbProfile.getFirstName());
                            userProfile.setLastname(fbProfile.getLastName());
                            userProfile.setProfile_image(fbProfile.getProfilePictureUri(400, 400));
                            userProfile.setEmail("talhaghaffar1222@gmail.com");
                            socialSignup(userProfile);

//                    userProfile.setProfile_image(conversionInBase64Format(fbProfile.getProfilePictureUri(400, 400)));
                        }

                        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        try {
                                            String email = object.getString("email");

                                            //     userProfile.setEmail("talhaghaffar922@gmail.com");
                                            //     Bundle parameters = new Bundle();
//                                    parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                                            socialSignup(userProfile);

                                        } catch (JSONException e) {

                                            //  e.printStackTrace();
                                        }

                                    }

                                });
                        request.executeAsync();


                        goMainscreen();


                    }

                    @Override
                    public void onCancel() {


                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });


        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {


                } else {

                }

            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoader();

                UserLoginDetailParams detail = new UserLoginDetailParams();
                detail.setUser_email(username.getText().toString());
                detail.setDevice_type("1");
                detail.setDevice_token(FirebaseInstanceId.getInstance().getToken());
                detail.setPassword(password.getText().toString());


                LoginDAL.userLogin(detail, new OnSignUpSuccess() {
                    @Override
                    public void onSuccess() {
                        QbSignIn(username.getText().toString().trim(), password.getText().toString().trim());
                        Common.getInstance().setPassword(password.getText().toString());


                        DevicePreferences.getInstance().saverememberme(remember.isChecked());

                        DevicePreferences.getInstance().saveusers(Common.getInstance().getUser());
                    }

                    @Override
                    public void onFailure() {
                        removeLoader();
                        Toast.makeText(SignIn.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void startLoader() {
        LoaderFragment loaderFragment = new LoaderFragment("Wait...");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_main_start,loaderFragment, Constants.CATCH_LOADER_TAG).commit();
    }

    private void removeLoader()
    {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.CATCH_LOADER_TAG);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }


    public void QbSignIn(final String email, String password) {

        final QBUser qbUser = new QBUser();
        qbUser.setLogin(email);
        qbUser.setEmail(email);
        qbUser.setPassword(password);
        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle bundle) {
                qbUser.setId(user.getId());

                DevicePreferences.getInstance().saveQbuser(qbUser);
                removeLoader();


             //   Toast.makeText(getApplicationContext(), "QuickBlox SignIn Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignIn.this, SelectActivity.class));
                onsuccess = true;


            }

            @Override
            public void onError(QBResponseException e) {
                removeLoader();

            }
        });

    }


    private void socialSignup(final UserProfile userProfile) {
        userProfile.setLattitude("12");
        userProfile.setLongitude("23");

        SocialSignup.socialsignupnew(userProfile, getApplicationContext(), new OnSocialSignUpSuccess() {

            @Override
            public void onSuccess(SocialSignUpResponse socialSignUpResponse) {
                if (socialSignUpResponse.getCode() == 200) {
                    /*Todo move to next screen after saving the cache the profile*/

                } else if (socialSignUpResponse.getCode() == 202) {
                    QbSignIn(username.getText().toString().trim(), password.getText().toString().trim());

                    socialSignIn(userProfile);

                }


            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void socialSignIn(UserProfile userProfile) {
        userProfile.setDevice_type("1");
        userProfile.setDevice_token(FirebaseInstanceId.getInstance().getToken());
        SocialSignIn.socialsigninnew(userProfile, getApplicationContext(), new OnSocialSignInSuccess() {
            @Override
            public void onSuccess(SocialSignInResponse socialSignInResponse) {
                if (socialSignInResponse.getCode() == 200) {
                    QbSignIn(username.getText().toString().trim(), password.getText().toString().trim());

                }
            }

            @Override
            public void onFailure() {

            }
        });

    }










    private void goMainscreen(){
        Intent intent = new Intent(this,SelectActivity.class);
        startActivity(intent);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager1.onActivityResult(requestCode, resultCode, data);
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






    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SignIn Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }



    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

