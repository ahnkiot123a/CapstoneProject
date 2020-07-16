package com.koit.capstonproject_version_1.Controller.Interface;

import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.SuggestedProduct;

public interface IProduct {
    void getSuggestedProduct(SuggestedProduct product);
    void isExistBarcode(boolean existed);

}
