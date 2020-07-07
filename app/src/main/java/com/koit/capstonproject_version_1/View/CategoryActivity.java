package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.dao.CategoryDAO;

public class CategoryActivity extends AppCompatActivity {

    public static final String CATEGORY_DATA = "CATEGORY_DATA";

    private ListView lvCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_activy);

        initView();

        CategoryDAO.getInstance().getListCategory(this, lvCategory);

        getCategoryListView();
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
}