package com.koit.capstonproject_version_1.View;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.koit.capstonproject_version_1.R;

public class CustomScreenScanActivity extends Activity implements DecoratedBarcodeView.TorchListener {

    private CaptureManager captureManager;
    private DecoratedBarcodeView barcodeView;
    private Button btnSwitchFlashlight;
    private Button btnContinuousFlashlight;
    private TextView tvPaused, tvMessagePause, tvMessageFlashOn;
    private boolean isPause;
    private boolean isFlashlightOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_screen_scan);

        findViewByID();
        barcodeView.setTorchListener(this);
        btnSwitchFlashlight.setText("Bật đèn flash");
        btnContinuousFlashlight.setText("Tạm dừng");
        tvPaused.setVisibility(View.INVISIBLE);
        tvMessagePause.setVisibility(View.INVISIBLE);
        tvMessageFlashOn.setVisibility(View.INVISIBLE);
        isPause = false;
        isFlashlightOn = false;
        if (!hasFlash()) {
            btnSwitchFlashlight.setVisibility(View.GONE);
        }
        captureManager = new CaptureManager(this, barcodeView);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();
    }

    private void findViewByID() {
        barcodeView = findViewById(R.id.zxing_barcode_scanner);
        btnSwitchFlashlight = findViewById(R.id.btnSwitchFlash);
        btnContinuousFlashlight = findViewById(R.id.btnContinuousFlash);
        tvPaused = findViewById(R.id.tvPaused);
        tvMessagePause = findViewById(R.id.tvMessagePause);
        tvMessageFlashOn = findViewById(R.id.tvMessageFlashOn);
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    private boolean hasFlash() {
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashLight(View view) {
        if ("Bật đèn flash".equalsIgnoreCase(btnSwitchFlashlight.getText().toString())) {
            barcodeView.setTorchOn();
            tvMessageFlashOn.setVisibility(View.VISIBLE);
            isFlashlightOn = true;
        } else {
            barcodeView.setTorchOff();
            tvMessageFlashOn.setVisibility(View.INVISIBLE);
            isFlashlightOn = false;
        }

    }

    public void continuousFlashLight(View view) {
        if (!isPause) {
            barcodeView.pause();
            btnContinuousFlashlight.setText("Tiếp tục");
            isPause = true;
            btnSwitchFlashlight.setVisibility(View.INVISIBLE);
            tvPaused.setVisibility(View.VISIBLE);
            tvMessagePause.setVisibility(View.VISIBLE);
            tvMessageFlashOn.setVisibility(View.INVISIBLE);
        } else {
            barcodeView.resume();
            btnContinuousFlashlight.setText("Tạm dừng");
            isPause = false;
            btnSwitchFlashlight.setVisibility(View.VISIBLE);
            tvPaused.setVisibility(View.INVISIBLE);
            tvMessagePause.setVisibility(View.INVISIBLE);
            if (isFlashlightOn) {
                tvMessageFlashOn.setVisibility(View.VISIBLE);
            } else {
                tvMessageFlashOn.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onTorchOn() {
        btnSwitchFlashlight.setText("Tắt đèn flash");
    }

    @Override
    public void onTorchOff() {
        btnSwitchFlashlight.setText("Bật đèn flash");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("ResultBarcodeCSC", grantResults.toString());
    }
}
