package com.koit.capstonproject_version_1;

import com.koit.capstonproject_version_1.Controller.TimeController;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.helper.Helper;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HelperUnitTest {
    Helper helper;
    Product product;
    List<Product> productList;

    @Before
    public void beforeTest() {
        helper = new Helper();
        Unit u1 = new Unit("u1", "Hộp", 0, 0, 2);
        List<Unit> units = new ArrayList<>();
        units.add(u1);
        product = new Product("0395106907", "PRO121", "", "", "",
                "", "", true, units);
        productList = new ArrayList<>();
        productList.add(product);
    }

    @Test
    public void hasOnly1Unit_istrue1() {

        assertEquals(true, helper.hasOnly1Unit(product));
    }

    @Test
    public void hasOnly1Unit_istrue2() {
        Unit addedUnit = new Unit("u0", "Unit0", 0, 0, 5);
        product.addUnit(addedUnit);
        assertEquals(false, helper.hasOnly1Unit(product));
    }

    @Test
    public void hasOnly1Unit_istrue6() {
        Unit addedUnit = new Unit("u0", "Unit0", 0, 0, 5);
        product.addUnit(addedUnit);
        Unit addedUnit2 = new Unit("u0", "Unit0", 0, 0, 5);
        product.addUnit(addedUnit2);
        Unit addedUnit3 = new Unit("u0", "Unit0", 0, 0, 5);
        product.addUnit(addedUnit3);
        Unit addedUnit4 = new Unit("u0", "Unit0", 0, 0, 5);
        product.addUnit(addedUnit4);

        assertEquals(false, helper.hasOnly1Unit(product));
    }

    @Test
    public void hasOnly1Unit_istrue0() {
        List<Unit> unitList = new ArrayList<>();
        product.setUnits(unitList);
        assertEquals(false, helper.hasOnly1Unit(product));
    }

    @Test
    public void getPositionOfProduct_0() {
        assertEquals(0, helper.getPositionOfProduct(productList, product));
    }

    @Test
    public void getPositionOfProduct_minus1() {
        Product productTest = new Product();
        assertEquals(-1, helper.getPositionOfProduct(productList, productTest));
    }

    @Test
    public void getPositionOfProduct_1() {
        Product product2 = new Product("0395106908", "PRO121", "", "", "",
                "", "", true, null);
        productList.add(product2);
        assertEquals(1, helper.getPositionOfProduct(productList, product2));
    }

    @Test
    public void addProductToListInOrder() {
        assertEquals(false, helper.addProductToListInOrder(productList, product));
        Product product2 = new Product("0395106908", "PRO121", "", "", "",
                "", "", true, null);
        assertEquals(true, helper.addProductToListInOrder(productList, product2));
    }

    @Test
    public void getMinUnit() {
        Unit u1 = new Unit("u1", "Hộp", 1, 0, 2);
        Unit u2 = new Unit("u2", "Thung", 24, 0, 2);
        Unit u3 = new Unit("u3", "Loc", 4, 0, 2);
        List<Unit> units = new ArrayList<>();
        units.add(u2);
        units.add(u1);
        units.add(u3);
        List<Unit> listCheck = new ArrayList<>();
        Unit unitCheck = new Unit("u1", "Hộp", 0, 0, 1);
        listCheck.add(unitCheck);
        assertEquals(listCheck.get(0).getUnitId(), helper.getMinUnit(units).get(0).getUnitId());
    }
    @Test
    public void deAccent(){
        assertEquals("hahahaha",helper.deAccent("hàhahaha"));
        assertEquals("haha ha ha",helper.deAccent("haha há hà"));
    }
}
