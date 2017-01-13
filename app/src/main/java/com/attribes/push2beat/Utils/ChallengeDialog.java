package com.attribes.push2beat.Utils;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.attribes.push2beat.R;
import com.attribes.push2beat.databinding.DialogChallengeBinding;
import com.attribes.push2beat.mainnavigation.MainActivity;
import com.attribes.push2beat.models.Response.MyProfileResponse;
import com.attribes.push2beat.network.DAL.ChallengeReplyDAL;
import com.attribes.push2beat.network.DAL.GetProfileDAL;
import com.attribes.push2beat.network.interfaces.ProfileDataArrivalListner;

/**
 * Created by android on 1/10/17.
 */

public class ChallengeDialog extends Activity {


    private DialogChallengeBinding binding;
    private String opponentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_challenge);
        binding = DataBindingUtil.setContentView(this,R.layout.dialog_challenge);
        String message = getIntent().getExtras().get("text").toString();
        opponentId = getIntent().getExtras().get("challenger_id").toString();
        startListeners();
        binding.messageTv.setText(message);

    }

    private void startListeners() {
        binding.accept.setOnClickListener(new AcceptAction());
        binding.decline.setOnClickListener(new DeclineAction());
    }




    private class AcceptAction implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            GetProfileDAL.getProfileData(opponentId, new ProfileDataArrivalListner() {
                @Override
                public void onDataRecieved(MyProfileResponse.Data data) {
                    Intent intent = new Intent(ChallengeDialog.this,MainActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("id",data.getId());
                    bundle.putBoolean("fromNotification",true);
                    bundle.putString("email",data.getEmail());
                    bundle.putString("latitude",data.getLattitude());
                    bundle.putString("longitude",data.getLongitude());
                    bundle.putString("username",data.getFirst_name());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtras(bundle);
                    startActivity(intent);
                   ChallengeReplyDAL.replyChallenger(DevicePreferences.getInstance().getuser().getId(),opponentId,"1");

                }

                @Override
                public void onEmptyData(String msg) {

                }
            });

        }
    }


    private class DeclineAction implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ChallengeReplyDAL.replyChallenger(DevicePreferences.getInstance().getuser().getId(),opponentId,"0");
            finish();
        }
    }
}
