package com.koit.capstonproject_version_1;

import com.koit.capstonproject_version_1.controller.EditProductQuantityController;
import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.model.Unit;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EditProductQuantityControllerUnitTest {
    EditProductQuantityController editProductQuantityController = new EditProductQuantityController();
    Product product;
    List<Unit> unitList;

    @Before
    public void beforeTest() {
        unitList = new ArrayList<>();
        unitList.add(new Unit("1", "thùng", 30, 200000, 32));
        unitList.add(new Unit("0", "túi", 10, 65000, 96));
        unitList.add(new Unit("2", "gói", 1, 7000, 965));
        product = new Product("0399271212", "15691528-2632-4c53-9f08-b28741f2b688", null,
                null, null, null, "omachi sườn", true, unitList);
    }


    @Test
    public void convertUnitList_isCorrect() {
        editProductQuantityController.convertUnitList(unitList);

        assertEquals(5,unitList.get(unitList.size()-1).getUnitQuantity());
        assertNotEquals(6,unitList.get(unitList.size()-1).getUnitQuantity());
        assertNotEquals(4,unitList.get(unitList.size()-1).getUnitQuantity());

        assertEquals(0,unitList.get(unitList.size()-2).getUnitQuantity());
        assertNotEquals(1,unitList.get(unitList.size()-2).getUnitQuantity());
        assertNotEquals(-1,unitList.get(unitList.size()-2).getUnitQuantity());

        assertEquals(32,unitList.get(0).getUnitQuantity());
        assertNotEquals(33,unitList.get(0).getUnitQuantity());
        assertNotEquals(31,unitList.get(0).getUnitQuantity());
    }
    @Test
    public void calInventoryByUnit_isCorrect() {
        editProductQuantityController.convertUnitList(unitList);
        editProductQuantityController.calInventoryByUnit(unitList);

        assertEquals(965,unitList.get(unitList.size()-1).getUnitQuantity());
        assertNotEquals(964,unitList.get(unitList.size()-1).getUnitQuantity());
        assertNotEquals(966,unitList.get(unitList.size()-1).getUnitQuantity());

        assertEquals(96,unitList.get(unitList.size()-2).getUnitQuantity());
        assertNotEquals(97,unitList.get(unitList.size()-2).getUnitQuantity());
        assertNotEquals(95,unitList.get(unitList.size()-2).getUnitQuantity());

        assertEquals(32,unitList.get(0).getUnitQuantity());
        assertNotEquals(33,unitList.get(0).getUnitQuantity());
        assertNotEquals(31,unitList.get(0).getUnitQuantity());
    }
}
