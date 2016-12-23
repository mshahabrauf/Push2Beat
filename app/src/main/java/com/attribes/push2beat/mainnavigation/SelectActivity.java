package com.attribes.push2beat.mainnavigation;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.databinding.ActivitySelectBinding;

public class SelectActivity extends AppCompatActivity {
    private ActivitySelectBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(SelectActivity.this,R.layout.activity_select);
        initButtons();


    }

    private void initButtons() {

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this,MainActivity.class);
                Common.getInstance().setRunType(1);
                startActivity(intent);
            }
        });

        binding.timerGhost.ghostGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this,MainActivity.class);
                Common.getInstance().setRunType(2);
                startActivity(intent);
            }
        });


        binding.timerCmiyc.cmGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SelectActivity.this,MainActivity.class);
                Common.getInstance().setRunType(3);
                startActivity(intent);
            }
        });


    }
}
