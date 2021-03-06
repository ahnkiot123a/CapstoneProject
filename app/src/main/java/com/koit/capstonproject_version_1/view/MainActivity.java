package com.koit.capstonproject_version_1.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.koit.capstonproject_version_1.controller.CreateProductController;
import com.koit.capstonproject_version_1.controller.EditProductQuantityController;
import com.koit.capstonproject_version_1.controller.ListCategoryController;
import com.koit.capstonproject_version_1.controller.OrderHistoryController;
import com.koit.capstonproject_version_1.controller.RandomStringController;
import com.koit.capstonproject_version_1.controller.UserController;
import com.koit.capstonproject_version_1.model.Category;
import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.helper.StatusBar;
import com.koit.capstonproject_version_1.model.Unit;
import com.koit.capstonproject_version_1.model.User;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.model.dao.CreateProductDAO;
import com.koit.capstonproject_version_1.model.dao.UserDAO;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout navDrawer;
    private TextView tvNameProfileLeft;
    private TextView tvEmailProfileLeft;

    private ListCategoryController listCategoryController;
    private List<Category> categoryList;
    private User currentUser;
    private CreateProductController createProductController;
    private UserController userController;
    private OrderHistoryController orderHistoryController;
    private boolean isConnected = false;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        currentUser = UserDAO.getInstance().getUser();
        getBottomNavigation();
        getNavigationMenuLeft();
        createProductController = new CreateProductController(this);
        userController = new UserController();
        orderHistoryController = new OrderHistoryController(this);
//        orderHistoryController.deleteDraftOrderBefore3Days();
//        add1000Products();

    }

    private void add1000Products() {
        EditProductQuantityController editProductQuantityController = new EditProductQuantityController();
        CreateProductDAO createProductDAO = new CreateProductDAO();
        for (int i = 1; i< 1000;i++){
            Product product = new Product();
            String productID = RandomStringController.getInstance().randomString();
            product.setProductId(productID);
            product.setProductImageUrl("JPEG_20200904_110102_1764192228597077254.jpg");
            product.setProductName("sản phẩm " + i);
            List<Unit> units = new ArrayList<>();
            units.add(new Unit("1", "đơn vị 1",1, 15000, 15 ));
            product.setUnits(units);
            editProductQuantityController.addUnitsToFireBase(product,units);
            createProductDAO.addProductInFirebase(product);
        }
    }


    private void getNavigationMenuLeft() {
        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.inflateHeaderView(R.layout.navigation_left_menu);
        tvNameProfileLeft = headerView.findViewById(R.id.tvNameProfileLeft);
        tvEmailProfileLeft = headerView.findViewById(R.id.tvEmailLeft);
        navDrawer = findViewById(R.id.drawer_layout);


        if (currentUser != null) {
            String name = !currentUser.getFullName().isEmpty() ? currentUser.getFullName() : currentUser.getStoreName();
            tvNameProfileLeft.setText(name);
            String text = !currentUser.getPhoneNumber().isEmpty() ? currentUser.getPhoneNumber() : currentUser.getEmail();
            tvEmailProfileLeft.setText(text);
        }
    }

    private void getBottomNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_debit, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void displayUserInfo(View view) {
        Intent intent = new Intent(this, UserInformationActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    public void changePassword(android.view.View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    public void transferToReveneuActivity(View view) {
        Intent intent = new Intent(this, RevenueActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    public void sendFeedBack(android.view.View view) {
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    public void navigationMenuLeft(View view) {
        // If the navigation drawer is not open then open it, if its already open then close it.
        if (!navDrawer.isDrawerOpen(GravityCompat.START)) navDrawer.openDrawer(GravityCompat.START);
    }

    public void intentToSelectProduct(View view) {
        Intent intent = new Intent(MainActivity.this, SelectProductActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    public void callCreateProductActivity(View view) {
        Intent intent = new Intent(MainActivity.this, CreateProductActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void callListProductActivity(View view) {
        Intent intent = new Intent(MainActivity.this, ListProductActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void callDraftOrderActivity(View view) {
        Intent intent = new Intent(MainActivity.this, DraftOrderActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void callOrderHistory(View view) {
        Intent intent = new Intent(this, OrderHistoryActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    public void logout(View view) {
        userController.logout(this);
    }

    public void closeNavigationLeft(View view) {
        navDrawer.closeDrawers();
    }

    public void getUpdateActivity(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tính năng đang được cập nhật.Quý khách vui lòng quay lại sau. Xin cảm ơn!");
        builder.setCancelable(false);
        builder.setPositiveButton("Đồng ý", null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        addProductOther();
    }

    private void addProductOther() {
        listCategoryController = new ListCategoryController(this);
        categoryList = new ArrayList<>();
        listCategoryController.getListCategory(this);
        Intent intent = getIntent();
        boolean success = intent.getBooleanExtra(ConvertRateActivity.IS_SUCCESS, false);
        final Product currentProduct = (Product) intent.getSerializableExtra(CreateProductActivity.NEW_PRODUCT);
        if (success) {
//            createProductController.addImageProduct();
            createProductController.addCategory(currentProduct);
        }
    }
    public void sentToListProduct(View view){
        Intent intent = new Intent(this, ListProductActivity.class);
        Bundle args2 = new Bundle();
        args2.putBoolean("isFromHomeFragment", true);
        intent.putExtra("BUNDLE", args2);
//                    Pair[] pairs = new Pair[1];
//                    pairs[0] = new Pair<View, String>(getlayoutSearch(), "layoutSearch");
//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        if (isConnected) {
            super.onBackPressed();
            if (navDrawer.isDrawerOpen(GravityCompat.START)) {
                navDrawer.closeDrawers();
            }
        }
    }

    public void ScanBarcodeFromHome(View view) {
        Toast.makeText(this, "scan barcode click", Toast.LENGTH_LONG).show();
    }

    public void SearchFromHome(View view) {
        Toast.makeText(this, "search click", Toast.LENGTH_LONG).show();
    }
    //    @Override
//    public void onBackPressed() {
//
//        if (isConnected) {
//            if (navDrawer.isDrawerOpen(GravityCompat.START)) {
//                navDrawer.closeDrawers();
//            }
//        }
////        else {
////            userController.logout(this);
////        }
//    }
}