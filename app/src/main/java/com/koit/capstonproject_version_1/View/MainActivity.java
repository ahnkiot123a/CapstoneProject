package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
import com.koit.capstonproject_version_1.Controller.CreateProductController;
import com.koit.capstonproject_version_1.Controller.ListCategoryController;
import com.koit.capstonproject_version_1.Controller.UserController;
import com.koit.capstonproject_version_1.Model.Category;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.dao.UserDAO;

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
    public void transferToReveneuActivity(View view){
        Intent intent = new Intent(this, RevenueActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
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
    public void intentToSelectProduct(View view){
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

    public void callDraftOrderActivity(View view){
        Intent intent = new Intent(MainActivity.this, DraftOrderActivity.class);
        startActivity(intent);
    }

    public void invoiceHistoryEvent(View view){
        Intent intent = new Intent(this, OrderHistoryActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        userController.logout(this);
    }

//    private void logout() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Bạn có muốn đăng xuất không?");
//        builder.setCancelable(false);
//        builder.setPositiveButton("Thoát", null);
//        builder.setNegativeButton("Đăng xuất", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                SharedPrefs.getInstance().clear();
//                LoginManager.getInstance().logOut();
//                FirebaseAuth.getInstance().signOut();
//                Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
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
        addProductOther();
    }

    private void addProductOther() {
        listCategoryController = new ListCategoryController(this);
        categoryList = new ArrayList<>();
        listCategoryController.getListCategory(this);
        Intent intent = getIntent();
        boolean success = intent.getBooleanExtra(ConvertRateActivity.IS_SUCCESS, false);
        final Product currentProduct = (Product) intent.getSerializableExtra(CreateProductActivity.NEW_PRODUCT);
        if(success){
            createProductController.addImageProduct();
            createProductController.addCategory(currentProduct);

        }
    }

    @Override
    public void onBackPressed() {
        if (navDrawer.isDrawerOpen(GravityCompat.START)) {
            navDrawer.closeDrawers();
        } else {
            userController.logout(this);
        }
    }
}