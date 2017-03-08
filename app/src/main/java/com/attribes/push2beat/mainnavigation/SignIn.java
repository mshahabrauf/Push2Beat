package com.attribes.push2beat.mainnavigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Alerts;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.Utils.OnSocialSignInSuccess;
import com.attribes.push2beat.Utils.OnSocialSignUpSuccess;
import com.attribes.push2beat.fragments.LoaderFragment;
import com.attribes.push2beat.interfaces.MyCallBacks;
import com.attribes.push2beat.models.BodyParams.SignInParams;
import com.attribes.push2beat.models.BodyParams.UserLoginDetailParams;
import com.attribes.push2beat.models.Response.SocialSignUp.SocialSignUpResponse;
import com.attribes.push2beat.models.Response.UserSignUp.SigninResponse;
import com.attribes.push2beat.models.UserProfile;
import com.attribes.push2beat.network.DAL.LoginDAL;
import com.attribes.push2beat.network.DAL.SocialSignIn;
import com.attribes.push2beat.network.DAL.SocialSignup;
import com.attribes.push2beat.services.LocationService;
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
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class SignIn extends AppCompatActivity {
    ImageView signin;
    EditText username;
    EditText password;

    AVLoadingIndicatorView progress;

    private UserProfile userProfile;
    private ImageButton fbButton;
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

        Common.getInstance().initializeQBInstance(getApplicationContext());
        fbButton = (ImageButton) findViewById(R.id.login_button1);


        signin = (ImageView) findViewById(R.id.signinuser);
        username = (EditText) findViewById(R.id.usersignin);
        password = (EditText) findViewById(R.id.userpassword);
        progress = (AVLoadingIndicatorView) findViewById(R.id.progress_wheel);
        remember = (CheckBox) findViewById(R.id.checkBox);
        callbackManager1 = CallbackManager.Factory.create();
        //





        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startLoader();

                LoginManager.getInstance().logInWithReadPermissions(SignIn.this, Arrays.asList("email"));

                LoginManager.getInstance().registerCallback(callbackManager1, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        Profile fbProfile = Profile.getCurrentProfile();
                            userProfile = new UserProfile();
                        if (fbProfile != null)
                        {

                            userProfile.setSocial_token(loginResult.getAccessToken().getToken());
                            userProfile.setFirstname(fbProfile.getFirstName());
                            userProfile.setLastname(fbProfile.getLastName());
                            userProfile.setPassword("12345678");
                            userProfile.setProfile_image(fbProfile.getProfilePictureUri(400, 400));

                        }

                        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        try {

                                            userProfile.setEmail(object.getString("email"));
                                            socialSignup(userProfile);



                                        } catch (JSONException e) {

                                            //  e.printStackTrace();
                                        }

                                    }

                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "email");
                        request.setParameters(parameters);
                        request.executeAsync();


                       // goMainscreen();


                    }

                    @Override
                    public void onCancel() {


                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(getApplicationContext(),""+ error, Toast.LENGTH_SHORT).show();


                    }
                }
                );


            }
        });





        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isValidate(username.getText().toString().trim(),password.getText().toString().trim()))
                {
                    signIn();
                }





            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private boolean isValidate(String username,String password)
    {
        if(username.equals("")||password.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Kindly, complete this form", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (Common.getInstance().emailValidator(username.toString()) == false)
        {
            Toast.makeText(getApplicationContext(), "This Email Address is not valid", Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;


    }



    public void signIn()
    {
        startLoader();

        UserLoginDetailParams detail = new UserLoginDetailParams();
        detail.setUser_email(username.getText().toString());
        detail.setDevice_type("1");
        detail.setDevice_token(FirebaseInstanceId.getInstance().getToken());
        detail.setPassword(password.getText().toString());


        LoginDAL.userLogin(detail, new MyCallBacks<SigninResponse>() {
            @Override
            public void onSuccess(SigninResponse data)
            {
                QbSignIn(username.getText().toString().trim(), password.getText().toString().trim());
                Common.getInstance().setPassword(password.getText().toString());
                DevicePreferences.getInstance().saveusers(Common.getInstance().getUser());
            }

            @Override
            public void onFailure(String message)
            {
                removeLoader();
                Alerts.showError(SignIn.this,message);
            }


        });
    }

    private void startlocationService()
    {
        startService(new Intent(SignIn.this, LocationService.class));
    }

    private void startLoader() {
        LoaderFragment loaderFragment = new LoaderFragment("Wait...");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_main_start,loaderFragment, Constants.CATCH_LOADER_TAG).commit();
    }

    private void removeLoader()
    {


    try
    {
         Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.CATCH_LOADER_TAG);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }
    catch(Exception e)
    {
     Log.e("crash", "removeLoader:"  + e.getMessage());
    }

    }


    public void QbSignIn( String email, String password) {

        final QBUser qbUser = new QBUser();
     //  qbUser.setLogin(email);
        qbUser.setEmail(email);
        qbUser.setPassword(password);
        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle bundle) {

                removeLoader();
                DevicePreferences.getInstance().saverememberme(remember.isChecked());
                DevicePreferences.getInstance().saveQbuser(qbUser);


                startlocationService();//tracking your location
                Intent intent = new Intent(SignIn.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(SignIn.this, "Your time/date is not correct", Toast.LENGTH_SHORT).show();
                removeLoader();

            }
        });

    }


    private void socialSignup(final UserProfile userProfile)
    {
        userProfile.setLattitude(String.valueOf(DevicePreferences.getInstance().getLocation().getLatitude()));
        userProfile.setLongitude(String.valueOf(DevicePreferences.getInstance().getLocation().getLongitude()));

        SocialSignup.socialsignupnew(userProfile, getApplicationContext(), new OnSocialSignUpSuccess() {

            @Override
            public void onSuccess(SocialSignUpResponse socialSignUpResponse)
            {

             //   Toast.makeText(SignIn.this, "Social signup success", Toast.LENGTH_SHORT).show();
                    socialSignIn(userProfile);

            }

            @Override
            public void onFailure() {
                removeLoader();
             //   Toast.makeText(SignIn.this, "Social signup failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void  socialSignIn(final UserProfile userProfile) {
        userProfile.setDevice_type("1");
        userProfile.setDevice_token(FirebaseInstanceId.getInstance().getToken());
        SocialSignIn.socialsigninnew(userProfile, getApplicationContext(), new OnSocialSignInSuccess() {
            @Override
            public void onSuccess() {

               // Toast.makeText(SignIn.this, "social signin success", Toast.LENGTH_SHORT).show();
                DevicePreferences.getInstance().saverememberme(true);
                QBSignUp(userProfile.getEmail(),userProfile.getPassword());

            }

            @Override
            public void onFailure() {
                Toast.makeText(SignIn.this, "social signin failed", Toast.LENGTH_SHORT).show();
                removeLoader();
            }
        });

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager1.onActivityResult(requestCode, resultCode, data);

    }




    public  void QBSignUp(final String email, final String password) {
        QBUser qbUser = new QBUser();
        qbUser.setLogin(email);
        qbUser.setEmail(email);
        qbUser.setPassword(password);
        QBUsers.signUp(qbUser).performAsync( new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle)
            {
              //  Toast.makeText(getApplicationContext(), "QuickBlox Signup Success", Toast.LENGTH_SHORT).show();
                QbSignIn(userProfile.getEmail(), userProfile.getPassword());

            }

            @Override
            public void onError(QBResponseException e) {


                if (e.getMessage().equals("login has already been taken,email has already been taken"))
                {
                    QbSignIn(userProfile.getEmail(),userProfile.getPassword());
                }
                else
                {
                    Toast.makeText(SignIn.this, "Unexpected error!!", Toast.LENGTH_SHORT).show();
                    removeLoader();
                }
            }
        });



    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SignIn Page")
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

