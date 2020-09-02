package com.koit.capstonproject_version_1;

import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.model.Unit;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductUnitTest {
    Product product;
    @Before
    public void beforeTest(){
        Unit u1 = new Unit("u1", "Hộp",0,0,2);
        Unit u2 = new Unit("u2", "Gói",0,0,3);
        Unit u3 = new Unit("u3", "Thùng",0,0,3);
        Unit u4 = new Unit("u4", "Cái",0,0,2);
        Unit u5 = new Unit("u5", "Combo",0,0,5);
        List<Unit> units = new ArrayList<>();
        units.add(u1);
        units.add(u2);
        units.add(u3);
        units.add(u4);
        units.add(u5);
        product = new Product("0395106907", "PRO121", "", "", "",
                "", "",true, units);
    }
    @Test
    public void addUnit_istrue1(){
        // Not duplicated
        Unit addedUnit = new Unit("u0", "Unit0",0,0,5);
        product.addUnit(addedUnit);
        assertEquals(5, product.getUnits().get(5).getUnitQuantity());
    }
    @Test
    public void addUnit_istrue2(){
        // Duplicated with first unit
        Unit addedUnit = new Unit("u1", "Hộp",0,0,4);
        product.addUnit(addedUnit);
        assertEquals(6, product.getUnits().get(0).getUnitQuantity());
    }
    @Test
    public void addUnit_istrue3(){
        // Duplicated with third unit
        Unit addedUnit = new Unit("u3", "Thùng",0,0,7);
        product.addUnit(addedUnit);
        assertEquals(10, product.getUnits().get(2).getUnitQuantity());
    }
    @Test
    public void addUnit_istrue4(){
        // Duplicated with list unit
        Unit addedUnit = new Unit("u5", "Combo",0,0,1);
        product.addUnit(addedUnit);
        assertEquals(6, product.getUnits().get(4).getUnitQuantity());
    }
}