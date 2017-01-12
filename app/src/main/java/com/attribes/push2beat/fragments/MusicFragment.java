package com.attribes.push2beat.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.vending.billing.IInAppBillingService;
import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.databinding.FragmentMusicBinding;
import utils_in_app_purchase.IabHelper;
import utils_in_app_purchase.IabResult;
import utils_in_app_purchase.Inventory;
import utils_in_app_purchase.Purchase;

import java.io.*;
import java.util.ArrayList;

import static com.attribes.push2beat.R.id.root;

/**
 * Created by Maaz on 12/30/2016.
 */
public class MusicFragment extends android.support.v4.app.Fragment {

    FragmentMusicBinding musicBinding;
    private IInAppBillingService mService;
    private ServiceConnection mServiceConn;

    static final String TAG = "Music HIT";   // Debug tag, for logging
    static final int RC_REQUEST = 10001;    // (arbitrary) request code for the purchase flow
    IabHelper mHelper;  // The helper object

    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAur6/aqP+oKF9ZqyZmUA44qKkp4xh6i8FomO0b8HBPUFWd9BHK/FjxXpFGUptZMG5ZRN7DGuODFKn+sYYepk8TPvQD/2tlPr70k8xWVpGrka7XVprE7s5D1ThgKwiNqT/kaqWPhRrJV0I8wf3O66HKSyXh80sCcWGWYuPKW61N4RoDoSul22lP7qarprL+CQCpQvUcXulMgZMrN+52D1I3ShPI/+M98SInrJmALay2A9oerA5RsLHz3r/XrXYMwbe7EwVP5IXIdFHsd1VWx9j5TZ6veeWW0qhf3ILv6pE0+v8kSW/xmayOIA5jz2E78l7y/t0a2Z/fA7H5bNN5TyW0QIDAQAB";

    String threeM_HIT = "com.attribes.push2beat.3minutes";
    String sevenM_HIT = "com.attribes.push2beat.7minutes";
    String fifteenM_HIT = "com.attribes.push2beat.15minutes";
    String twentytwoM_HIT = "com.attribes.push2beat.22minutes";
    String thirtyM_HIT = "com.attribes.push2beat.30minutes";

    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;     // Progress dialog type (0 - for Horizontal progress bar)

    private  MediaPlayer mPlayer;
    private final Handler handler = new Handler();

    public MusicFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        musicBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_music,container,false);
        View view = musicBinding.getRoot();
        isStoragePermissionGranted();
        initAndListners();
        enableHelperListner();
        initPurchaseListners();
        return view;
    }


    private void initAndListners() {
        DevicePreferences.getInstance().init(getActivity());
        mPlayer = new MediaPlayer();

        musicBinding.hitOneBtn.setOnClickListener(new HIT_OneListner());
        musicBinding.hitTwoBtn.setOnClickListener(new HIT_TwoListner());

        musicBinding.play.setOnClickListener(new PlayListner());
        musicBinding.stop.setOnClickListener(new StopListner());
    }


    private void initPurchaseListners() {
        musicBinding.freeBtn.setOnClickListener(new FreeHITListner());
        musicBinding.sevenHitBtn.setOnClickListener(new SevenHITListner());
        musicBinding.fifteenHitBtn.setOnClickListener(new FifteenHITListner());
        musicBinding.twentytwoHitBtn.setOnClickListener(new TwentwoHITListner());
        musicBinding.thirtyHitBtn.setOnClickListener(new ThirtyHITListner());
    }


    private void enableHelperListner() {

        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(getActivity(), base64EncodedPublicKey);    // Create the helper, passing it our context and the public key
        // to verify signatures with
        mHelper.enableDebugLogging(true);   // enable debug logging (for a production application, you should set this to false).

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }
                if (mHelper == null) return;    // Have we been disposed of in the meantime? If so, quit.

                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(true, prepareListOfProducts(),mGotInventoryListener);  // IAB is fully set up. Now, let's get an inventory of stuff we own.
            }
        });
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {

        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

            Toast.makeText(getActivity(), "Query inventory finished.", Toast.LENGTH_SHORT).show();

            if (mHelper == null) return;    // Have we been disposed of in the meantime? If so, quit.

            if (result.isFailure()) {       // Is it a failure?
                complain("Failed to query inventory: " + result);
                return;
            }
            Toast.makeText(getActivity(), "Query inventory was successful.", Toast.LENGTH_SHORT).show();


            Purchase threeM_HITPurchase = inventory.getPurchase(threeM_HIT);
            if(threeM_HITPurchase != null){
                mHelper.consumeAsync(inventory.getPurchase(threeM_HIT), mConsumeFinishedListener);
                return;
            }


            Purchase sevenM_HITPurchase = inventory.getPurchase(sevenM_HIT);
            if(sevenM_HITPurchase != null){
                mHelper.consumeAsync(inventory.getPurchase(sevenM_HIT), mConsumeFinishedListener);
                return;
            }


            Purchase fifteenM_HITPurchase = inventory.getPurchase(fifteenM_HIT);
            if(fifteenM_HITPurchase != null){
                mHelper.consumeAsync(inventory.getPurchase(fifteenM_HIT), mConsumeFinishedListener);
                return;
            }


            Purchase twentytwoM_HITPurchase = inventory.getPurchase(twentytwoM_HIT);
            if(twentytwoM_HITPurchase != null){
                mHelper.consumeAsync(inventory.getPurchase(twentytwoM_HIT), mConsumeFinishedListener);
                return;
            }


            Purchase thirtyM_HITPurchase = inventory.getPurchase(thirtyM_HIT);
            if(thirtyM_HITPurchase != null){
                mHelper.consumeAsync(inventory.getPurchase(thirtyM_HIT), mConsumeFinishedListener);
                return;
            }

            updateUi();

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
            Toast.makeText(getActivity(), "Initial inventory query finished; enabling main UI.", Toast.LENGTH_SHORT).show();
        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Toast.makeText(getActivity(), "Consumption successful", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Consumption successful. Provisioning.");
            }
            else {
                complain("Error while consuming: " + result);
            }
            updateUi();

            Log.d(TAG, "End consumption flow.");
        }
    };


    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(threeM_HIT)) {
                Toast.makeText(getActivity(), "threeM_HIT"+" Purchased", Toast.LENGTH_SHORT).show();
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }

            if (purchase.getSku().equals(sevenM_HIT)) {
                Toast.makeText(getActivity(), "sevenM_HIT"+" Purchased", Toast.LENGTH_SHORT).show();
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                new DownloadFileFromURL().execute("https://www.dropbox.com/s/27f3udiocs2wsb5/first7.mp3?dl=1");
            }

            if (purchase.getSku().equals(fifteenM_HIT)) {
                Toast.makeText(getActivity(), "fifteenM_HIT"+" Purchased", Toast.LENGTH_SHORT).show();
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                new DownloadFileFromURL().execute("https://www.dropbox.com/s/trcoakb6r22eujv/first15.mp3?dl=1");
            }

            if (purchase.getSku().equals(twentytwoM_HIT)) {
                Toast.makeText(getActivity(), "twentytwoM_HIT"+" Purchased", Toast.LENGTH_SHORT).show();
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                new DownloadFileFromURL().execute("https://www.dropbox.com/s/zgie7locjtrdumr/first22.mp3?dl=1");
            }

            if (purchase.getSku().equals(thirtyM_HIT)) {
                Toast.makeText(getActivity(), "thirtyM_HIT"+" Purchased", Toast.LENGTH_SHORT).show();
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                new DownloadFileFromURL().execute("https://www.dropbox.com/s/c4vuopcqd2mzuzz/fullmix.mp3?dl=1");
            }
        }
    };

    private ArrayList<String> prepareListOfProducts() {

        ArrayList<String> skuList = new ArrayList<String>();
        skuList.add("com.attribes.push2beat.3minutes");
        skuList.add("com.attribes.push2beat.7minutes");
        skuList.add("com.attribes.push2beat.15minutes");
        skuList.add("com.attribes.push2beat.22minutes");
        skuList.add("com.attribes.push2beat.30minutes");

        return skuList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            getActivity().unbindService(mServiceConn);
        }
    }

    private void complain(String message) {
        Log.e(TAG, "**** Error: " + message);
        alert("Error: " + message);
    }

    private void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    private void updateUi() {


    }


    private class FreeHITListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String payload = "";
            new DownloadFileFromURL().execute("https://www.dropbox.com/s/1vyy0x5r4zmrhsq/first3.mp3?dl=1");
           // mHelper.launchPurchaseFlow(getActivity(), threeM_HIT, RC_REQUEST, mPurchaseFinishedListener, payload);
        }
    }

    private class SevenHITListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String payload = "";
            mHelper.launchPurchaseFlow(getActivity(), sevenM_HIT, RC_REQUEST, mPurchaseFinishedListener, payload);
        }
    }

    private class FifteenHITListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String payload = "";
            mHelper.launchPurchaseFlow(getActivity(), fifteenM_HIT, RC_REQUEST, mPurchaseFinishedListener, payload);
        }
    }

    private class TwentwoHITListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String payload = "";
            mHelper.launchPurchaseFlow(getActivity(), twentytwoM_HIT, RC_REQUEST, mPurchaseFinishedListener, payload);
        }
    }

    private class ThirtyHITListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String payload = "";
            mHelper.launchPurchaseFlow(getActivity(), thirtyM_HIT, RC_REQUEST, mPurchaseFinishedListener, payload);
        }
    }

    private class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        private void showDialog(int progress_bar_type) {

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Downloading Please wait...");
            if (progress_bar_type==ProgressDialog.STYLE_HORIZONTAL){

                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
            }
            pDialog.show();
        }

        @Override
       protected String doInBackground(String... file_url) {

//            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maaz_files";
//            String file_path = Environment.DIRECTORY_DOWNLOADS + "/maaz_files";

            String file_path =  Environment.getExternalStorageDirectory()+"/maaz_files";
            File direct = new File(file_path);

            if (!direct.exists()) {
                direct.mkdirs();
            }

            DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloadUri = Uri.parse(file_url[0]);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            request.setAllowedNetworkTypes( DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle("Music")
                    .setDestinationInExternalPublicDir("/maaz_files","music.mp3");
            DevicePreferences.getInstance().save_musicTrackPath(file_path);
            mgr.enqueue(request);

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));  // setting progress percentage
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            Toast.makeText(getActivity(),"Done!!!", Toast.LENGTH_LONG).show();
        }
    }

    private class HIT_OneListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            musicBinding.hitTwo.hitTwo.setVisibility(View.GONE);
            musicBinding.hitOne.setVisibility(View.VISIBLE);
        }
    }

    private class HIT_TwoListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            musicBinding.hitTwo.hitTwo.setVisibility(View.VISIBLE);
            musicBinding.hitOne.setVisibility(View.GONE);
        }
    }


    private class PlayListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            playMusic();
        }
    }

    private void playMusic() {

        if(DevicePreferences.getInstance().getMusicTrackPath() != null) {

            Uri myUri = Uri.parse(DevicePreferences.getInstance().getMusicTrackPath()+"/music.mp3");
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mPlayer.setDataSource(getActivity(), myUri);
            } catch (IllegalArgumentException e) {
                Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (SecurityException e) {
                Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IllegalStateException e) {
                Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                mPlayer.prepare();
            } catch (IllegalStateException e) {
                Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            }

            mPlayer.start();
            //set up MediaPlayer
//            try {
//                mp.setDataSource(DevicePreferences.getInstance().getMusicTrackPath());
//                mp.prepare();
//                mp.start();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        else{

            try {
                DevicePreferences.getInstance().getMusicTrackPath();
            } catch (NullPointerException e) {
                Toast.makeText(getActivity(), "No music available , Download it first !" + e, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class StopListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(mPlayer!=null && mPlayer.isPlaying()){
                mPlayer.stop();
            }
//            if(mp!=null && mp.isPlaying()){
//                mp.stop();
//            }
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission is granted", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(getActivity(), "Permission is revoked", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Toast.makeText(getActivity(), "Permission is granted", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            initPurchaseListners();
        }
    }
}
