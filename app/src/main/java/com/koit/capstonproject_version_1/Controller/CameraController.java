package com.koit.capstonproject_version_1.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.zxing.integration.android.IntentIntegrator;
import com.koit.capstonproject_version_1.View.CreateProductActivity;
import com.koit.capstonproject_version_1.View.CustomScreenScanActivity;
import com.koit.capstonproject_version_1.View.ListItemInOrderActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraController {

    private Activity activity;
    private String currentPhotoPath = "";

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    public CameraController(Activity activity) {
        this.activity = activity;
    }

    public void askCameraPermission(int feature) {

        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, feature);
        } else {
            switch (feature) {
                case CreateProductActivity.BARCODE_PER_CODE:
                    scanBarcode();
                    break;

                case CreateProductActivity.TAKE_PHOTO_PER_CODE:
                    captureImage();
                    break;
            }
        }
    }

    public void scanBarcode() {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setOrientationLocked(true);
        integrator.setCaptureActivity(CustomScreenScanActivity.class);
//        integrator.setCaptureActivity(ListItemInOrderActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Đặt mã vạch vào trong khung để quét.");
//        integrator.setTimeout(10000);
        integrator.initiateScan();
    }

    public void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity.getApplicationContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, CreateProductActivity.CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        Log.i("imageName", imageFileName + storageDir);
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Log.i("path", currentPhotoPath);
        return image;
    }

}
