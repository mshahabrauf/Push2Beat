package com.attribes.push2beat.mainnavigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.attribes.push2beat.R;

public class MainActivityStart extends AppCompatActivity {

    private ImageButton signinbtn;
    private ImageButton signupbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_start);
        signinbtn=(ImageButton)findViewById(R.id.signin);

        signupbtn=(ImageButton)findViewById(R.id.signup);


        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

Intent intent = new Intent(MainActivityStart.this,SignIn.class);
startActivity(intent);

            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityStart.this,SignUp.class);
                startActivity(intent);



            }
        });
    }
}
