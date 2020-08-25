package com.koit.capstonproject_version_1.Controller;

import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.util.List;

public class EditProductQuantityController {
    public EditProductQuantityController() {
    }
// 32 goi ==> 1 thung + 2 goi
    public void convertUnitList(List<Unit> unitList) {
        // Collections.reverse(unitList);

        long total = unitList.get(unitList.size() - 1).getUnitQuantity();
        for (int i = 1; i < unitList.size(); i++) {
            total = total - unitList.get(i - 1).getUnitQuantity() * unitList.get(i - 1).getConvertRate();
            long quantity = total / unitList.get(i).getConvertRate();
            unitList.get(i).setUnitQuantity(quantity);
        }
    }
// 32 goi = 1 thùng
    public void calInventoryByUnit(List<Unit> unitList) {
        //Collections.reverse(unitList);

        long total = 0;
        for (int i = 0; i < unitList.size(); i++) {
            total = total + unitList.get(i).getConvertRate() * unitList.get(i).getUnitQuantity();
        }
        for (int i = 0; i < unitList.size(); i++) {
            unitList.get(i).setUnitQuantity(total / unitList.get(i).getConvertRate());
        }
    }


    public void addUnitsToFireBase(Product product, List<Unit> unitList) {
        Unit unit = new Unit();
        unit.addUnitsToFirebase(UserDAO.getInstance().getUserID(), product.getProductId(), unitList);
    }


}
