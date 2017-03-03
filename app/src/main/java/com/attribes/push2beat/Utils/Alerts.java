package com.attribes.push2beat.Utils;

import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Author: Uzair Qureshi
 * Date:  2/27/17.
 * Description:
 */

public class Alerts {
    public static void showError(Context context,String message)
    {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));

        pDialog.setTitleText("Oops...");
        pDialog.setContentText(message);

        //pDialog.setCancelable(false);
        pDialog.show();
    }
    public static void showDialoge(Context context,String message)
    {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));

        pDialog.setTitleText("Alert!");
        pDialog.setContentText(message);

        //pDialog.setCancelable(false);
        pDialog.show();
    }

}
