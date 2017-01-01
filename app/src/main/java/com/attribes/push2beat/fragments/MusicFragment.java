package com.attribes.push2beat.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.vending.billing.IInAppBillingService;
import com.attribes.push2beat.R;
import com.attribes.push2beat.databinding.FragmentMusicBinding;
import utils_in_app_purchase.IabHelper;
import utils_in_app_purchase.IabResult;
import utils_in_app_purchase.Inventory;
import utils_in_app_purchase.Purchase;

import java.util.ArrayList;

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

    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm2lLP4cixk0F13868EssZeuv3S1/179cxIj070t2WKxLsodu1F5N2BOiPyQdAod2pXvq45wYZBd91gR7f4ofTTNGPPis8qPwtk4af9J2HEemWIoOrar4MRmHqmwH0miPtZMM/udOddX0Q+mzBbyjsHSIztLDXbb8YPiqh5NyOstkfrSiX7V5dsL9PVWD/ekUGxEV0FjqOw5vy5sKxZ3swIc2/NvrNLwa/hJm/Oql1JFM4KOKYYQNyj3XArf/uc23rvrfo+RwhYv5DnRqyOgZsmKcI+GNroyW9RlZ9fUzb6D/mizt7djfjYoNT8/zxvw+x5ZtPmdtvM70Cf43t9yvkwIDAQAB";

    String threeM_HIT = "com.infoicon.push2beat.3minutess";
    String sevenM_HIT = "com.infoicon.push2beat.7minutes";
    String fifteenM_HIT = "com.infoicon.push2beat.15minutes";
    String twentytwoM_HIT = "com.infoicon.push2beat.22minutes";
    String thirtyM_HIT = "com.infoicon.push2beat.30minute";


    public MusicFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        musicBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_music,container,false);
        View view = musicBinding.getRoot();
        initPurchaseListners();
        enableHelperListner();
        return view;
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
            }

            if (purchase.getSku().equals(fifteenM_HIT)) {
                Toast.makeText(getActivity(), "fifteenM_HIT"+" Purchased", Toast.LENGTH_SHORT).show();
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }

            if (purchase.getSku().equals(twentytwoM_HIT)) {
                Toast.makeText(getActivity(), "twentytwoM_HIT"+" Purchased", Toast.LENGTH_SHORT).show();
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }

            if (purchase.getSku().equals(thirtyM_HIT)) {
                Toast.makeText(getActivity(), "thirtyM_HIT"+" Purchased", Toast.LENGTH_SHORT).show();
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }
//            else if (purchase.getSku().equals(SKU_PREMIUM)) {
//                // bought the premium upgrade!
//                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
//                alert("Thank you for upgrading to premium!");
//                mIsPremium = true;
//                updateUi();
//                setWaitScreen(false);
//            }
//            else if (purchase.getSku().equals(SKU_INFINITE_GAS)) {
//                // bought the infinite gas subscription
//                Log.d(TAG, "Infinite gas subscription purchased.");
//                alert("Thank you for subscribing to infinite gas!");
//                mSubscribedToInfiniteGas = true;
//                mTank = TANK_MAX;
//                updateUi();
//                setWaitScreen(false);
//            }
        }
    };

    private ArrayList<String> prepareListOfProducts() {

        ArrayList<String> skuList = new ArrayList<String>();
        skuList.add("com.infoicon.push2beat.3minutess");
        skuList.add("com.infoicon.push2beat.7minutes");
        skuList.add("com.infoicon.push2beat.15minutes");
        skuList.add("com.infoicon.push2beat.22minutes");
        skuList.add("com.infoicon.push2beat.30minute");

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
            mHelper.launchPurchaseFlow(getActivity(), threeM_HIT, RC_REQUEST, mPurchaseFinishedListener, payload);
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
}