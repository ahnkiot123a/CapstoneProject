package com.koit.capstonproject_version_1.Controller;

import com.koit.capstonproject_version_1.Adapter.AddProductQuantityAdapter;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddProductQuantityController {
    private Unit unit;
    private UserDAO userDAO;




    public AddProductQuantityController() {
        unit = new Unit();
        userDAO = new UserDAO();
    }
    public void convertUnitList(List<Unit> unitList){
       // Collections.reverse(unitList);

        long total = unitList.get(unitList.size() - 1).getUnitQuantity();
          for (int i = 1; i< unitList.size();i++){
              total = total - unitList.get(i-1).getUnitQuantity() * unitList.get(i-1).getConvertRate();
              long quantity = total / unitList.get(i).getConvertRate() ;
              unitList.get(i).setUnitQuantity(quantity);
          }
    }
    public void convertUnitList2(List<Unit> unitList){
        //Collections.reverse(unitList);

        long total = 0;
       for (int i = 0; i < unitList.size(); i++){
           total = total + unitList.get(i).getConvertRate() * unitList.get(i).getUnitQuantity();
       }
       for (int i = 0; i < unitList.size();i++){
           unitList.get(i).setUnitQuantity(total/unitList.get(i).getConvertRate());
       }
    }


    public void addUnitsToFireBase(Product product, List<Unit> unitList){
        unit.addUnitsToFirebase(userDAO.getUserID(),product.getProductId(),unitList);
    }



}
