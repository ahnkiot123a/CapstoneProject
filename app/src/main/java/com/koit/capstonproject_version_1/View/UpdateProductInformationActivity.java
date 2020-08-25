package com.koit.capstonproject_version_1.View;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.koit.capstonproject_version_1.Controller.CameraController;
import com.koit.capstonproject_version_1.Controller.CreateProductController;
import com.koit.capstonproject_version_1.Controller.DetailProductController;
import com.koit.capstonproject_version_1.Controller.UpdateProductController;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UpdateProductInformationActivity extends AppCompatActivity {

    public static final int BARCODE_PER_CODE = 101;
    public static final int TAKE_PHOTO_PER_CODE = 102;
    public static final int CAMERA_REQUEST_CODE = 103;
    public static final int GALLERY_REQ_CODE = 104;
    public static final int REQUEST_CATEGORY_CODE = 2;
    public static final int REQUEST_QUANTITY_CODE = 3;
    public static final int REQUEST_UNIT_CODE = 4;
    public static final int REQUEST_CONVERT_RATE_CODE = 5;

    public static String photoName;
    public static Uri photoUri;

    private static final String BARCODE = "barcode";
    private static final String TAKE_PHOTO = "take_photo";

    private TextInputEditText etBarcode;
    private Toolbar toolbar;
    private TextView tvToolbarTitle, tvConvertRate, tvUnitQuantity;
    private CreateProductController controller;
    private TextInputEditText tetProductName, tetDescription;
    private LinearLayout linearConvertRate;
    private TextView tvCategory;
    private Button btnEditUnits, btnEditConvertRate, btnAddProductQuantiy, btnUpdateProduct;
    private BottomSheetDialog bottomSheetDialog;
    private ImageView ivProduct;
    private RecyclerView recyclerUnits, recyclerConvertRate;
    private ArrayList<Unit> listUnit;
    private CameraController cameraController;
    private Product currentProduct;
    private Switch switchActive;
    private Spinner spinnerUnit;
    private DetailProductController detailProductController;

    private UpdateProductController updateProductController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_update_product_information);

        //init activity
        initView();
        controller = new CreateProductController();
        bottomSheetDialog = new BottomSheetDialog(UpdateProductInformationActivity.this, R.style.BottomSheet);

        detailProductController = new DetailProductController(this);
        cameraController = new CameraController(this);
        updateProductController = new UpdateProductController(this);
        tvToolbarTitle.setText("Cập nhật sản phẩm");

        //build recyclerview unit
        // buildRvUnit();
        setProductInformation();
        actionBtnEditUnits();
        actionBtnAddProductQuantiy();
        actionBtnEditConvertRate();
        actionBtnUpdateProduct();

    }

    private void actionBtnUpdateProduct() {
        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProductController.updateProduct(etBarcode,tetProductName,tetDescription,tvCategory,switchActive,currentProduct);
            }
        });
    }

    private void actionBtnEditConvertRate() {
        btnEditConvertRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProductInformationActivity.this, EditConvertRateActivity.class);
                intent.putExtra("product", currentProduct);
                startActivityForResult(intent, REQUEST_CONVERT_RATE_CODE);
            }
        });
    }

    private void actionBtnAddProductQuantiy() {
        btnAddProductQuantiy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProductInformationActivity.this, EditProductQuantityActivity.class);
                intent.putExtra("product", currentProduct);
                startActivityForResult(intent, REQUEST_QUANTITY_CODE);
            }
        });
    }

    private void actionBtnEditUnits() {
        btnEditUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProductInformationActivity.this, EditProductUnitsActivity.class);
                intent.putExtra("product", currentProduct);
                startActivityForResult(intent, REQUEST_UNIT_CODE);
            }
        });
    }

    private void setProductInformation() {
        Intent intent = getIntent();
        currentProduct = (Product) intent.getSerializableExtra("product");
        listUnit = (ArrayList<Unit>) currentProduct.getUnits();
        etBarcode.setText(currentProduct.getBarcode());
        tetProductName.setText(currentProduct.getProductName());
        tetDescription.setText(currentProduct.getProductDescription());
        if (currentProduct.isActive()) switchActive.setChecked(true);
        detailProductController.setProductImageView(ivProduct, currentProduct);
        tvCategory.setText(currentProduct.getCategoryName());

        photoName = currentProduct.getProductImageUrl();
        setRecyclerUnits();
        setRecyclerConvertRate();
        setSpinnerUnit();
    }

    private void setRecyclerUnits() {
        detailProductController.setRecyclerViewUnits(recyclerUnits,listUnit);
    }

    private void setRecyclerConvertRate() {
        detailProductController.setRecyclerConvertRate(listUnit,linearConvertRate,recyclerConvertRate);
    }

    private void setSpinnerUnit() {
        detailProductController.setSpinnerUnit(listUnit,spinnerUnit,tvUnitQuantity);
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
        btnEditUnits = findViewById(R.id.btnEditUnits);
        recyclerUnits = findViewById(R.id.recyclerUnits);
        switchActive = findViewById(R.id.switchActive);
        tvConvertRate = findViewById(R.id.tvConvertRate);
        btnEditConvertRate = findViewById(R.id.btnEditConvertRate);
        recyclerConvertRate = findViewById(R.id.recyclerConvertRate);
        btnAddProductQuantiy = findViewById(R.id.btnAddProductQuantiy);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        tvUnitQuantity = findViewById(R.id.tvUnitQuantity);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        linearConvertRate = findViewById(R.id.linearConvertRate);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //set default view
        bottomSheetDialog.dismiss();
    }

    //event click camera scan
    public void scanCustomScanner(View view) {
        cameraController.askCameraPermission(BARCODE_PER_CODE);
    }

    public void showPhotoDialog(View view) {

        View bottomSheet = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.bottom_sheet_layout_take_photo, (LinearLayout) findViewById(R.id.bottomSheetCont));

        bottomSheet.findViewById(R.id.btnTakePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraController.askCameraPermission(TAKE_PHOTO_PER_CODE);
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

    private void takeProductPhotoFromAlbum() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQ_CODE);
    }

    public void cancelTakePhoto() {
        bottomSheetDialog.cancel();
    }

    public void back(View view) {
        onBackPressed();
    }

    public void categoryAction(View view) {
        Intent intent = new Intent(UpdateProductInformationActivity.this, CategoryActivity.class);
        startActivityForResult(intent, REQUEST_CATEGORY_CODE);
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
                controller.checkExistedBarcode(barcode, tetProductName, tvCategory, etBarcode);

            }
        }
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            File file = new File(cameraController.getCurrentPhotoPath());
            ivProduct.setImageURI(Uri.fromFile(file));
            ivProduct.setRotation(ivProduct.getRotation() + 90);
            Uri uri = Uri.fromFile(file);
            photoName = file.getName();
            photoUri = uri;
        }

        if (requestCode == GALLERY_REQ_CODE && resultCode == Activity.RESULT_OK) {
            Uri contentUri = data.getData();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imgFileName = "JPEG" + "_" + timeStamp + "." + getFileExt(contentUri);
            Log.i("gallery", "onActivityResult: Gallery Image Uri " + imgFileName);
            ivProduct.setImageURI(contentUri);
            ivProduct.setRotation(ivProduct.getRotation() + 180);
            photoName = imgFileName;
            Log.i("photoNameGallery", photoName);
            photoUri = contentUri;
        }

        if (requestCode == REQUEST_CATEGORY_CODE && resultCode == Activity.RESULT_OK) {
            String category = data.getStringExtra(CategoryActivity.CATEGORY_DATA);
            Log.d("category", category);
            tvCategory.setText(category);
        }

        if (requestCode == REQUEST_QUANTITY_CODE && resultCode == Activity.RESULT_OK) {
            currentProduct = (Product) data.getSerializableExtra("product");
            listUnit = (ArrayList<Unit>) currentProduct.getUnits();
            setSpinnerUnit();
        }
        if (requestCode == REQUEST_UNIT_CODE && resultCode == Activity.RESULT_OK) {
            currentProduct = (Product) data.getSerializableExtra("product");
            listUnit = (ArrayList<Unit>) currentProduct.getUnits();
            setRecyclerUnits();
            setRecyclerConvertRate();
            setSpinnerUnit();
        }
        if (requestCode == REQUEST_CONVERT_RATE_CODE && resultCode == Activity.RESULT_OK) {
            currentProduct = (Product) data.getSerializableExtra("product");
            listUnit = (ArrayList<Unit>) currentProduct.getUnits();
            setRecyclerConvertRate();
            setSpinnerUnit();
        }
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(contentUri));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == TAKE_PHOTO_PER_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraController.captureImage();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền này để sử dụng chức năng chụp ảnh", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == BARCODE_PER_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraController.scanBarcode();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền này để sử dụng chức năng quét mã vạch", Toast.LENGTH_SHORT).show();
            }
        }
    }
}