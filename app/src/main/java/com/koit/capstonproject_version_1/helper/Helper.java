package com.koit.capstonproject_version_1.helper;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.model.Unit;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class Helper {
    public static Helper mInstance;

    public Helper() {
    }

    public static Helper getInstance() {
        if (mInstance == null) mInstance = new Helper();
        return mInstance;
    }

    public int getColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void setImage(CircleImageView ivAvatar, TextView tvFirstName, char firstName) {
        //set color background of image
        ivAvatar.setImageDrawable(new ColorDrawable(Helper.getInstance().getColor()));
        tvFirstName.setText(String.valueOf(firstName).toUpperCase());
    }

    //check product has only 1 unit or not
    public boolean hasOnly1Unit(Product product) {
        int count = 0;
        List<Unit> unitList = product.getUnits();
        if (unitList != null)
            for (Unit unit : unitList) {
                count++;
                if (count >= 2) break;
            }
        if (count == 1)
            // product has only 1 unit
            return true;
        else if (count == 0) {
            return false;
        } else {
            // product has more than 1 unit
            return false;
        }
    }

    //get position of product in list, if list doesn't contain product, return -1
    public int getPositionOfProduct(List<Product> productList, Product product) {
        int position = -1;
        for (Product pInList : productList
        ) {
            if (pInList.getProductId().equals(product.getProductId())) {
                position = productList.indexOf(pInList);
            }
        }
        return position;
    }

    //return true if add new product, false if change quantity
    public boolean addProductToListInOrder(List<Product> productList, Product product) {
        boolean isAddnew = true;
        if (hasOnly1Unit(product)) {
            int position = getPositionOfProduct(productList, product);
            if (position != -1) {
                //add quantity to Unit of Product in productList
                Unit unitOfProduct = productList.get(position).getUnits().get(0);
                productList.get(position).getUnits().get(0).setUnitQuantity(unitOfProduct.getUnitQuantity() + 1);
                isAddnew = false;
            } else {
                Product productInOrder = new Product();
                productInOrder.setProductId(product.getProductId());
                productInOrder.setProductName(product.getProductName());
                productInOrder.setUnits(getMinUnit(product.getUnits()));

                productList.add(productInOrder);

            }
        } else {
            Product productInOrder = new Product();
            productInOrder.setProductId(product.getProductId());
            productInOrder.setProductName(product.getProductName());
            productInOrder.setUnits(getMinUnit(product.getUnits()));

            productList.add(productInOrder);
        }
        return isAddnew;
    }

    public static List<Unit> getMinUnit(List<Unit> unitList) {
        List<Unit> list = new ArrayList<>();
        if (unitList != null)
            for (Unit unit : unitList) {
                if (unit.getConvertRate() == 1) {
                    Unit unitInOrder = new Unit();
                    unitInOrder.setUnitId(unit.getUnitId());
                    unitInOrder.setUnitName(unit.getUnitName());
                    unitInOrder.setUnitPrice(unit.getUnitPrice());
                    unitInOrder.setUnitQuantity(1);
                    list.add(unitInOrder);
                    break;
                }
            }
        // list contain max 1 item
        return list;
    }

    //Tách list selected product và duplicate phần tử ở inventory
    public void toListOfEachUnit(List<Product> selectedProducts, List<Product> inventory) {
        List<Product> newSelectedPros = new ArrayList();
        List<Product> newInvent = new ArrayList();
        for (int i = 0; i < selectedProducts.size(); i++) {
            Product p = selectedProducts.get(i);
            for (Unit u : p.getUnits()) {
                Product pro = new Product(p.getUserId(), p.getProductId(), p.getBarcode(), p.getCategoryName(),
                        p.getProductDescription(), p.getProductImageUrl(), p.getProductName(), p.isActive());
                pro.getUnits().add(u);
                newSelectedPros.add(pro);
                Product pInvent = inventory.get(i);
                newInvent.add(pInvent);
            }
        }
        selectedProducts.clear();
        selectedProducts.addAll(newSelectedPros);
        inventory.clear();
        inventory.addAll(newInvent);
    }

    //Tách mỗi product ra làm nhiều product nhỏ với unit khác nhau
    private List<Product> getProductOfEachUnit(Product p, List<Product> newInvent) {
        List<Product> products = new ArrayList();
        for (Unit u : p.getUnits()) {
            Product pro = new Product(p.getUserId(), p.getProductId(), p.getBarcode(), p.getCategoryName(),
                    p.getProductDescription(), p.getProductImageUrl(), p.getProductName(), p.isActive());
            pro.getUnits().add(u);
            products.add(pro);
            newInvent.add(p);
        }
        return products;
    }

    public String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}
