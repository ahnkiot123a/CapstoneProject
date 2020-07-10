package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.koit.capstonproject_version_1.Adapter.ItemAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.ListProductActivity;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListProductController extends AppCompatActivity {
    //    private ListProductActivity listProductActivity;
//    private DatabaseReference myRef;
//    private ArrayList<Product> list;
    private Context context;
    private Product product;
    ItemAdapter itemAdapter;
    private int item_count;
    private List<Product> productList;
    SwipeController swipeController = null;

    public ListProductController(Context context) {
        this.context = context;
        product = new Product();
    }

    public void getListProduct(String searchText, RecyclerView recyclerViewListProduct, final TextView textView,
                               LinearLayout linearLayoutEmpty, ConstraintLayout constraintLayout,
                               LinearLayout layoutNotFoundItem, Spinner category_Spinner, ProgressBar pBarList) {
        final List<Product> listProduct = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(context, listProduct, R.layout.item_layout);
        recyclerViewListProduct.setAdapter(itemAdapter);
        ListProductInterface listProductInterface = new ListProductInterface() {
            @Override
            public void getListProductModel(Product product) {
                listProduct.add(product);
                textView.setText(listProduct.size() + " sản phẩm");
                itemAdapter.notifyDataSetChanged();
            }

        };
        product.getListProduct(searchText, listProductInterface, textView, linearLayoutEmpty,
                constraintLayout, layoutNotFoundItem, category_Spinner, pBarList);
    }

    public void getListProduct(Context context, RecyclerView recyclerViewListProduct, String categoryName,
                               final TextView textView, LinearLayout linearLayoutEmpty, ConstraintLayout
                                       constraintLayout, LinearLayout layoutNotFoundItem, Spinner category_Spinner, ProgressBar pBarList) {
        final List<Product> listProduct = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(context, listProduct, R.layout.item_layout);
        recyclerViewListProduct.setAdapter(itemAdapter);
        ListProductInterface listProductInterface = new ListProductInterface() {
            @Override
            public void getListProductModel(Product product) {
                listProduct.add(product);
                textView.setText(listProduct.size() + " sản phẩm");
                itemAdapter.notifyDataSetChanged();
            }

        };
        product.getListProduct(listProductInterface, categoryName, linearLayoutEmpty, constraintLayout,
                layoutNotFoundItem, textView, category_Spinner, pBarList);

    }

    public void tranIntent(Activity activity1, Class activity2) {
        Intent intent = new Intent(activity1.getApplicationContext(), activity2);
        activity1.startActivity(intent);
    }

    public void setupRecyclerView(RecyclerView recyclerView) {
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                //remove item on right click
                itemAdapter.notifyItemRemoved(position);
                itemAdapter.notifyItemRangeChanged(position, itemAdapter.getItemCount());
                //remove tu beo

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có chắc chắn muốn xoá sản phẩm này không? ")
                        .setCancelable(false)
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                UserDAO userDAO = new UserDAO();
                                Unit unit = new Unit();
                                product.removeProduct(userDAO.getUserID(), product.getProductId());
                                unit.removeProductUnits(userDAO.getUserID(), product.getProductId());
                                Intent intent = new Intent(context, ListProductActivity.class);
                                context.startActivity(intent);
                                Toast.makeText(context, "Bạn đã xoá thành công sản phẩm", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
                //Itent sang man hinh edit
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }
}
