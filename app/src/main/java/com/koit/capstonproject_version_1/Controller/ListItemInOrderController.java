package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.graphics.Canvas;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Adapter.ItemBeforeOrderAdapter;
import com.koit.capstonproject_version_1.Adapter.ItemInOrderAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.Controller.OrderSwipeController;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListItemInOrderController extends AppCompatActivity {
    private Activity context;
    private OrderSwipeController orderSwipeController;
    private Product product;
    private ItemInOrderAdapter itemAdapter;
    private List<Product> listSelectedProductInOrder;
    private List<Product> listSelectedProductInWareHouse;

    public ListItemInOrderController(Activity context, List<Product> listSelectedProductInOrder,
                                     List<Product> listSelectedProductInWareHouse) {
        this.context = context;
        product = new Product();
        this.listSelectedProductInWareHouse = listSelectedProductInWareHouse;
        this.listSelectedProductInOrder = listSelectedProductInOrder;
    }

    public void getListProduct(String searchText, RecyclerView recyclerViewListProduct, TextView tvTotalQuantity,
                               TextView tvTotalPrice) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        itemAdapter = new ItemInOrderAdapter(context, R.layout.item_layout_in_order, listSelectedProductInWareHouse,
                tvTotalQuantity, tvTotalPrice, listSelectedProductInOrder);
        recyclerViewListProduct.setAdapter(itemAdapter);
        ListProductInterface listProductInterface = new ListProductInterface() {
            @Override
            public void getListProductModel(Product product) {
                //add product to list in ware house
                listSelectedProductInWareHouse.add(product);

                //add prodcut to list in order
                Product productInOrder = new Product();
                productInOrder.setProductId(product.getProductId());
                productInOrder.setProductName(product.getProductName());
                productInOrder.setUnits(getMinUnit(product.getUnits()));
                listSelectedProductInOrder.add(productInOrder);

                itemAdapter.notifyDataSetChanged();
            }
        };
        product.getListProduct(searchText, listProductInterface);
    }

    //return 1 unit that contain convert rate = 1;
    private List<Unit> getMinUnit(List<Unit> unitList) {
        List<Unit> list = new ArrayList<>();
        if (unitList != null)
            for (Unit unit : unitList) {
                if (unit.getConvertRate() == 1) {
                    Unit unitInOrder = new Unit();
                    unitInOrder.setUnitId(unit.getUnitId());
                    unitInOrder.setUnitName(unit.getUnitName());
                    unitInOrder.setUnitPrice(unit.getUnitPrice());
                    unitInOrder.setUnitQuantity(1);
                    list.add(unitInOrder);
                    break;
                }
            }
        // list contain max 1 item
        return list;
    }
    public void setupRecyclerView(RecyclerView recyclerView, final TextView tvTotalQuantity, final TextView tvTotalPrice) {
        orderSwipeController = new OrderSwipeController(new OrderSwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                //remove item in 2 list
                listSelectedProductInOrder.remove(position);
                listSelectedProductInWareHouse.remove(position);
                itemAdapter.notifyItemRemoved(position);

                tvTotalQuantity.setText(ItemInOrderAdapter.getTotalQuantity(listSelectedProductInOrder) + "");
                tvTotalPrice.setText(Money.getInstance().formatVN(ItemInOrderAdapter.getTotalPrice(listSelectedProductInOrder)));
//                itemAdapter.notifyItemRangeChanged(position, itemAdapter.getItemCount());
            }

        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(orderSwipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                orderSwipeController.onDraw(c);
            }
        });
    }
}

