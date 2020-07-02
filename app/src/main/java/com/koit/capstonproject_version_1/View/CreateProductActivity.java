package com.koit.capstonproject_version_1.View;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.koit.capstonproject_version_1.Controller.CreateProductController;
import com.koit.capstonproject_version_1.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateProductActivity extends AppCompatActivity {

    private static final int CAMERA_PER_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    static final int REQUEST_TAKE_PHOTO = 1;

    private TextInputEditText etBarcode;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private CreateProductController controller;
    private TextInputEditText tetProductName, tetDescription;
    private TextView tvCategory;
    private BottomSheetDialog bottomSheetDialog;
    private ImageView ivProduct;
    String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        //init activity
        initView();
        controller = new CreateProductController();
        bottomSheetDialog = new BottomSheetDialog(CreateProductActivity.this, R.style.BottomSheet);

        tvToolbarTitle.setText("Thêm sản phẩm");
    }

    private void initView() {
        //find view by id
        etBarcode = findViewById(R.id.etBarcode);
        toolbar = findViewById(R.id.toolbarGeneral);
        tetProductName = findViewById(R.id.etProductName);
        tetDescription = findViewById(R.id.etDescription);
        tvCategory = findViewById(R.id.tvCategory);
        tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        ivProduct = findViewById(R.id.ivProduct);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //set default view
        tetProductName.setText("");
        tvCategory.setText("");

        bottomSheetDialog.dismiss();
    }

    public void scanCustomScanner(View view) {
        //set permission camera
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(true);
        integrator.setCaptureActivity(CustomScreenScanActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Đặt mã vạch vào trong khung để quét.");
//        integrator.setTimeout(10000);
        integrator.initiateScan();
    }

    public void showPhotoDialog(View view) {

        View bottomSheet = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.bottom_sheet_layout_take_photo, (LinearLayout) findViewById(R.id.bottomSheetCont));

        bottomSheet.findViewById(R.id.btnTakePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
                takeProductPhoto();
            }
        });

        bottomSheet.findViewById(R.id.btnTakeFromAlbum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeProductPhotoFromAlbum();
            }
        });

        bottomSheet.findViewById(R.id.btnCancelPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTakePhoto();
            }
        });
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PER_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }

    private void takeProductPhotoFromAlbum() {
    }

    private void takeProductPhoto() {
    }

    public void cancelTakePhoto() {
        bottomSheetDialog.cancel();
    }

    public void back(View view) {
        onBackPressed();
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.koit.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                etBarcode.setText("");
            } else {
                String barcode = intentResult.getContents();
                etBarcode.setText(barcode);
                controller.fillProduct(barcode, tetProductName, tvCategory);

            }
        }
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.i("image", currentPhotoPath);
            File file = new File(currentPhotoPath);
            ivProduct.setImageURI(Uri.fromFile(file));
            ivProduct.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PER_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //open camera
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền này để sử dụng chức năng chụp ảnh", Toast.LENGTH_LONG).show();
            }
        }
    }
}