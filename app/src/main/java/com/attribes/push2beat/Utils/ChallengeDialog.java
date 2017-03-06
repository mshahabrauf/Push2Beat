package com.attribes.push2beat.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.attribes.push2beat.R;
import com.attribes.push2beat.databinding.DialogChallengeBinding;
import com.attribes.push2beat.fragments.LoaderFragment;
import com.attribes.push2beat.interfaces.MyCallBacks;
import com.attribes.push2beat.mainnavigation.MainActivity;
import com.attribes.push2beat.models.Response.MyProfileResponse;
import com.attribes.push2beat.models.Response.PushFireBase.PushResponse;
import com.attribes.push2beat.network.DAL.ChallengeReplyDAL;
import com.attribes.push2beat.network.DAL.GetProfileDAL;
import com.attribes.push2beat.network.interfaces.ProfileDataArrivalListner;

/**
 * Created by android on 1/10/17.
 */

public class ChallengeDialog extends AppCompatActivity {


    private DialogChallengeBinding binding;
    private String opponentId;
    private MyProfileResponse.Data data;
    private boolean ChallengeOrAcceptNotification;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_challenge);
        this.setFinishOnTouchOutside(false);
        binding = DataBindingUtil.setContentView(this,R.layout.dialog_challenge);
        String message = getIntent().getExtras().get("text").toString();
        opponentId = getIntent().getExtras().get("challenger_id").toString();
        ChallengeOrAcceptNotification = getIntent().getExtras().getBoolean("fromNotification");

        /*Accept or Challenge */
        if (!ChallengeOrAcceptNotification)
        {
            binding.blackBackground.setVisibility(View.GONE);
            getApiData();
        }
        else    //run when new challenge recived
        {
            startListeners();
            binding.messageTv.setText(message);
        }


    }

    private void getApiData()
    {
       startLoader();

        GetProfileDAL.getProfileData(opponentId, new ProfileDataArrivalListner() {
            @Override
            public void onDataRecieved(MyProfileResponse.Data datam) {
                    data = datam;
                    removeLoader();

                    if(ChallengeOrAcceptNotification == false)//when opponent accepts challenged
                    {
                        startMainActivityForAccept();
                    }
                    else
                    {
                        startActivityforChallenge();
                    }
                    finish();


            }

            @Override
            public void onEmptyData(String msg) {
                removeLoader();

            }

            @Override
            public void onFailure() {
                removeLoader();
                Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startActivityforChallenge() {
        ChallengeReplyDAL.replyChallenger(DevicePreferences.getInstance().getuser().getId(), opponentId, "1", new MyCallBacks<PushResponse>() {
            @Override
            public void onSuccess(PushResponse pushResponse)
            {
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

            }

            @Override
            public void onFailure(String message)
            {
                Alerts.showError(ChallengeDialog.this,message);

            }
        });


    }

    private void startMainActivityForAccept() {
        Intent intent = new Intent(this,MainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("id",data.getId());
        bundle.putBoolean("fromcatch",true);
        bundle.putString("email",data.getEmail());
        bundle.putString("latitude",data.getLattitude());
        bundle.putString("longitude",data.getLongitude());
        bundle.putString("username",data.getFirst_name());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void startListeners() {
        binding.accept.setOnClickListener(new AcceptAction());
        binding.decline.setOnClickListener(new DeclineAction());
    }




    private class AcceptAction implements View.OnClickListener {
        @Override
        public void onClick(View view)
        {
            getApiData();
        }
    }

    /**
     * These two method is just for showing progress bar when task is fetching
     * @param message
     */

    private void showProgress(String message)
    {
        progressDialog= ProgressDialog.show(ChallengeDialog.this,"",message,false);
    }
    private void hideProgress()
    {
        progressDialog.dismiss();
    }

    private class DeclineAction implements View.OnClickListener {
        @Override
        public void onClick(View view)
        {
            showProgress("Requesting....");
            ChallengeReplyDAL.replyChallenger(DevicePreferences.getInstance().getuser().getId(), opponentId, "0", new MyCallBacks<PushResponse>() {
                @Override
                public void onSuccess(PushResponse data)
                {
                    hideProgress();
                    if(data.getSuccess()==1)
                    {

                        Alerts.showDialoge(ChallengeDialog.this, "You have decline the challenged!");
                        finish();
                    }
                    if(data.getSuccess()==0)
                    {
                        Alerts.showDialoge(ChallengeDialog.this, "Request was not sent to the challenger!!");
                        finish();
                    }

                }

                @Override
                public void onFailure(String message)
                {
                    hideProgress();
                    Alerts.showError(ChallengeDialog.this,message);
                    finish();

                }
            });

        }
    }



    private void startLoader() {
        try {
            LoaderFragment loaderFragment = new LoaderFragment("Fetching Opponent Detail...");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.parent_top,loaderFragment, Constants.CATCH_LOADER_TAG).commit();
        }
        catch(Exception e)
        {
            Log.e("crash", "removeLoader:"  + e.getMessage());
        }

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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
