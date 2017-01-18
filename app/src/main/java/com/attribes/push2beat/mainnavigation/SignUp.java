package com.attribes.push2beat.mainnavigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.Utils.OnSignUpSuccess;
import com.attribes.push2beat.fragments.LoaderFragment;
import com.attribes.push2beat.models.BodyParams.SignUpParams;
import com.attribes.push2beat.models.BodyParams.UserLoginDetailParams;
import com.attribes.push2beat.network.DAL.LoginDAL;
import com.attribes.push2beat.network.DAL.SignUpDAL;
import com.google.firebase.iid.FirebaseInstanceId;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    EditText username;
    EditText email;
    EditText password;
    EditText repeatpassword;
    Button createacount;
    CheckBox checkBox;
    QBChatService chatService;
    Context mcontext;
    Boolean issuccess = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

       Common.getInstance().initializeQBInstance(getApplicationContext());

        checkBox = (CheckBox) findViewById(R.id.checkbox);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);

        password = (EditText) findViewById(R.id.password);

        repeatpassword = (EditText) findViewById(R.id.repeatpassword);
        createacount = (Button) findViewById(R.id.create);



        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {


                if (isChecked) {
                    createacount.setEnabled(true);

                } else {
                    createacount.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "You Must Agree Terms And Conditions", Toast.LENGTH_SHORT).show();


                }

            }
        });

        createacount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                SignUpParams user = new SignUpParams();

                user.setFirstName("" + username.getText().toString());
                user.setLastName("");
                user.setLongitude(""+DevicePreferences.getInstance().getLocation().getLatitude());
                user.setLattitude(""+DevicePreferences.getInstance().getLocation().getLongitude());
                user.setProfileImage("");
                user.setEmail("" + email.getText().toString());
                user.setPassword("" + password.getText().toString());

                user.setPassword("" + password.getText().toString());
                if(username.getText().toString().equals("") || email.getText().toString().equals("") || password.getText().toString().equals("") || repeatpassword.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Kindly, complete this form", Toast.LENGTH_SHORT).show();
                }
                else if(user.getPassword().equals(repeatpassword.getText().toString())==false){
                    Toast.makeText(getApplicationContext(), "Password and Repeat Password Not Matched", Toast.LENGTH_SHORT).show();
                }
                else if(user.getPassword().length()<8&&repeatpassword.getText().toString().length()<8)
                {

                    Toast.makeText(getApplicationContext(), "Password Length and Repeat Password Length should be 8 or greater then 8 ", Toast.LENGTH_SHORT).show();
                }
                else if(checkBox.isChecked()==false){
                    Toast.makeText(getApplicationContext(), "You must agree terms and conditions", Toast.LENGTH_SHORT).show();
                }
                else if(checkBox.isChecked()==true&&(user.getPassword().equals(repeatpassword.getText().toString())))
                {
                    startLoader();
                    SignUpDAL.userRegister(user, getApplicationContext(), new OnSignUpSuccess() {
                        @Override
                        public void onSuccess() {
                            QBSignUp(email.getText().toString().trim(), password.getText().toString().trim());

                        }
                        @Override
                        public void onFailure() {
                            removeLoader();
                        }
                    });
                }}

        });    }



    private void startLoader() {
        LoaderFragment loaderFragment = new LoaderFragment("Please Wait...");
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

    private void signInUser(String email, String password) {

        UserLoginDetailParams detail = new UserLoginDetailParams();
        detail.setUser_email(email);
        detail.setDevice_type("1");
        detail.setDevice_token(FirebaseInstanceId.getInstance().getToken());
        detail.setPassword(password);


        LoginDAL.userLogin(detail, new OnSignUpSuccess() {
            @Override
            public void onSuccess() {

                Common.getInstance().setPassword(SignUp.this.password.getText().toString());
                DevicePreferences.getInstance().saveusers(Common.getInstance().getUser());
            }

            @Override
            public void onFailure() {
                removeLoader();
                Toast.makeText(SignUp.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }


    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

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
                Toast.makeText(getApplicationContext(), "QuickBlox Signup Success", Toast.LENGTH_SHORT).show();

                    signInUser(email,password);
                    QbSignIn(email,password);
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getApplicationContext(), "QuickBlox Signup Failed", Toast.LENGTH_SHORT).show();
                removeLoader();
            }
        });



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
                startActivity(new Intent(SignUp.this, SelectActivity.class));

                finish();

            }

            @Override
            public void onError(QBResponseException e) {
                removeLoader();
                // Toast.makeText(getApplicationContext(), "QuickBlox SignIn Failed", Toast.LENGTH_SHORT).show();

            }
        });

    }



}