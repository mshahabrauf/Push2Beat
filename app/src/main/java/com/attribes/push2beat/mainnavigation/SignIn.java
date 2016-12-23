package com.attribes.push2beat.mainnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.OnSignUpSuccess;
import com.attribes.push2beat.models.BodyParams.UserLoginDetailParams;
import com.attribes.push2beat.network.DAL.LoginDAL;
import com.google.firebase.iid.FirebaseInstanceId;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class SignIn extends AppCompatActivity {
    ImageButton signin;
    EditText username;
    EditText password;
    Boolean onsuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Common.getInstance().initializeQBInstance(getApplicationContext());

        signin = (ImageButton) findViewById(R.id.signinuser);
        username = (EditText) findViewById(R.id.usersignin);
        password = (EditText) findViewById(R.id.userpassword);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserLoginDetailParams detail = new UserLoginDetailParams();
                detail.setUser_email(username.getText().toString());
                detail.setDevice_type(FirebaseInstanceId.getInstance().getToken());
                detail.setDevice_token("1");
                detail.setPassword(password.getText().toString());

                LoginDAL.userLogin(detail, new OnSignUpSuccess() {
                    @Override
                    public void onSuccess() {
                        acccoutSignin(username.getText().toString().trim(),password.getText().toString().trim());
                    }

                    @Override
                    public void onFailure() {

                    }
                });



            }
        });
    }


    public void acccoutSignin(String email, String password) {

        QBUser qbUser = new QBUser();
        qbUser.setLogin(email);
        qbUser.setEmail(email);
        qbUser.setPassword(password);
        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Common.getInstance().setQbUser(qbUser);

                Toast.makeText(getApplicationContext(), "QuickBlox SignIn Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignIn.this,SelectActivity.class));
                onsuccess=true;

            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getApplicationContext(), "QuickBlox SignIn Failed", Toast.LENGTH_SHORT).show();

            }
        });

    }
}