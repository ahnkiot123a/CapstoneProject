package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.graphics.Canvas;
import android.util.Log;
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
import com.koit.capstonproject_version_1.Controller.Interface.IInvoiceDetail;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.Model.InvoiceDetail;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.Controller.OrderSwipeController;
import com.koit.capstonproject_version_1.helper.Helper;


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
    private InvoiceDetail invoiceDetail;
    private int hightLightPosition;

    public ListItemInOrderController(Activity context, List<Product> listSelectedProductInOrder,
                                     List<Product> listSelectedProductInWareHouse) {
        this.context = context;
        product = new Product();
        this.listSelectedProductInOrder = listSelectedProductInOrder;
        this.listSelectedProductInWareHouse = listSelectedProductInWareHouse;
        invoiceDetail = new InvoiceDetail();
        hightLightPosition = -1;

    }

    public void getListProduct(String searchText, final RecyclerView recyclerViewListProduct, final TextView tvTotalQuantity,
                               final TextView tvTotalPrice) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        itemAdapter = new ItemInOrderAdapter(context, R.layout.item_layout_in_order, listSelectedProductInWareHouse,
                tvTotalQuantity, tvTotalPrice, listSelectedProductInOrder, hightLightPosition);
        recyclerViewListProduct.setAdapter(itemAdapter);

        ListProductInterface listProductInterface = new ListProductInterface() {
            @Override
            public void getListProductModel(Product product) {
                int position = Helper.getInstance().getPositionOfProduct(listSelectedProductInOrder, product);
                if (Helper.getInstance().hasOnly1Unit(product)) {
                    if (position != -1) {
                        Unit unitOfProduct = listSelectedProductInOrder.get(position).getUnits().get(0);
                        listSelectedProductInOrder.get(position).getUnits().get(0).setUnitQuantity(unitOfProduct.getUnitQuantity() + 1);
                        hightLightPosition = position;
                    } else {
                        //san pham co 1 unit nhung chua co trong danh sach
                        Product productInOrder = new Product();
                        productInOrder.setProductId(product.getProductId());
                        productInOrder.setProductName(product.getProductName());
                        productInOrder.setUnits(Helper.getInstance().getMinUnit(product.getUnits()));

                        listSelectedProductInWareHouse.add(product);
                        listSelectedProductInOrder.add(productInOrder);

                        hightLightPosition = listSelectedProductInOrder.size() - 1;
                    }
                } else {
                    //san pham co 2 units tro leen
                    Product productInOrder = new Product();
                    productInOrder.setProductId(product.getProductId());
                    productInOrder.setProductName(product.getProductName());
                    productInOrder.setUnits(Helper.getInstance().getMinUnit(product.getUnits()));

                    listSelectedProductInWareHouse.add(product);
                    listSelectedProductInOrder.add(productInOrder);

                    hightLightPosition = listSelectedProductInOrder.size() - 1;

                }

                getListProduct("", recyclerViewListProduct, tvTotalQuantity, tvTotalPrice);

                Log.d("listSelectedProducLI2", listSelectedProductInWareHouse.toString());
                Log.d("listSelectedProducLI1", listSelectedProductInOrder.toString());

                recyclerViewListProduct.scrollToPosition(position);

                itemAdapter.notifyDataSetChanged();
            }
        };
        product.getListProduct(searchText, listProductInterface);

    }

    public void getListProductInDraftOrder(String invoiceId) {
        IInvoiceDetail iInvoiceDetail = new IInvoiceDetail() {
            @Override
            public void getListProductInOrder(Product product) {
                if (product != null) {
                    listSelectedProductInOrder.add(product);
                }
            }

            @Override
            public void getListProductInWarehouse(Product product) {
                if (product != null) {
                    listSelectedProductInWareHouse.add(product);

                }
            }
        };
        invoiceDetail.getListProductInDraftOrder(iInvoiceDetail, invoiceId);
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

