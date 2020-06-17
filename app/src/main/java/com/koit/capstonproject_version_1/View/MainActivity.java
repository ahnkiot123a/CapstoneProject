package com.koit.capstonproject_version_1.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.koit.capstonproject_version_1.Controller.SharedPreferences.SharedPrefs;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;

public class MainActivity extends AppCompatActivity {
    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_debit, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        Intent intent = getIntent();
        currentUser =(User)intent.getSerializableExtra("currentUser");

    }
    public void displayUserInfo(View view){
        Intent intent = new Intent(this, UserInformationActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }

    public void changePassword(android.view.View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }

    public void sendFeedBack(android.view.View view) {
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }

    public void logout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn đăng xuất không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Không", null);
        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LoginManager.getInstance().logOut();
                AccessToken.setCurrentAccessToken(null);
                SharedPrefs.getInstance().clear();
                Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}