package com.attribes.push2beat.mainnavigation;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.databinding.ActivitySelectBinding;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

public class SelectActivity extends AppCompatActivity {
    private ActivitySelectBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(SelectActivity.this,R.layout.activity_select);
        binding.appbar.backButton.setVisibility(View.GONE);
        DevicePreferences.getInstance().init(getApplicationContext());
        createChatService();
        initButtons();



    }


    private void createChatService() {
        Common.getInstance().initializeQBInstance(getApplicationContext());
        final QBChatService chatService = QBChatService.getInstance();


            chatService.login(DevicePreferences.getInstance().getQbUser(), new QBEntityCallback() {
                @Override
                public void onSuccess(Object o, Bundle bundle) {
                    Common.getInstance().setChatService(chatService);
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.d("mChat", "" + e);
                }
            });

    }


    private void initButtons() {
        binding.appbar.text.setText("Select Activity");

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this,MainActivity.class);
                Common.getInstance().setRunType(1); // 1 : normal run
                startActivity(intent);
            }
        });

        binding.timerGhost.ghostGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this,MainActivity.class);
                Common.getInstance().setRunType(2);  // 2 : ghost Rider
                startActivity(intent);
            }
        });


        binding.timerCmiyc.cmGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SelectActivity.this,MainActivity.class);
                Common.getInstance().setRunType(3); // 3 : Catch me if you can
                startActivity(intent);
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
