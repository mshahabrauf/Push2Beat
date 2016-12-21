package com.attribes.push2beat.mainnavigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.Utils.OnSignUpSuccess;
import com.attribes.push2beat.models.BodyParams.SignUpParams;
import com.attribes.push2beat.network.DAL.SignUpDAL;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class SignUp extends AppCompatActivity {
    EditText username;
    EditText email;
    EditText password;
    EditText repeatpassword;
    Button createacount;
    CheckBox checkBox;
    Context mcontext;
    Boolean issuccess = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        QBSettings.getInstance().init(getApplicationContext(), Constants.APP_ID, Constants.AUTH_KEY, Constants.AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Constants.ACCOUNT_KEY);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);

        password = (EditText) findViewById(R.id.password);
        repeatpassword = (EditText) findViewById(R.id.repeatpassword);
        createacount = (Button) findViewById(R.id.create);



//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//
//
//                if (isChecked) {
//                    createacount.setClickable(true);
//
//                } else {
//                    createacount.setClickable(false);
//                }
//
//            }
//        });

        createacount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                SignUpParams user = new SignUpParams();
                user.setFirstName(""+username.getText().toString());
                user.setLastName("");
                user.setLongitude("23.3");
                user.setLattitude("14.4434");
                user.setProfileImage("");
                user.setEmail(""+email.getText().toString());
                user.setPassword(""+password.getText().toString());
//
                SignUpDAL.userRegister(user, getApplicationContext(),new OnSignUpSuccess(){
                    @Override
                    public void onSuccess() {
                        acntsignup(email.getText().toString().trim(),password.getText().toString().trim());
                    }

                    @Override
                    public void onFailure() {

                    }
                });




            }
        });


    }

    public  void acntsignup(String email,String password) {
        QBUser qbUser = new QBUser();
        qbUser.setLogin(email);
       qbUser.setEmail(email);
       qbUser.setPassword(password);

        QBUsers.signUp(qbUser).performAsync( new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle)
            {

              startActivity(new Intent(SignUp.this,SelectActivity.class));
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });



}

}