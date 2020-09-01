package com.koit.capstonproject_version_1;

import com.koit.capstonproject_version_1.Controller.CreateOrderController;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
        assertNotEquals(expectedValue - 1, totalProductQuantity);
        assertNotEquals(expectedValue + 1, totalProductQuantity);
    }

    @Test
    public void calTotalPrice_isCorrect() {
        long totalPrice = createOrderController.calTotalPrice(listSelectedProductInOrder);
        long expectedValue = 218000;
        assertEquals(expectedValue, totalPrice);
        assertNotEquals(expectedValue + 1, totalPrice);
        assertNotEquals(expectedValue - 1, totalPrice);

    }

    @Test
    public void calTotalQuantityInUnitList() {
        long totalProductQuantity = createOrderController.calTotalQuantityInUnitList(listSelectedProductInOrder.get(0).getUnits());
        long expectedValue = 1;
        assertEquals(expectedValue, totalProductQuantity);
        assertNotEquals(expectedValue + 1, totalProductQuantity);
        assertNotEquals(expectedValue - 1, totalProductQuantity);

    }

    @Test
    public void formatListProductInOrder_isCorrect() {
        listSelectedProductInOrder = new ArrayList<>();
        List<Unit> unitList1 = new ArrayList<>();
        unitList1.add(new Unit("2", "gói", 1, 196000, 4));
        Product product1 = new Product("0399271212", "15691528-2632-4c53-9f08-b28741f2b688", null,
                null, null, null, "omachi sườn", true, unitList1);
        List<Unit> unitList2 = new ArrayList<>();
        unitList2.add(new Unit("1", "thùng", 1, 15000, 3));
        Product product2 = new Product("0399271212", "15691528-2632-4c53-9f08-b28741f2b688", null,
                null, null, null, "omachi sườn", true, unitList2);
        List<Unit> unitList3 = new ArrayList<>();
        unitList3.add(new Unit("0", "túi", 1, 7000, 1));
        Product product3 = new Product("0399271212", "15691528-2632-4c53-9f08-b28741f2b688", null,
                null, null, null, "omachi sườn", true, unitList3);
        listSelectedProductInOrder.add(product1);
        listSelectedProductInOrder.add(product2);
        listSelectedProductInOrder.add(product3);

        List<Product> expectedListSelectedProductInOrder = new ArrayList<>();
        List<Unit> unitList4 = new ArrayList<>();
        unitList4.add(new Unit("2", "gói", 1, 196000, 4));
        unitList4.add(new Unit("1", "thùng", 1, 15000, 3));
        unitList4.add(new Unit("0", "túi", 1, 7000, 1));
        Product product4 = new Product("0399271212", "15691528-2632-4c53-9f08-b28741f2b688", null,
                null, null, null, "omachi sườn", true, unitList4);
        expectedListSelectedProductInOrder.add(product4);
        createOrderController.formatListProductInOrder(listSelectedProductInOrder);

        assertEquals(listSelectedProductInOrder.get(0).getProductId(), expectedListSelectedProductInOrder.get(0).getProductId());
        assertEquals(listSelectedProductInOrder.size(), expectedListSelectedProductInOrder.size());
    }

    @Test
    public void formatListProductWarehouse_isCorrect() {
        List<Product> listProductWarehouse = new ArrayList<>();
        List<Product> expectedListProductWarehouse = new ArrayList<>();
        List<Unit> unitList = new ArrayList<>();
        unitList.add(new Unit("2", "gói", 1, 196000, 4));
        unitList.add(new Unit("1", "thùng", 1, 15000, 3));
        unitList.add(new Unit("0", "túi", 1, 7000, 1));
        Product product = new Product("0399271212", "15691528-2632-4c53-9f08-b28741f2b688", null,
                null, null, null, "omachi sườn", true, unitList);

        listProductWarehouse.add(product);
        listProductWarehouse.add(product);
        listProductWarehouse.add(product);


        expectedListProductWarehouse.add(product);
        createOrderController.formatListProductWarehouse(listProductWarehouse);
        assertEquals(listProductWarehouse.get(0).getProductId(), expectedListProductWarehouse.get(0).getProductId());
    }

    @Test
    public void sortUnitByPrice_isCorrect() {
        List<Unit> unitList = new ArrayList<>();
        Unit unit1 = new Unit("2", "gói", 1, 196000, 4);
        Unit unit2 = new Unit("1", "thùng", 1, 15000, 3);
        Unit unit3 = new Unit("0", "túi", 1, 7000, 1);
        unitList.add(unit1);
        unitList.add(unit2);
        unitList.add(unit3);
        createOrderController.sortUnitByPrice(unitList);
        assertEquals(unitList.get(0),unit1);
        assertEquals(unitList.get(1),unit2);
        assertEquals(unitList.get(2),unit3);
    }

}
