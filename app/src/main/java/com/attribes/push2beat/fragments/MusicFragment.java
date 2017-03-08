package com.attribes.push2beat.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.databinding.FragmentMusicBinding;
import com.attribes.push2beat.mainnavigation.MainActivity;

import java.io.File;
import java.util.ArrayList;

import utils_in_app_purchase.IabHelper;
import utils_in_app_purchase.IabResult;
import utils_in_app_purchase.Inventory;
import utils_in_app_purchase.Purchase;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Maaz on 12/30/2016.
 */
public class MusicFragment extends android.support.v4.app.Fragment {

    FragmentMusicBinding musicBinding;
    private IInAppBillingService mService;
    private ServiceConnection mServiceConn;
    private String fileName;

    static final String TAG = "Music HIT";   // Debug tag, for logging
    static final int RC_REQUEST = 10001;    // (arbitrary) request code for the purchase flow
    IabHelper mHelper;  // The helper object

    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAur6/aqP+oKF9ZqyZmUA44qKkp4xh6i8FomO0b8HBPUFWd9BHK/FjxXpFGUptZMG5ZRN7DGuODFKn+sYYepk8TPvQD/2tlPr70k8xWVpGrka7XVprE7s5D1ThgKwiNqT/kaqWPhRrJV0I8wf3O66HKSyXh80sCcWGWYuPKW61N4RoDoSul22lP7qarprL+CQCpQvUcXulMgZMrN+52D1I3ShPI/+M98SInrJmALay2A9oerA5RsLHz3r/XrXYMwbe7EwVP5IXIdFHsd1VWx9j5TZ6veeWW0qhf3ILv6pE0+v8kSW/xmayOIA5jz2E78l7y/t0a2Z/fA7H5bNN5TyW0QIDAQAB";

    String threeM_HIT = "com.attribes.push2beat.3minutes";
    String sevenM_HIT = "com.attribes.push2beat.7minutes";
    String fifteenM_HIT = "com.attribes.push2beat.15minutes";
    String twentytwoM_HIT = "com.attribes.push2beat.22minutes";
    String thirtyM_HIT = "com.attribes.push2beat.30minutes";

    String sevenM_HIT2 = "com.attribe.push2beat.hit2.3minute";
    String fifteenM_HIT2 = "com.attribes.push2beat.hiit2.22minutes";
    String twentytwoM_HIT2 = "com.attribes.push2beat.hiit2.22minutes";
    String thirtyM_HIT2 = "com.attribes.push2beat.hiit2.30minutes";
    String fourtyM_HIT2 = "com.attribes.push2beat.hiit2.40minutes";





    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;     // Progress dialog type (0 - for Horizontal progress bar)
    private boolean isOnGpsFragment = false;

    private  MediaPlayer mPlayer;
    private final Handler handler = new Handler();

    @SuppressLint("ValidFragment")
    public MusicFragment(boolean bool)
    {
        Common.getInstance().updateFragmentCounter();
        isOnGpsFragment = bool;
    }

    public MusicFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        musicBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_music,container,false);
        View view = musicBinding.getRoot();
        ((MainActivity)getActivity()).changeTitle("Select Your Workout");
        isStoragePermissionGranted();
        updateUI();
        initAndListners();
        enableHelperListner();
        initPurchaseListners();
        return view;
    }

    private void updateUI() {
        if(isOnGpsFragment)
        {
            musicBinding.widgetPlayer.setVisibility(View.GONE);
        }
    }


    private void initAndListners() {
        DevicePreferences.getInstance().init(getActivity());

        mPlayer = new MediaPlayer();

        musicBinding.libraryButton.setOnClickListener(new LibraryListener());
        musicBinding.hitOne.setOnClickListener(new HIT_OneListner());
        musicBinding.hitTwo.setOnClickListener(new HIT_TwoListner());
        musicBinding.pause.setOnClickListener(new PauseListener());
        musicBinding.play.setOnClickListener(new PlayListner());
        musicBinding.stop.setOnClickListener(new StopListner());
    }


    private void initPurchaseListners() {
        musicBinding.freeBtn.setOnClickListener(new FreeHITListner());
        musicBinding.sevenHitBtn.setOnClickListener(new SevenHITListner());
        musicBinding.fifteenHitBtn.setOnClickListener(new FifteenHITListner());
        musicBinding.twentytwoHitBtn.setOnClickListener(new TwentwoHITListner());
        musicBinding.thirtyHitBtn.setOnClickListener(new ThirtyHITListner());

        // HIIT TWO SONGS
        musicBinding.hitTwoLayout.sevenHitBtn.setOnClickListener(new HitTwoSevenListener() );
        musicBinding.hitTwoLayout.fifteenHitBtn.setOnClickListener(new HitTwoFifteenListener() );
        musicBinding.hitTwoLayout.twentytwoHitBtn.setOnClickListener(new TwentwoHIT2Listner() );
        musicBinding.hitTwoLayout.thirtyHitBtn.setOnClickListener(new HitTwothirtyListener());
        musicBinding.hitTwoLayout.thirtysixHitBtn.setOnClickListener(new  HitTwothirtyListener());  //Todo :Change it to 36 listerner
        musicBinding.hitTwoLayout.fortyHitBtn.setOnClickListener(new HitTwotfortyListener());
        musicBinding.hitTwoLayout.freeBtn.setOnClickListener(new FreeHIT2Listner() );

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


           // Toast.makeText(getActivity(), "Query inventory finished.", Toast.LENGTH_SHORT).show();

            if (mHelper == null) return;    // Have we been disposed of in the meantime? If so, quit.

            if (result.isFailure()) {       // Is it a failure?
                complain("Failed to query inventory: " + result);
                return;
            }

           // Toast.makeText(getActivity(), "Query inventory was successful.", Toast.LENGTH_SHORT).show();


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
        //    Toast.makeText(getActivity(), "Initial inventory query finished; enabling main UI.", Toast.LENGTH_SHORT).show();
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
           //     Toast.makeText(getActivity(), "Consumption successful", Toast.LENGTH_SHORT).show();
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

                       /*  Hit 2 music consumptions */

            if (purchase.getSku().equals(fifteenM_HIT2)) {
                Toast.makeText(getActivity(), "fifteenM_HIT2"+" Purchased", Toast.LENGTH_SHORT).show();
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                new DownloadFileFromURL().execute("https://www.dropbox.com/s/81tbdhv7qb93mtu/0-15.40.mp3?dl=0");
            }

            if (purchase.getSku().equals(twentytwoM_HIT2)) {
                Toast.makeText(getActivity(), "twentytwoM_HIT2"+" Purchased", Toast.LENGTH_SHORT).show();
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                new DownloadFileFromURL().execute("https://www.dropbox.com/s/i5dyz8e593utwce/0-22.22.mp3?dl=0");
            }

            if (purchase.getSku().equals(thirtyM_HIT2)) {
                Toast.makeText(getActivity(), "thirtyM_HIT2"+" Purchased", Toast.LENGTH_SHORT).show();
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                new DownloadFileFromURL().execute("https://www.dropbox.com/s/w8bcrocm4purgtd/0-29.15.mp3?dl=0");
            }

            if (purchase.getSku().equals(fourtyM_HIT2)) {
                Toast.makeText(getActivity(), "fourtyM_HIT2"+" Purchased", Toast.LENGTH_SHORT).show();
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                new DownloadFileFromURL().execute("https://www.dropbox.com/s/of965naknzekth7/Push2beat%20FULL%20MASTER.mp3?dl=0");
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

        if (resultCode == RESULT_OK  && requestCode==1) {
            Uri uri = data.getData();
            DevicePreferences.getInstance().saveMusicTrackPath(uri.toString());
            if(isOnGpsFragment) {
                startPrepareFragment();
            }
        }


        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
       //     super.onActivityResult(requestCode, resultCode, data);
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
        bld.setMessage("There is some issue occur in your In-App purchasing kindly retry later");
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    private void updateUi() {


    }


    private class FreeHIT2Listner implements View.OnClickListener {
        @Override
        public void onClick(View view) {


            File extStore = Environment.getExternalStorageDirectory();
            File myFile = new File(extStore.getAbsolutePath() + "/push2beat/0-8.25.mp3");

            if(myFile.exists() ){
                if(isOnGpsFragment) {
                    DevicePreferences.getInstance().saveMusicTrackPath(extStore.getAbsolutePath() + "/push2beat/0-8.25.mp3");
                    startPrepareFragment();
                }
                else {
                    DevicePreferences.getInstance().saveMusicTrackPath(extStore.getAbsolutePath() + "/push2beat/0-8.25.mp3");
                    ((MainActivity)getActivity()).playMusic();
                }
            }
            else {
                fileName = "0-8.25.mp3";
                new DownloadFileFromURL().execute("https://www.dropbox.com/s/q67r5s94dm3ny1m/0-8.25.mp3?dl=1");

            }
        }
        // mHelper.launchPurchaseFlow(getActivity(), threeM_HIT, RC_REQUEST, mPurchaseFinishedListener, payload);

    }


    private class FreeHITListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {


            File extStore = Environment.getExternalStorageDirectory();
            File myFile = new File(extStore.getAbsolutePath() + "/push2beat/first3.mp3");

            if(myFile.exists() ){
               if(isOnGpsFragment) {
                   startPrepareFragment();
                   DevicePreferences.getInstance().saveMusicTrackPath(extStore.getAbsolutePath() + "/push2beat/first3.mp3");
               }
                else {
                   DevicePreferences.getInstance().saveMusicTrackPath(extStore.getAbsolutePath() + "/push2beat/first3.mp3");
                   ((MainActivity)getActivity()).playMusic();
               }
            }
            else {
                fileName = "first3.mp3";
                new DownloadFileFromURL().execute("https://www.dropbox.com/s/1vyy0x5r4zmrhsq/first3.mp3?dl=1");

            }
        }
        // mHelper.launchPurchaseFlow(getActivity(), threeM_HIT, RC_REQUEST, mPurchaseFinishedListener, payload);

    }

    private class SevenHITListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String payload = "";

                try {
                    mHelper.launchPurchaseFlow(getActivity(), sevenM_HIT, RC_REQUEST, mPurchaseFinishedListener, payload);
                }
                catch(IllegalStateException ex){
                    Toast.makeText(getContext(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
                }
        }
    }


    private class HitTwotfortyListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String payload = "";
            try {
                mHelper.launchPurchaseFlow(getActivity(), fourtyM_HIT2, RC_REQUEST, mPurchaseFinishedListener, payload);
            } catch(IllegalStateException ex){
                Toast.makeText(getContext(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private class HitTwothirtyListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String payload = "";
            try {
                mHelper.launchPurchaseFlow(getActivity(), thirtyM_HIT2, RC_REQUEST, mPurchaseFinishedListener, payload);
            } catch(IllegalStateException ex){
                Toast.makeText(getContext(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
            }

        }
    }



    private class HitTwoSevenListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            File extStore = Environment.getExternalStorageDirectory();
            File myFile = new File(extStore.getAbsolutePath() + "/push2beat/first8.mp3");

            if(myFile.exists() ){
                if(isOnGpsFragment) {
                    startPrepareFragment();
                    DevicePreferences.getInstance().saveMusicTrackPath(extStore.getAbsolutePath() + "/push2beat/first8.mp3");
                }
                else {
                    DevicePreferences.getInstance().saveMusicTrackPath(extStore.getAbsolutePath() + "/push2beat/first8.mp3");
                    ((MainActivity)getActivity()).playMusic();
                }
            }
            else {
                fileName = "first8.mp3";
                new DownloadFileFromURL().execute("https://www.dropbox.com/s/q67r5s94dm3ny1m/0-8.25.mp3?dl=0");

            }
        }
    }


    private class HitTwoFifteenListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String payload = "";
            try {
                mHelper.launchPurchaseFlow(getActivity(), fifteenM_HIT2, RC_REQUEST, mPurchaseFinishedListener, payload);
            } catch(IllegalStateException ex){
                Toast.makeText(getContext(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class FifteenHITListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String payload = "";
    try {
    mHelper.launchPurchaseFlow(getActivity(), fifteenM_HIT, RC_REQUEST, mPurchaseFinishedListener, payload);
    }
    catch(IllegalStateException ex){
    Toast.makeText(getContext(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
}
        }
    }
    private class TwentwoHIT2Listner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String payload = "";
            try {
                mHelper.launchPurchaseFlow(getActivity(), twentytwoM_HIT2, RC_REQUEST, mPurchaseFinishedListener, payload);
            }
            catch(IllegalStateException ex){
                Toast.makeText(getContext(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class TwentwoHITListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String payload = "";
            try {
                mHelper.launchPurchaseFlow(getActivity(), twentytwoM_HIT, RC_REQUEST, mPurchaseFinishedListener, payload);
            }
            catch(IllegalStateException ex){
                Toast.makeText(getContext(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class ThirtyHITListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String payload = "";
            try{
                mHelper.launchPurchaseFlow(getActivity(), thirtyM_HIT, RC_REQUEST, mPurchaseFinishedListener, payload);
            }  catch(IllegalStateException ex){
                Toast.makeText(getContext(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... file_url) {
            String file_path =  Environment.getExternalStorageDirectory()+"/push2beat";
            File direct = new File(file_path);

            if (!direct.exists()) {
                direct.mkdirs();
            }

            DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloadUri = Uri.parse(file_url[0]);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            DevicePreferences.getInstance().saveMusicTrackPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/push2beat/"+fileName);


            request.setAllowedNetworkTypes( DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle("Music")
                    .setDestinationInExternalPublicDir("push2beat/",fileName);
           // DevicePreferences.getInstance().saveMusicTrackPath(file_path+"/music.mp3");
            long lastDownload = mgr.enqueue(request);


            boolean downloading = true;
            while (downloading) {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(lastDownload);

                try {
                    Cursor cursor = mgr.query(q);
                    cursor.moveToFirst();
                    long bytes_downloaded = cursor.getLong(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    long bytes_total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    publishProgress(String.valueOf(bytes_total), String.valueOf(bytes_downloaded));
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        pDialog.dismiss();

                        if (isOnGpsFragment) {
                            startPrepareFragment();
                        } else {
                            ((MainActivity)getActivity()).playMusic();
                        }

                        downloading = false;
                    }
                }
                catch (SQLiteException e)
                {
                    e.printStackTrace(); // if unable to open database file (code 14)
                }
                catch (RuntimeException e)
                {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pDialog.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissDialog();
        }

        private void showDialog(int progress_bar_type) {

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Downloading Please wait...");
            pDialog.setCancelable(false);
            if (progress_bar_type==ProgressDialog.STYLE_HORIZONTAL){

                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
            }
            pDialog.show();
        }

        private void dismissDialog(){
            pDialog = new ProgressDialog(getActivity());
            pDialog.dismiss();
        }

    }

    private class HIT_OneListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            musicBinding.hitTwoLayout.hitTwo.setVisibility(View.GONE);
            musicBinding.hitoneLayout.setVisibility(View.VISIBLE);
            musicBinding.hitOne.setBackgroundColor(getResources().getColor(R.color.secondary_red_dark));
            musicBinding.hitTwo.setBackgroundColor(getResources().getColor(R.color.secondary_red));
        }
    }



    private class HIT_TwoListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            musicBinding.hitTwoLayout.hitTwo.setVisibility(View.VISIBLE);
            musicBinding.hitoneLayout.setVisibility(View.GONE);
            musicBinding.hitOne.setBackgroundColor(getResources().getColor(R.color.secondary_red));
            musicBinding.hitTwo.setBackgroundColor(getResources().getColor(R.color.secondary_red_dark));
        }
    }


    private class PlayListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {

        if(DevicePreferences.getInstance().getMusicTrackPath() == null || DevicePreferences.getInstance().getMusicTrackPath().isEmpty()){
            Toast.makeText(getActivity(), "Please select or download music first", Toast.LENGTH_SHORT).show();
        }else{
            ((MainActivity)getActivity()).playMusic();
        }

        }
    }



    private void startPrepareFragment()
    {


        PrepareFragment prepareFragment = new PrepareFragment();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.music_container, prepareFragment).commitAllowingStateLoss();
    }


    private class StopListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            ((MainActivity)getActivity()).stopMusic();

        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation

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

    private class LibraryListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
           startActivityForResult(i,1);

        }
    }


    private class PauseListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ((MainActivity)getActivity()).pauseMusic();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }


    }


}



