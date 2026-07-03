package com.johnapps.smartphotoeditor.picker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageCaptureManager {
    Context mContext;
    private String mCurrentPhotoPath;

    public ImageCaptureManager(Context context) {
        this.mContext = context;
    }

    public File createImageFile() throws IOException {
        String str = "JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()) + ".jpg";
        File files = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (files != null && (files.exists() || files.mkdir())) {
            File file = new File(files, str);
            this.mCurrentPhotoPath = file.getAbsolutePath();
            return file;
        }
        Log.e("TAG", "Throwing Errors....");
        throw new IOException();
    }

    public Intent dispatchTakePictureIntent() throws IOException {
        Uri uri;
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (intent.resolveActivity(this.mContext.getPackageManager()) != null) {
            File createImageFile = createImageFile();
            uri = FileProvider.getUriForFile(this.mContext.getApplicationContext(), this.mContext.getApplicationInfo().packageName + ".provider", createImageFile);
            if (uri != null) {
                intent.putExtra("output", uri);
            }
        }
        return intent;
    }


    public void galleryAddPic() {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        if (!TextUtils.isEmpty(this.mCurrentPhotoPath)) {
            intent.setData(Uri.fromFile(new File(this.mCurrentPhotoPath)));
            this.mContext.sendBroadcast(intent);
        }
    }

    public String getCurrentPhotoPath() {
        return this.mCurrentPhotoPath;
    }
}
