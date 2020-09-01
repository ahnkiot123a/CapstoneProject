package com.koit.capstonproject_version_1.View.ui.home;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.koit.capstonproject_version_1.Controller.OrderHistoryController;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.ListProductActivity;
import com.koit.capstonproject_version_1.View.SelectProductActivity;

import java.io.Serializable;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RelativeLayout cart_badge;
    private TextView totalOrderDraftQuantity;
    private OrderHistoryController orderHistoryController;
    private CardView layoutCreateOrder, layoutDraftOrder, layoutCreateProduct, layoutProductList;
    private SearchView etSearchField;
    private ConstraintLayout layoutSearchHome;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initView(root);
        etSearchField.clearFocus();
        etSearchField.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Intent intent = new Intent(getActivity(), ListProductActivity.class);
                Bundle args2 = new Bundle();
                args2.putBoolean("isFromHomeFragment", true);
                intent.putExtra("BUNDLE", args2);
                startActivity(intent);
            }
        });
        orderHistoryController = new OrderHistoryController(this.getActivity());
        orderHistoryController.setTotalDraftOrder(cart_badge, totalOrderDraftQuantity);

        return root;


    }

    private ConstraintLayout getlayoutSearch() {
        return layoutSearchHome;
    }

    private void initView(View root) {
        cart_badge = root.findViewById(R.id.cart_badge);
        totalOrderDraftQuantity = root.findViewById(R.id.totalOrderDraftQuantity);
        layoutCreateOrder = root.findViewById(R.id.layoutCreateOrder);
        layoutDraftOrder = root.findViewById(R.id.layoutDraftOrder);
        layoutCreateProduct = root.findViewById(R.id.layoutCreateProduct);
        layoutProductList = root.findViewById(R.id.layoutProductList);
        etSearchField = root.findViewById(R.id.etSearchField);
        layoutSearchHome = root.findViewById(R.id.layoutSearchHome);
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