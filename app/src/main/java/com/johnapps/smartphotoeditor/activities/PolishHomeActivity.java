package com.johnapps.smartphotoeditor.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;
import com.johnapps.smartphotoeditor.R;
import com.johnapps.smartphotoeditor.constants.Constants;
import com.johnapps.smartphotoeditor.constants.SelectorSettings;
import com.johnapps.smartphotoeditor.dialog.DetailsDialog;
import com.johnapps.smartphotoeditor.dialog.RateDialog;
import com.johnapps.smartphotoeditor.picker.ImageCaptureManager;
import com.johnapps.smartphotoeditor.preference.Preference;
import com.johnapps.smartphotoeditor.utils.AdsUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PolishHomeActivity extends PolishBaseActivity {
    private ImageCaptureManager imageCaptureManager;
    private ImageView ImageViewSettings;
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;
    // private final int MANAGE_FILE_ACCESS_REQ_CODE = 6237;
    // GDPR START //
//    ConsentForm form;
    // GDPR END //
    public static Context contextApp;
    //  Helper helper;


    public void onCreate(Bundle bundle) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(bundle);
        MobileAds.initialize(this, (OnInitializationCompleteListener) initializationStatus -> {
        });

        setFullScreen();
        setContentView(R.layout.activity_polish_home);
        contextApp = this.getApplicationContext();
        //    helper = new Helper(PolishHomeActivity.this);

        (findViewById(R.id.layout_edit_photo)).setOnClickListener(this.onClickListener);
        (findViewById(R.id.layout_edit_collage)).setOnClickListener(this.onClickListener);
        (findViewById(R.id.layout_edit_camera)).setOnClickListener(this.onClickListener);
        this.imageCaptureManager = new ImageCaptureManager(this);

        findViewById(R.id.main_settings).setOnClickListener(v -> {
            startActivity(new Intent(this,PolishSettingsActivity.class));
        });
        initInterstitialAd();

    }


    View.OnClickListener onClickListener = view -> {
        switch (view.getId()) {
            case R.id.layout_edit_collage:
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(Objects.requireNonNull(PolishHomeActivity.this));
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            goToCollage();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when fullscreen content failed to show.
                            goToCollage();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null;
                        }
                    });

                } else {
                    goToCollage();
                    initInterstitialAd();
                }
                return;
            case R.id.layout_edit_camera:
                String[] arrPermissionsCamera = {"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                if (Build.VERSION.SDK_INT >= 29)arrPermissionsCamera=new String[]{"android.permission.CAMERA"};
                Dexter.withContext(PolishHomeActivity.this).withPermissions(arrPermissionsCamera)
                        .withListener(new MultiplePermissionsListener() {
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                    home = HomeTools.CAMERA;
                                    try {
                                        ActivityCompat.startActivityForResult(PolishHomeActivity.this, imageCaptureManager.dispatchTakePictureIntent(), 1, null);
                                    } catch (IOException | ActivityNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                                    DetailsDialog.showDetailsDialog(PolishHomeActivity.this);                                }
                            }

                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest>list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).withErrorListener(dexterError ->Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
                return;
            case R.id.layout_edit_photo:

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(Objects.requireNonNull(PolishHomeActivity.this));
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            goToEditPhoto();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when fullscreen content failed to show.
                            goToEditPhoto();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null;
                        }

                    });
                } else {
                    goToEditPhoto();
                    initInterstitialAd();
                }
                return;
            default:
        }

    };

    private static final int REQUEST_CODE = 732;
    public ArrayList<String> mResults = new ArrayList<>();


    private void goToEditPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String[] arrPermissionsGrid = {"android.permission.READ_MEDIA_IMAGES"};
            Dexter.withContext(PolishHomeActivity.this).withPermissions(arrPermissionsGrid).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        home = HomeTools.EDITOR;
                        Intent o = new Intent(PolishHomeActivity.this, PickerActivity.class);
                        o.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1);
                        o.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 1);
                        o.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                        startActivityForResult(o, REQUEST_CODE);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {DetailsDialog.showDetailsDialog(PolishHomeActivity.this);                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest>list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(dexterError ->Toast.makeText(PolishHomeActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();

        } else {
            String[] arrPermissionsGrid = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
            if (Build.VERSION.SDK_INT >= 29) arrPermissionsGrid=new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
            Dexter.withContext(PolishHomeActivity.this).withPermissions(arrPermissionsGrid).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        home = HomeTools.EDITOR;
                        Intent o = new Intent(PolishHomeActivity.this, PickerActivity.class);
                        o.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1);
                        o.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 1);
                        o.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                        startActivityForResult(o, REQUEST_CODE);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {DetailsDialog.showDetailsDialog(PolishHomeActivity.this);}
                }
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest>list, PermissionToken permissionToken) {permissionToken.continuePermissionRequest();}
            }).withErrorListener(dexterError ->Toast.makeText(PolishHomeActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
        }
    }

    private void goToCollage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String[] arrPermissionsGrid = {"android.permission.READ_MEDIA_IMAGES"};
            Dexter.withContext(PolishHomeActivity.this).withPermissions(arrPermissionsGrid).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        home = HomeTools.COLLAGE;
                        Intent o = new Intent(PolishHomeActivity.this, PickerActivity.class);
                        o.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 9);
                        o.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 2);
                        o.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                        startActivityForResult(o, REQUEST_CODE);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {DetailsDialog.showDetailsDialog(PolishHomeActivity.this);                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest>list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(dexterError ->Toast.makeText(PolishHomeActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();

        } else {
            String[] arrPermissionsGrid = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
            if (Build.VERSION.SDK_INT >= 29) arrPermissionsGrid=new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
            Dexter.withContext(PolishHomeActivity.this).withPermissions(arrPermissionsGrid).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        home = HomeTools.COLLAGE;
                        Intent o = new Intent(PolishHomeActivity.this, PickerActivity.class);
                        o.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 9);
                        o.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 2);
                        o.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                        startActivityForResult(o, REQUEST_CODE);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {DetailsDialog.showDetailsDialog(PolishHomeActivity.this);}
                }
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest>list, PermissionToken permissionToken) {permissionToken.continuePermissionRequest();}
            }).withErrorListener(dexterError ->Toast.makeText(PolishHomeActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();

        }
    }

    public void onPostCreate(@Nullable Bundle bundle) {
        super.onPostCreate(bundle);
    }

    HomeTools home = HomeTools.EDITOR;
    public static final String KEY_DATA_RESULT = "KEY_DATA_RESULT";

    enum HomeTools{
        EDITOR, COLLAGE, CAMERA
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (home == HomeTools.COLLAGE){
            if (data != null) {
                ArrayList<String> listExtra = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                this.mResults = listExtra;
                if (listExtra.size() == 0) {
                    // Toast.makeText(this, getResources().getString(R.string.txt_select_picture), Toast.LENGTH_SHORT).show();
                } else if (this.mResults.size() == 1) {
                    Toast.makeText(this, getResources().getString(R.string.no_result_found), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, PolishCollageActivity.class);
                    intent.putStringArrayListExtra(KEY_DATA_RESULT, mResults);
                    startActivity(intent);
                }
            }
        } else if (home == HomeTools.EDITOR){
            if (data != null) {
                ArrayList<String> listExtra = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                this.mResults = listExtra;
                if ((listExtra != null ? listExtra.size() : 0) == 0) {
//                    Toast.makeText(this, getResources().getString(R.string.txt_select_picture), Toast.LENGTH_SHORT).show();
                } else  {
                    Glide.with(this)
                            .asBitmap()
                            .load("file:///" + this.mResults.get(0))
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap bt, @Nullable Transition<? super Bitmap> transition) {
                                    try {
                                        Constants.iBitmap = bt;
                                        Intent o = new Intent(PolishHomeActivity.this, PolishEditorActivity.class);
                                        o.putExtra("done","done");
                                        startActivityForResult(o, 900);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                }
            }
        } else if (home == HomeTools.CAMERA){
            if (this.imageCaptureManager == null) {
                this.imageCaptureManager = new ImageCaptureManager(this);
            }
            new Handler(Looper.getMainLooper()).post(() -> PolishHomeActivity.this.imageCaptureManager.galleryAddPic());
            Intent intent2 = new Intent(getApplicationContext(), PolishEditorActivity.class);
            intent2.putExtra(KEY_DATA_RESULT, this.imageCaptureManager.getCurrentPhotoPath());
            startActivity(intent2);
        }

    }


    public void onResume() {
        super.onResume();
        if (AdsUtils.isNetworkAvailabel(getApplicationContext())) {
            if (!Preference.isRated(getApplicationContext()) && Preference.getCounter(getApplicationContext()) % 6 == 0) {
                new RateDialog(this, false).show();
            }
            Preference.increateCounter(getApplicationContext());
        }
    }

    public void takePhotoFromCamera() {
        try {
            startActivityForResult(this.imageCaptureManager.dispatchTakePictureIntent(), 1);
        } catch (IOException | ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initInterstitialAd() {
        adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(Objects.requireNonNull(this), getResources().getString(R.string.interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error

                mInterstitialAd = null;
            }
        });
    }


    public static Context getAppContext() {
        return contextApp;
    }

}
