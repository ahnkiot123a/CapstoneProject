package com.koit.capstonproject_version_1.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.koit.capstonproject_version_1.Controller.ListCategoryController;
import com.koit.capstonproject_version_1.Model.Category;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UpdateProductActivity extends AppCompatActivity {
    private EditText edProductName, edBarcode, edDescription;
    private Spinner spinnerCategoryName,spinnerUnit;
    private RecyclerView recyclerUnits,recyclerConvertRate;
    private Button btnAddUnits, btnAddQuantity,btnDoneUpdate;
    private ImageButton btnCamera;
    private TextView tvProductQuantity;
    private ListCategoryController listCategoryController;
    private ImageView ivProduct;
    private Switch switchActive;
    private BottomSheetDialog bottomSheetDialog;
    private static final int CAMERA_PER_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    static final int REQUEST_TAKE_PHOTO = 1;
    String currentPhotoPath;
    private Product currentProduct;


    Category category ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        initView();
        setSpinnerCategoryName();
        setProductInformation();
    }

    private void setProductInformation(){
        Intent intent = getIntent();
        currentProduct =(Product) intent.getSerializableExtra("product");
        edBarcode.setText(currentProduct.getBarcode());
        edProductName.setText(currentProduct.getProductName());
        edDescription.setText(currentProduct.getProductDescription());
        if(currentProduct.isActive()) switchActive.setChecked(true);
        setSpinnerUnit(currentProduct.getUnits());
    }
    private void setSpinnerUnit(final List<Unit> unitList){

        ArrayAdapter<Unit> adapter =
                new ArrayAdapter<Unit>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, unitList);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        spinnerUnit.setAdapter(adapter);
        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvProductQuantity.setText(unitList.get(position).getUnitQuantity() + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView() {
        spinnerCategoryName = findViewById(R.id.spinnerCategoryName);
        edProductName = findViewById(R.id.edProductName);
        edBarcode = findViewById(R.id.edBarcode);
        edDescription = findViewById(R.id.edDescription);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        recyclerUnits = findViewById(R.id.recyclerUnits);
        recyclerConvertRate = findViewById(R.id.recyclerConvertRate);
        btnAddUnits = findViewById(R.id.btnAddUnits);
        btnAddQuantity = findViewById(R.id.btnAddQuantity);
        btnDoneUpdate = findViewById(R.id.btnDoneUpdate);
        btnCamera = findViewById(R.id.btnCamera);
        tvProductQuantity = findViewById(R.id.tvProductQuantity);
        ivProduct = findViewById(R.id.ivProduct);
        switchActive = findViewById(R.id.switchActive);

        bottomSheetDialog = new BottomSheetDialog(UpdateProductActivity.this, R.style.BottomSheet);

    }
    private void setSpinnerCategoryName(){
        listCategoryController = new ListCategoryController(this.getApplicationContext());
        listCategoryController.getListCategory(getApplicationContext(),spinnerCategoryName);
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
    private void takeProductPhotoFromAlbum() {
    }

    private void takeProductPhoto() {
    }
    public void cancelTakePhoto() {
        bottomSheetDialog.cancel();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                edBarcode.setText("");
            } else {
                String barcode = intentResult.getContents();
                edBarcode.setText(barcode);
                //controller.fillProduct(barcode, tetProductName, tvCategory);

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