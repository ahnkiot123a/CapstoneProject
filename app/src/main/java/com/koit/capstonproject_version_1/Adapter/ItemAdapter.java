package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.koit.capstonproject_version_1.Model.Product;

import androidx.annotation.NonNull;

public class ItemAdapter extends ArrayAdapter<Product> {
    public ItemAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
