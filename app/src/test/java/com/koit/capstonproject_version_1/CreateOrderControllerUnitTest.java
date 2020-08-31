package com.koit.capstonproject_version_1;

import com.koit.capstonproject_version_1.Controller.CreateOrderController;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CreateOrderControllerUnitTest {
    List<Product> listSelectedProductInOrder = new ArrayList<>();
    CreateOrderController createOrderController = new CreateOrderController();

    @Before
    public void beforeTest() {
        List<Unit> unitList1 = new ArrayList<>();
        unitList1.add(new Unit("0", "bộ", 1, 196000, 1));
        Product product1 = new Product("0399271212", "3db099e7-ba38-46f1-85ca-ed0b64598730", null,
                null, null, null, "mèo nổ", true, unitList1);
        List<Unit> unitList2 = new ArrayList<>();
        unitList2.add(new Unit("0", "quyển", 1, 15000, 1));
        Product product2 = new Product("0399271212", "00713223-5062-412f-9a26-5f40710d87e2", null,
                null, null, null, "vở notebook", true, unitList2);
        List<Unit> unitList3 = new ArrayList<>();
        unitList3.add(new Unit("2", "gói", 1, 7000, 1));
        Product product3 = new Product("0399271212", "15691528-2632-4c53-9f08-b28741f2b688", null,
                null, null, null, "omachi sườn", true, unitList3);
        listSelectedProductInOrder.add(product1);
        listSelectedProductInOrder.add(product2);
        listSelectedProductInOrder.add(product3);
    }

    @Test
    public void calTotalProductQuantity_isCorrect() {
        long totalProductQuantity = createOrderController.calTotalProductQuantity(listSelectedProductInOrder);
        long expectedValue = 3;
        assertEquals(expectedValue, totalProductQuantity);

    }

    @Test
    public void calTotalPrice_isCorrect() {
        long totalPrice = createOrderController.calTotalPrice(listSelectedProductInOrder);
        long expectedValue = 218000;
        assertEquals(expectedValue, totalPrice);
    }

    @Test
    public void calTotalQuantityInUnitList() {
        long totalProductQuantity = createOrderController.calTotalQuantityInUnitList(listSelectedProductInOrder.get(0).getUnits());
        long expectedValue = 1;
        assertEquals(expectedValue, totalProductQuantity);
    }


}
