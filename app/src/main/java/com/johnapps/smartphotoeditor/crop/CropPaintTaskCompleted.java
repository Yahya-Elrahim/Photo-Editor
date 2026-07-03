package com.johnapps.smartphotoeditor.crop;

import android.graphics.Bitmap;


interface CropPaintTaskCompleted {
    void onTaskCompleted(Bitmap bitmap, double d);
}
