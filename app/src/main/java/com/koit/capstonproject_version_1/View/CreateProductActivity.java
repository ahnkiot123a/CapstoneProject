package com.koit.capstonproject_version_1.View;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.koit.capstonproject_version_1.Adapter.CreateUnitAdapter;
import com.koit.capstonproject_version_1.Controller.CameraController;
import com.koit.capstonproject_version_1.Controller.CreateProductController;
import com.koit.capstonproject_version_1.Controller.ListCategoryController;
import com.koit.capstonproject_version_1.Model.Category;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.LinearLayoutManagerWrapper;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateProductActivity extends AppCompatActivity {

    public static final int BARCODE_PER_CODE = 101;
    public static final int TAKE_PHOTO_PER_CODE = 102;
    public static final int CAMERA_REQUEST_CODE = 103;
    public static final int GALLERY_REQ_CODE = 104;
    public static  final int REQUEST_CATEGORY_CODE = 2;
    public static final String NEW_PRODUCT = "PRODUCT";

    public static String photoName;
    public static Uri photoUri;

    private TextInputEditText etBarcode;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private CreateProductController controller;
    private TextInputEditText tetProductName, tetDescription;
    private TextView tvCategory;
    private BottomSheetDialog bottomSheetDialog;
    private ImageView ivProduct;
    private static RecyclerView recyclerCreateUnit;
    private Switch switchActive;

    private ArrayList<Integer> listUnit;
    private CameraController cameraController;
    private static CreateUnitAdapter createUnitAdapter;

    private ListView lvCategory;
    private ListCategoryController listCategoryController;
    private List<Category> categoryList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        //init activity
        initView();
        controller = new CreateProductController(CreateProductActivity.this);
        bottomSheetDialog = new BottomSheetDialog(CreateProductActivity.this, R.style.BottomSheet);

        cameraController = new CameraController(this);

        tvToolbarTitle.setText("Thêm sản phẩm");

        //create list in recyclerview
        createListRecyclerview();

        //build recyclerview unit
        buildRvUnit();
        /*listCategoryController = new ListCategoryController(this);
        listCategoryController.getListCategory(this);
        //  CategoryDAO.getInstance().getListCategory(this, lvCategory);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {

                categoryList = listCategoryController.getCategories();
                for (Category category : categoryList){
                    Log.i("kiemtraCategory",category.getCategoryName());
                }

            }
        }, 5000);*/


    }

    private void buildRvUnit() {
        recyclerCreateUnit.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false);
        recyclerCreateUnit.setLayoutManager(linearLayoutManager);
        createUnitAdapter = new CreateUnitAdapter(this, listUnit);
        recyclerCreateUnit.setAdapter(createUnitAdapter);

        createUnitAdapter.setOnItemClickLister(new CreateUnitAdapter.OnItemClickLister() {
            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }

    private void createListRecyclerview() {
        listUnit = new ArrayList<>();
        listUnit.add(1);
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
        recyclerCreateUnit = findViewById(R.id.recyclerCreateUnit);
        switchActive = findViewById(R.id.switchActive);
        lvCategory = findViewById(R.id.lvCategory);


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

    //event click thêm đơn vị button
    public void addUnitRv(View view) {
        int position = listUnit.size() + 1;
        insertItem(position);
    }

    //insert one unit item in recycler view
    private void insertItem(int position) {
        listUnit.add(position);
        createUnitAdapter.notifyDataSetChanged();
    }

    //remove one unit item in recycler view
    private void removeItem(int position) {
        listUnit.remove(position);
        createUnitAdapter.notifyDataSetChanged();
    }

    //create product
    public void addProduct(View view) {
        controller.createProduct(etBarcode, tetProductName, tetDescription, tvCategory,switchActive.isChecked());

    }

    public static ArrayList<Unit> getUnitFromRv(){
        ArrayList<Unit> list = new ArrayList<>();
        for (int i = 0; i < createUnitAdapter.getItemCount(); i++) {
            CreateUnitAdapter.ViewHolder viewHolder = (CreateUnitAdapter.ViewHolder) recyclerCreateUnit.findViewHolderForAdapterPosition(i);
            String unitName = viewHolder.getEtUnitName().getText().toString().trim();
            String unitPrice = viewHolder.getEtUnitPrice().getText().toString().trim();
            Log.i("price", unitPrice);
            if(!unitName.isEmpty() && !unitPrice.isEmpty()){
                Unit unit = new Unit();
                unit.setUnitName(unitName);
                unit.setUnitPrice(Long.parseLong(unitPrice));
                unit.setConvertRate(1);
                list.add(unit);
            }
        }
        Log.i("listUnit", list.toString());
        return list;
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

    public void categoryAction(View view){
        Intent intent = new Intent(CreateProductActivity.this, CategoryActivity.class);
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
                controller.fillProduct(barcode, tetProductName, tvCategory);

            }
        }
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.i("image", cameraController.getCurrentPhotoPath());
            String photoPath = cameraController.getCurrentPhotoPath();
            File file = new File(photoPath);
            Uri uri = Uri.fromFile(file);
            ivProduct.setImageURI(uri);
            ivProduct.setRotation(ivProduct.getRotation() + 90);
            photoName = file.getName();
            Log.i("photoName", photoName);
            photoUri = uri;


        }

        if (requestCode == GALLERY_REQ_CODE && resultCode == Activity.RESULT_OK) {
            Uri contentUri = data.getData();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imgFileName = "JPEG" + "_"  + timeStamp + "." + getFileExt(contentUri);
            Log.i("gallery", "onActivityResult: Gallery Image Uri " + imgFileName);
            photoName = imgFileName;
            ivProduct.setImageURI(contentUri);
            Log.i("contentUri", contentUri.toString());
            ivProduct.setRotation(ivProduct.getRotation() + 180);
            photoUri = contentUri;
        }

        if(requestCode == REQUEST_CATEGORY_CODE && resultCode == Activity.RESULT_OK){
            String category = data.getStringExtra(CategoryActivity.CATEGORY_DATA);
            Log.d("category", category);
            tvCategory.setText(category);
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