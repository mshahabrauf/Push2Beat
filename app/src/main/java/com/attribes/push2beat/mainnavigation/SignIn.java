package com.attribes.push2beat.mainnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.wang.avi.AVLoadingIndicatorView;

public class SignIn extends AppCompatActivity {
    ImageButton signin;
    EditText username;
    EditText password;
    Boolean onsuccess = false;
    AVLoadingIndicatorView progress;
    QBChatService chatService;
    QBUser opponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Common.getInstance().initializeQBInstance(getApplicationContext());

        signin = (ImageButton) findViewById(R.id.signinuser);
        username = (EditText) findViewById(R.id.usersignin);
        password = (EditText) findViewById(R.id.userpassword);
        progress = (AVLoadingIndicatorView) findViewById(R.id.progress_wheel);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);

                UserLoginDetailParams detail = new UserLoginDetailParams();
                detail.setUser_email(username.getText().toString());
                detail.setDevice_type("1");
                detail.setDevice_token(FirebaseInstanceId.getInstance().getToken());
                detail.setPassword(password.getText().toString());

                LoginDAL.userLogin(detail, new OnSignUpSuccess() {
                    @Override
                    public void onSuccess() {
                        acccoutSignin(username.getText().toString().trim(),password.getText().toString().trim());
                      Common.getInstance().setPassword(password.getText().toString());
                    }

                    @Override
                    public void onFailure() {

                    }
                });



            }
        });
    }


    public void acccoutSignin(final String email, String password) {

        final QBUser qbUser = new QBUser();
        qbUser.setLogin(email);
        qbUser.setEmail(email);
        qbUser.setPassword(password);
        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle bundle) {
                qbUser.setId(user.getId());
                Common.getInstance().setQbUser(qbUser);
                progress.setVisibility(View.GONE);


                Toast.makeText(getApplicationContext(), "QuickBlox SignIn Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignIn.this,SelectActivity.class));
                onsuccess=true;
                createChatService();

            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getApplicationContext(), "QuickBlox SignIn Failed", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void createChatService() {


        chatService = QBChatService.getInstance();
        chatService.login(Common.getInstance().getQbUser(), new QBEntityCallback() {
            @Override
            public void onSuccess(Object o, Bundle bundle) {
                Common.getInstance().setChatService(chatService);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("mChat",""+e);
            }
        });
    }

}

