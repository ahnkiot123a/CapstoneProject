package com.koit.capstonproject_version_1.controller;

import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.controller.SharedPreferences.SharedPrefs;
import com.koit.capstonproject_version_1.model.User;
import com.koit.capstonproject_version_1.view.LoginActivity;
import com.koit.capstonproject_version_1.view.MainActivity;

import java.util.HashMap;
import java.util.Map;

public final class DemoDataSeeder {

    private static final String DEMO_USER_ID = "0900000000";
    private static final String SEEDED_FLAG_KEY = "DEMO_DATA_SEEDED_V1";

    private DemoDataSeeder() {
    }

    public static void seedAndAutoLogin(final LoginActivity activity) {
        boolean seeded = SharedPrefs.getInstance().get(SEEDED_FLAG_KEY, Boolean.class);
        if (seeded) {
            saveDemoUserAndOpenMain(activity, false);
            return;
        }

        // Allow access immediately, then seed data in background.
        saveDemoUserAndOpenMain(activity, true);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> updates = new HashMap<>();

        addUser(updates);
        addCategories(updates);
        addProductsAndUnits(updates);
        addDebtors(updates);
        addInvoicesAndDetails(updates);
        addDebtPayments(updates);
        addFeedbacks(updates);

        rootRef.updateChildren(updates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error != null) {
                    Toast.makeText(activity, "Seed demo data that bai: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private static void saveDemoUserAndOpenMain(LoginActivity activity, boolean seededNow) {
        SharedPrefs.getInstance().putCurrentUser(LoginActivity.CURRENT_USER, buildDemoUser());
        SharedPrefs.getInstance().put(SEEDED_FLAG_KEY, true);

        if (seededNow) {
            Toast.makeText(activity, "Da seed du lieu demo", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private static User buildDemoUser() {
        User user = new User();
        user.setFullName("Chu cua hang Demo");
        user.setAddress("01 Nguyen Trai, Quan 1, TP.HCM");
        user.setEmail("demo@koit.vn");
        user.setStoreName("Tap Hoa Capstone Demo");
        user.setDateOfBirth("01-01-1995");
        user.setPhoneNumber(DEMO_USER_ID);
        user.setPassword("123456");
        user.setRoleID("1");
        user.setHasFingerprint(false);
        user.setGender(true);
        return user;
    }

    private static void addUser(Map<String, Object> updates) {
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", "Chu cua hang Demo");
        user.put("address", "01 Nguyen Trai, Quan 1, TP.HCM");
        user.put("email", "demo@koit.vn");
        user.put("storeName", "Tap Hoa Capstone Demo");
        user.put("dateOfBirth", "01-01-1995");
        user.put("phoneNumber", DEMO_USER_ID);
        user.put("password", "123456");
        user.put("roleID", "1");
        user.put("hasFingerprint", false);
        user.put("gender", true);
        updates.put("User/" + DEMO_USER_ID, user);
    }

    private static void addCategories(Map<String, Object> updates) {
        String[] categories = new String[]{"Do uong", "Ban keo", "Gia vi", "Mi goi", "Do gia dung"};
        for (String category : categories) {
            updates.put("Categories/" + DEMO_USER_ID + "/" + category + "/categoryName", category);
        }
    }

    private static void addProductsAndUnits(Map<String, Object> updates) {
        addProduct(updates, "PRD001", "8938505974193", "Do uong", "Nuoc ngot coca lon", "", "Coca Cola", true);
        addUnit(updates, "PRD001", "UNIT001", "Lon", 1, 12000, 220);
        addUnit(updates, "PRD001", "UNIT002", "Thung 24 lon", 24, 270000, 35);

        addProduct(updates, "PRD002", "8934673123456", "Do uong", "Sua tuoi khong duong", "", "Sua tuoi Vinamilk", true);
        addUnit(updates, "PRD002", "UNIT001", "Hop", 1, 9000, 180);
        addUnit(updates, "PRD002", "UNIT002", "Thung 48 hop", 48, 410000, 20);

        addProduct(updates, "PRD003", "8934803043210", "Ban keo", "Banh quy bo", "", "Banh quy bo", true);
        addUnit(updates, "PRD003", "UNIT001", "Goi", 1, 18000, 160);
        addUnit(updates, "PRD003", "UNIT002", "Thung 20 goi", 20, 330000, 18);

        addProduct(updates, "PRD004", "8936036021000", "Gia vi", "Nuoc mam nhi", "", "Nuoc mam 500ml", true);
        addUnit(updates, "PRD004", "UNIT001", "Chai", 1, 45000, 90);

        addProduct(updates, "PRD005", "8935217400100", "Mi goi", "Mi tom chua cay", "", "Mi tom chua cay", true);
        addUnit(updates, "PRD005", "UNIT001", "Goi", 1, 4500, 500);
        addUnit(updates, "PRD005", "UNIT002", "Thung 30 goi", 30, 128000, 40);

        addProduct(updates, "PRD006", "8938505800109", "Do gia dung", "Nuoc rua chen 750ml", "", "Nuoc rua chen", true);
        addUnit(updates, "PRD006", "UNIT001", "Chai", 1, 38000, 75);
    }

    private static void addProduct(Map<String, Object> updates, String productId, String barcode, String category,
                                   String description, String imageUrl, String productName, boolean active) {
        String path = "Products/" + DEMO_USER_ID + "/" + productId + "/";
        updates.put(path + "userId", DEMO_USER_ID);
        updates.put(path + "productId", productId);
        updates.put(path + "barcode", barcode);
        updates.put(path + "categoryName", category);
        updates.put(path + "productDescription", description);
        updates.put(path + "productImageUrl", imageUrl);
        updates.put(path + "productName", productName);
        updates.put(path + "active", active);
        updates.put(path + "removed", false);
    }

    private static void addUnit(Map<String, Object> updates, String productId, String unitId, String unitName,
                                long convertRate, long unitPrice, long unitQuantity) {
        String path = "Units/" + DEMO_USER_ID + "/" + productId + "/" + unitId + "/";
        updates.put(path + "unitId", unitId);
        updates.put(path + "unitName", unitName);
        updates.put(path + "convertRate", convertRate);
        updates.put(path + "unitPrice", unitPrice);
        updates.put(path + "unitQuantity", unitQuantity);
    }

    private static void addDebtors(Map<String, Object> updates) {
        addDebtor(updates, "KH001", "Le Thi Anh", "0903000001", "125 Le Loi, Q1", "anh1@gmail.com", "12-03-1992", false, 1500000);
        addDebtor(updates, "KH002", "Tran Van Binh", "0903000002", "42 Nguyen Hue, Q1", "binh2@gmail.com", "21-07-1989", true, 550000);
        addDebtor(updates, "KH003", "Pham Thu Cuc", "0903000003", "8 Dinh Tien Hoang, Q1", "cuc3@gmail.com", "06-11-1995", false, 0);
    }

    private static void addDebtor(Map<String, Object> updates, String debtorId, String fullName, String phoneNumber,
                                  String address, String email, String dob, boolean gender, long remainingDebit) {
        String path = "Debtors/" + DEMO_USER_ID + "/" + debtorId + "/";
        updates.put(path + "fullName", fullName);
        updates.put(path + "phoneNumber", phoneNumber);
        updates.put(path + "address", address);
        updates.put(path + "email", email);
        updates.put(path + "dateOfBirth", dob);
        updates.put(path + "gender", gender);
        updates.put(path + "remainingDebit", remainingDebit);
    }

    private static void addInvoicesAndDetails(Map<String, Object> updates) {
        addInvoice(updates, "INV001", "", "01-05-2026", "08:22:30", 0, 20000, 180000, 200000, false);
        addInvoiceDetailForRetail(updates, "INV001");

        addInvoice(updates, "INV002", "KH001", "01-05-2026", "10:15:10", 320000, 0, 180000, 500000, false);
        addInvoiceDetailForKh001(updates, "INV002");

        addInvoice(updates, "INV003", "KH002", "02-05-2026", "14:05:45", 120000, 10000, 200000, 330000, false);
        addInvoiceDetailForKh002(updates, "INV003");

        addInvoice(updates, "INV004", "", "03-05-2026", "09:10:01", 0, 0, 76000, 76000, false);
        addInvoiceDetailForRetail2(updates, "INV004");

        addInvoice(updates, "INV005", "KH001", "03-05-2026", "11:30:40", 0, 5000, 245000, 250000, true);
        addInvoiceDetailForDraft(updates, "INV005");
    }

    private static void addInvoice(Map<String, Object> updates, String invoiceId, String debtorId,
                                   String invoiceDate, String invoiceTime, long debitAmount,
                                   long discount, long firstPaid, long total, boolean drafted) {
        String path = "Invoices/" + DEMO_USER_ID + "/" + invoiceId + "/";
        updates.put(path + "debtorId", debtorId);
        updates.put(path + "invoiceDate", invoiceDate);
        updates.put(path + "invoiceTime", invoiceTime);
        updates.put(path + "debtorImage", "");
        updates.put(path + "debitAmount", debitAmount);
        updates.put(path + "discount", discount);
        updates.put(path + "firstPaid", firstPaid);
        updates.put(path + "total", total);
        updates.put(path + "drafted", drafted);
    }

    private static void addInvoiceDetailForRetail(Map<String, Object> updates, String invoiceId) {
        addInvoiceDetailLine(updates, invoiceId, "PRD001", "Coca Cola", "UNIT001", "Lon", 12000, 5);
        addInvoiceDetailLine(updates, invoiceId, "PRD003", "Banh quy bo", "UNIT001", "Goi", 18000, 8);
    }

    private static void addInvoiceDetailForKh001(Map<String, Object> updates, String invoiceId) {
        addInvoiceDetailLine(updates, invoiceId, "PRD005", "Mi tom chua cay", "UNIT001", "Goi", 4500, 40);
        addInvoiceDetailLine(updates, invoiceId, "PRD004", "Nuoc mam 500ml", "UNIT001", "Chai", 45000, 4);
        addInvoiceDetailLine(updates, invoiceId, "PRD006", "Nuoc rua chen", "UNIT001", "Chai", 38000, 2);
    }

    private static void addInvoiceDetailForKh002(Map<String, Object> updates, String invoiceId) {
        addInvoiceDetailLine(updates, invoiceId, "PRD002", "Sua tuoi Vinamilk", "UNIT001", "Hop", 9000, 20);
        addInvoiceDetailLine(updates, invoiceId, "PRD001", "Coca Cola", "UNIT001", "Lon", 12000, 12);
    }

    private static void addInvoiceDetailForRetail2(Map<String, Object> updates, String invoiceId) {
        addInvoiceDetailLine(updates, invoiceId, "PRD005", "Mi tom chua cay", "UNIT001", "Goi", 4500, 8);
        addInvoiceDetailLine(updates, invoiceId, "PRD003", "Banh quy bo", "UNIT001", "Goi", 18000, 2);
    }

    private static void addInvoiceDetailForDraft(Map<String, Object> updates, String invoiceId) {
        addInvoiceDetailLine(updates, invoiceId, "PRD001", "Coca Cola", "UNIT001", "Lon", 12000, 10);
        addInvoiceDetailLine(updates, invoiceId, "PRD002", "Sua tuoi Vinamilk", "UNIT001", "Hop", 9000, 15);
    }

    private static void addInvoiceDetailLine(Map<String, Object> updates, String invoiceId, String productId,
                                             String productName, String unitId, String unitName,
                                             long unitPrice, long unitQuantity) {
        String productPath = "InvoiceDetail/" + DEMO_USER_ID + "/" + invoiceId + "/products/" + productId + "/";
        updates.put(productPath + "productName", productName);

        String unitPath = productPath + "units/" + unitId + "/";
        updates.put(unitPath + "unitId", unitId);
        updates.put(unitPath + "unitName", unitName);
        updates.put(unitPath + "unitPrice", unitPrice);
        updates.put(unitPath + "unitQuantity", unitQuantity);
        updates.put(unitPath + "convertRate", 1);
    }

    private static void addDebtPayments(Map<String, Object> updates) {
        String payment1Path = "DebtPayments/" + DEMO_USER_ID + "/KH001/DP001/";
        updates.put(payment1Path + "payAmount", 300000L);
        updates.put(payment1Path + "payDate", "02-05-2026");
        updates.put(payment1Path + "payTime", "17:12:21");
        updates.put(payment1Path + "debtBeforePay", 1800000L);
        updates.put(payment1Path + "invoiceDebtPayments/INV002/debitAmount", 320000L);
        updates.put(payment1Path + "invoiceDebtPayments/INV002/payMoney", 300000L);

        String payment2Path = "DebtPayments/" + DEMO_USER_ID + "/KH002/DP001/";
        updates.put(payment2Path + "payAmount", 150000L);
        updates.put(payment2Path + "payDate", "03-05-2026");
        updates.put(payment2Path + "payTime", "09:05:45");
        updates.put(payment2Path + "debtBeforePay", 700000L);
        updates.put(payment2Path + "invoiceDebtPayments/INV003/debitAmount", 120000L);
        updates.put(payment2Path + "invoiceDebtPayments/INV003/payMoney", 150000L);
    }

    private static void addFeedbacks(Map<String, Object> updates) {
        String feedback1 = "Feedbacks/DEMO_FEEDBACK_001/";
        updates.put(feedback1 + "userID", DEMO_USER_ID);
        updates.put(feedback1 + "fullName", "Chu cua hang Demo");
        updates.put(feedback1 + "phoneNumber", DEMO_USER_ID);
        updates.put(feedback1 + "content", "Ung dung de dung, can them dashboard tong quan chi tiet hon.");
        updates.put(feedback1 + "rating", 4L);

        String feedback2 = "Feedbacks/DEMO_FEEDBACK_002/";
        updates.put(feedback2 + "userID", DEMO_USER_ID);
        updates.put(feedback2 + "fullName", "Le Thi Anh");
        updates.put(feedback2 + "phoneNumber", "0903000001");
        updates.put(feedback2 + "content", "Tinh nang tao don no nhanh va tien loi.");
        updates.put(feedback2 + "rating", 5L);
    }
}
