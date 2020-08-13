package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Controller.ListCategoryController;
import com.koit.capstonproject_version_1.Model.Category;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.dao.CategoryDAO;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    public static final String CATEGORY_DATA = "CATEGORY_DATA";

    private ListView lvCategory;
    private Button btnAddNewCategory, btnCancelAddNewCategory;
    private EditText edAddNewCategory;
    private List<Category> categoryList;
    private ListCategoryController listCategoryController;
    private UserDAO userDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_activy);

        initView();
        userDAO = new UserDAO();
        listCategoryController = new ListCategoryController(this);
        listCategoryController.getListCategory(this, lvCategory);
        //  CategoryDAO.getInstance().getListCategory(this, lvCategory);
        getCategoryListView();
      /*  (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {

                categoryList = listCategoryController.getCategories();

            }
        }, 1000);*/


    }

    private void initView() {
        lvCategory = findViewById(R.id.lvCategory);
    }

    //lấy category mà người dùng chọn rồi chuyền sang CreateProduct
    private void getCategoryListView() {
        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getAdapter().getItem(position) + "";
                Log.i("category", category);
                Intent intent = new Intent();
                intent.putExtra(CATEGORY_DATA, category);
                // Đặt resultCode là Activity.RESULT_OK to
                // thể hiện đã thành công và có chứa kết quả trả về
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    public void addNewCategory(final View view) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custome_add_new_category_dialog, null);

        btnAddNewCategory = dialogView.findViewById(R.id.btnAddNewCategory);
        btnCancelAddNewCategory = dialogView.findViewById(R.id.btnCancelAddNewCategory);
        edAddNewCategory = dialogView.findViewById(R.id.edAddNewCategory);


        btnAddNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*(new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String categoryName = edAddNewCategory.getText().toString().trim();
                        categoryList = listCategoryController.getCategories();
                        boolean flag = true;
                        for (int i = 0; i < categoryList.size(); i++) {
                            if (categoryName.equals(categoryList.get(i).getCategoryName()))
                                flag = false;
                        }
                        if (flag) {
                            Category category = new Category(categoryName);
                          //  category.addCategoryToFireBase(userDAO.getUserID());
                            Intent intent = new Intent();
                            intent.putExtra(CATEGORY_DATA, categoryName);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        } else {
                            Toast.makeText(CategoryActivity.this, "Loại sản phẩm đã có trong hệ thống", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1000);
                */
                String categoryName = edAddNewCategory.getText().toString().trim();

                Intent intent = new Intent();
                intent.putExtra(CATEGORY_DATA, categoryName);
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });
        btnCancelAddNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.cancel();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    public void back(View v) {
        onBackPressed();
    }

}