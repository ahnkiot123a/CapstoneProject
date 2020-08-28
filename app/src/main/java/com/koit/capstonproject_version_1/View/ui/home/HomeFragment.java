package com.koit.capstonproject_version_1.View.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.koit.capstonproject_version_1.Controller.OrderHistoryController;
import com.koit.capstonproject_version_1.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RelativeLayout cart_badge;
    private TextView totalOrderDraftQuantity;
    private OrderHistoryController orderHistoryController;
    private CardView layoutCreateOrder, layoutDraftOrder, layoutCreateProduct, layoutProductList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initView(root);



        orderHistoryController = new OrderHistoryController(this.getActivity());
        orderHistoryController.setTotalDraftOrder(cart_badge,totalOrderDraftQuantity);
        return root;
    }

    private void initView(View root) {
        cart_badge = root.findViewById(R.id.cart_badge);
        totalOrderDraftQuantity = root.findViewById(R.id.totalOrderDraftQuantity);
        layoutCreateOrder = root.findViewById(R.id.layoutCreateOrder);
        layoutDraftOrder = root.findViewById(R.id.layoutDraftOrder);
        layoutCreateProduct = root.findViewById(R.id.layoutCreateProduct);
        layoutProductList = root.findViewById(R.id.layoutProductList);

        setAnimationLayout();

        //
        cart_badge.setVisibility(View.GONE);
    }

    private void setAnimationLayout() {

        layoutCreateOrder.setAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_in_left));
        layoutCreateProduct.setAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_in_left));
        layoutDraftOrder.setAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_in_right));
        layoutProductList.setAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_in_right));
    }
}