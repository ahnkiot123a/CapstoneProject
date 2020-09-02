package com.koit.capstonproject_version_1.controller.Interface;

import com.koit.capstonproject_version_1.model.Product;

public interface IInvoiceDetail {
    void getListProductInOrder(Product product);
    void getListProductInWarehouse(Product product);
    void getListProductInOrderWWarehouse(Product productInOrder, Product productWarehouse);
}
