package com.koit.capstonproject_version_1.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.koit.capstonproject_version_1.R;

public class CreateProductActivity extends AppCompatActivity {

    private TextInputEditText etBarcode;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        //init activity
        initView();
        tvToolbarTitle.setText("Thêm sản phẩm");
    }

    private void initView() {
        etBarcode = findViewById(R.id.etBarcode);
        toolbar = findViewById(R.id.toolbarGeneral);
        tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
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

    public void showPhotoDialog(View view){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(CreateProductActivity.this, R.style.BottomSheet);
        View bottomSheet = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.bottom_sheet_layout_take_photo, (LinearLayout) findViewById(R.id.bottomSheetCont));
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
    }

    public void back(View view){
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                etBarcode.setText("");
            } else {
                etBarcode.setText(intentResult.getContents());
            }
        }
    }
}