package com.koit.capstonproject_version_1.controller;

import android.app.Activity;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.adapter.ItemInOrderAdapter;
import com.koit.capstonproject_version_1.controller.Interface.IInvoiceDetail;
import com.koit.capstonproject_version_1.controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.model.InvoiceDetail;
import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.helper.Money;
import com.koit.capstonproject_version_1.model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.helper.Helper;


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
                tvTotalQuantity, tvTotalPrice, listSelectedProductInOrder, hightLightPosition, recyclerViewListProduct);
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
                        //haha
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

                recyclerViewListProduct.scrollToPosition(hightLightPosition);

                itemAdapter.notifyDataSetChanged();
            }
        };
        product.getListProduct(searchText, listProductInterface);

    }

    public void getListProductInDraftOrder(String invoiceId) {
        IInvoiceDetail iInvoiceDetail = new IInvoiceDetail() {
            int count = 0;

            @Override
            public void getListProductInOrder(Product product) {
//                if (product != null) {
//                    for (Unit u : product.getUnits()) {
//                        Product pro = new Product(product.getUserId(), product.getProductId(), product.getBarcode(), product.getCategoryName(),
//                                product.getProductDescription(), product.getProductImageUrl(), product.getProductName(), product.isActive());
//                        pro.getUnits().add(u);
//                        listSelectedProductInOrder.add(pro);
//                        count++;
//                    }
//                }
//                listSelectedProductInOrder.add(product);
            }

            @Override
            public void getListProductInWarehouse(Product product) {
//                if (product != null) {
//                    for(int i=0;i<count;i++){
//
//                    }
//                }
//                  listSelectedProductInOrder.add(product);
            }

            @Override
            public void getListProductInOrderWWarehouse(Product productInOrder, Product productWarehouse) {
                if (productInOrder != null && productWarehouse != null)
                    for (Unit u : productInOrder.getUnits()) {
                        Product pro = new Product(productInOrder.getUserId(), productInOrder.getProductId(),
                                productInOrder.getBarcode(), productInOrder.getCategoryName(),
                                productInOrder.getProductDescription(), productInOrder.getProductImageUrl(),
                                productInOrder.getProductName(), productInOrder.isActive());
                        pro.getUnits().add(u);
                        listSelectedProductInOrder.add(pro);
                        listSelectedProductInWareHouse.add(productWarehouse);

                        itemAdapter.notifyDataSetChanged();
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

