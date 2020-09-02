package com.koit.capstonproject_version_1.controller.Interface;

import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.model.SuggestedProduct;

public interface IProduct {
    void getSuggestedProduct(SuggestedProduct product);
    void getProductById(Product product);

}
