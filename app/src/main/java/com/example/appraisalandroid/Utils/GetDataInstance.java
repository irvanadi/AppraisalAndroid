package com.example.appraisalandroid.Utils;

import android.net.Uri;
import android.os.Environment;

public class GetDataInstance {

    public String getFilePathfromUri(Uri uri){
        String filepath = uri.getPath();
        String filepath1[] = filepath.split(":");
        return Environment.getExternalStorageDirectory().getPath()+"/"+filepath1[1];
    }
}
