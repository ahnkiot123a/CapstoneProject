package com.koit.capstonproject_version_1.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.koit.capstonproject_version_1.adapter.ItemAdapter;
import com.koit.capstonproject_version_1.controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.view.ListProductActivity;
import com.koit.capstonproject_version_1.view.UpdateProductInformationActivity;
import com.koit.capstonproject_version_1.model.dao.UserDAO;
import com.koit.capstonproject_version_1.helper.CustomToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private Activity context;
    private Product product;
    ItemAdapter itemAdapter;
    private List<Product> listProduct;
    SwipeController swipeController = null;
    RecyclerView recyclerView;

    public ListProductController(Activity context) {
        this.context = context;
        product = new Product();
    }

    public void getListProduct(String searchText, RecyclerView recyclerViewListProduct, final TextView textView,
                               LinearLayout linearLayoutEmpty, ConstraintLayout constraintLayout,
                               LinearLayout layoutNotFoundItem, Spinner category_Spinner, ProgressBar pBarList) {
        listProduct = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView = recyclerViewListProduct;
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

    public void getListProduct(RecyclerView recyclerViewListProduct, String categoryName,
                               final TextView textView, LinearLayout linearLayoutEmpty, ConstraintLayout
                                       constraintLayout, LinearLayout layoutNotFoundItem, Spinner category_Spinner, ProgressBar pBarList) {
        listProduct = new ArrayList<>();

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


    public void setupRecyclerView(RecyclerView recyclerView, final ListProductActivity listProductActivity) {
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                //confirm to delete
                final Product product = listProduct.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(listProductActivity);
                builder.setMessage("Bạn có chắc chắn muốn xoá sản phẩm này không? ")
                        .setCancelable(true)
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //remove item on right click
                                itemAdapter.notifyItemRemoved(position);
                                itemAdapter.notifyItemRangeChanged(position, itemAdapter.getItemCount());
                                UserDAO userDAO = new UserDAO();
                                Unit unit = new Unit();
                                Product product = listProduct.get(position);
                                product.removeProduct(product);
                                unit.removeProductUnits(userDAO.getUserID(), product.getProductId());
                                Intent intent = new Intent(listProductActivity, ListProductActivity.class);
                                listProductActivity.startActivity(intent);
                                CustomToast.makeText(listProductActivity, "Bạn đã xoá thành công sản phẩm",
                                        Toast.LENGTH_LONG, CustomToast.SUCCESS, true, Gravity.BOTTOM).show();
                            }
                        })
                        .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                    }
                });
                alert.show();

            }

            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
                //Itent sang man hinh edit
                Product product = listProduct.get(position);
                ArrayList<Unit> unitList = (ArrayList<Unit>) product.getUnits();
                sortUnitByPrice(unitList);
                product.setUnits(unitList);
                Intent intentProduct = new Intent(context, UpdateProductInformationActivity.class);
                intentProduct.putExtra("product", product);
                intentProduct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentProduct);
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

    public List<Product> getListProduct() {
        return itemAdapter.getListProduct();
    }

    public void tranIntent(Activity activity1, Class activity2) {
        Intent intent = new Intent(activity1, activity2);
        activity1.startActivity(intent);
    }

    public void sortUnitByPrice(ArrayList<Unit> unitList) {
        Collections.sort(unitList, new Comparator<Unit>() {
            @Override
            public int compare(Unit o1, Unit o2) {
                return (int) (o2.getUnitPrice() - o1.getUnitPrice());
            }


        });
    }

}
