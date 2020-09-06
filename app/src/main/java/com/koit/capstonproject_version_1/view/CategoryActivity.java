package com.koit.capstonproject_version_1.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.koit.capstonproject_version_1.controller.ListCategoryController;
import com.koit.capstonproject_version_1.model.Category;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.model.dao.UserDAO;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    public static final String CATEGORY_DATA = "CATEGORY_DATA";

    private ListView lvCategory;
    private Button btnAddNewCategory, btnCancelAddNewCategory;
    private EditText edAddNewCategory;
    private List<Category> categoryList;
    private ListCategoryController listCategoryController;
    private UserDAO userDAO;
    private ConstraintLayout layout_not_found_item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_category_activy);

        initView();
        userDAO = new UserDAO();


        listCategoryController = new ListCategoryController(this);

        buildCategoryListView();
        //  CategoryDAO.getInstance().getListCategory(this, lvCategory);
        getCategoryListView();


    }

    private void buildCategoryListView() {
        listCategoryController.getListCategory(this, lvCategory, layout_not_found_item);
    }

    private void initView() {
        lvCategory = findViewById(R.id.lvCategory);
        layout_not_found_item = findViewById(R.id.layout_not_found_item);
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
                if (categoryName.isEmpty()) {
                    Toast.makeText(CategoryActivity.this, "Loại sản phẩm không được để trống", Toast.LENGTH_SHORT).show();
                } else {
                    String formatCategoryName = categoryName.toUpperCase().charAt(0) + categoryName.substring(1).toLowerCase();
                    Intent intent = new Intent();
                    intent.putExtra(CATEGORY_DATA, formatCategoryName);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }


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