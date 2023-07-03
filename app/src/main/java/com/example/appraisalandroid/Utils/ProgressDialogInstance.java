package com.example.appraisalandroid.Utils;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogInstance {

    private static ProgressDialog progressDialog;

    /**
     * Create an progaress dialog instance
     * @param Context context
     * @Title title of String
     *
     **/
    public static ProgressDialog createProgressDialoge(Context context, String title) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(title);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return progressDialog;
    }

    /**
     * It dimiss progress dialoge if already visible
     *
     * */
    public static void dissmisProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    /**
     * Show a progress dialoge
     */

    public static void showProgress() {
        if (progressDialog != null)
            progressDialog.show();
    }
}
