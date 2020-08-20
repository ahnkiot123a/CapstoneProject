package com.koit.capstonproject_version_1.View.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.koit.capstonproject_version_1.Controller.OrderHistoryController;
import com.koit.capstonproject_version_1.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RelativeLayout cart_badge;
    private TextView totalOrderDraftQuantity;
    private OrderHistoryController orderHistoryController;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        cart_badge = root.findViewById(R.id.cart_badge);
        totalOrderDraftQuantity = root.findViewById(R.id.totalOrderDraftQuantity);
        cart_badge.setVisibility(View.GONE);

        orderHistoryController = new OrderHistoryController(this.getActivity());
        orderHistoryController.setTotalDraftOrder(cart_badge,totalOrderDraftQuantity);
        return root;
    }
}