package com.koit.capstonproject_version_1.View;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.koit.capstonproject_version_1.Controller.CreateProductController;
import com.koit.capstonproject_version_1.Controller.ListCategoryController;
import com.koit.capstonproject_version_1.Controller.UserController;
import com.koit.capstonproject_version_1.Model.Category;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.MyDialog;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.dao.UserDAO;
import com.koit.capstonproject_version_1.helper.CustomToast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout navDrawer;
    private TextView tvNameProfileLeft;
    private TextView tvEmailProfileLeft;

    private ListCategoryController listCategoryController;
    private List<Category> categoryList;
    private User currentUser;
    private CreateProductController createProductController;
    private UserController userController;
    MyDialog dialog;

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
        dialog = new MyDialog(this);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onResume() {
        super.onResume();
        ReactiveNetwork.observeNetworkConnectivity(getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(connectivity -> {
                    if (connectivity.available()) {
                        if (dialog != null) dialog.dismissDialog();
                    } else {
                        dialog.showInternetError();
                    }
                });
    }

    private void getNavigationMenuLeft() {
        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.inflateHeaderView(R.layout.navigation_left_menu);
        tvNameProfileLeft = headerView.findViewById(R.id.tvNameProfileLeft);
        tvEmailProfileLeft = headerView.findViewById(R.id.tvEmailLeft);
        navDrawer = findViewById(R.id.drawer_layout);


        if (currentUser != null) {
            tvNameProfileLeft.setText(currentUser.getFullName());
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
    }

    public void changePassword(android.view.View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
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
    }

    public void navigationMenuLeft(View view) {
        // If the navigation drawer is not open then open it, if its already open then close it.
        if (!navDrawer.isDrawerOpen(GravityCompat.START)) navDrawer.openDrawer(GravityCompat.START);
    }

    public void intentToSelectProduct(View view) {
        Intent intent = new Intent(MainActivity.this, SelectProductActivity.class);
        startActivity(intent);
    }

    public void callCreateProductActivity(View view) {
        Intent intent = new Intent(MainActivity.this, CreateProductActivity.class);
        startActivity(intent);
    }

    public void callListProductActivity(View view) {
        Intent intent = new Intent(MainActivity.this, ListProductActivity.class);
        startActivity(intent);
    }

    public void callDraftOrderActivity(View view) {
        Intent intent = new Intent(MainActivity.this, DraftOrderActivity.class);
        startActivity(intent);
    }

    public void callOrderHistory(View view) {
        Intent intent = new Intent(this, OrderHistoryActivity.class);
        startActivity(intent);
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

    @Override
    public void onBackPressed() {
        if (navDrawer.isDrawerOpen(GravityCompat.START)) {
            navDrawer.closeDrawers();
        }
//        else {
//            userController.logout(this);
//        }
    }
}