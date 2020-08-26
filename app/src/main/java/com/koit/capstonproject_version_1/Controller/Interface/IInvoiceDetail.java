package com.koit.capstonproject_version_1.Controller.Interface;

import com.koit.capstonproject_version_1.Model.InvoiceDetail;
import com.koit.capstonproject_version_1.Model.Product;

public interface IInvoiceDetail {
    void getListProductInOrder(Product product);
    void getListProductInWarehouse(Product product);
    void getListProductInOrderWWarehouse(Product productInOrder, Product productWarehouse);
}
