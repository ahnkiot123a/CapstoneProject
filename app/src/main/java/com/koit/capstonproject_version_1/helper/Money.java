package com.koit.capstonproject_version_1.helper;

import java.text.NumberFormat;
import java.util.Locale;

public class Money {

    private static Money mInstance;

    public Money() {
    }

    public static Money getInstance() {
        if (mInstance == null) {
            mInstance = new Money();
        }
        return mInstance;
    }

    public String formatVN(long money) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat vn = NumberFormat.getInstance(Locale.US);
        return vn.format(money);
    }

    public long reFormatVN(String money) {
        if (money.contains(".")) {
            money = money.replaceAll("[.]", "");
        }
        String[] array = money.split("[,]");
        String formatMoney = "";
        for (int i = 0; i < array.length; i++) {
            formatMoney = formatMoney.concat(array[i]);
        }
        if (!formatMoney.equals(""))
            return Long.parseLong(formatMoney);
        else return 0;
    }

    public long reFormatVND(String money) {
        String[] a = money.split("\\s");
        String[] array = a[0].split("[,]");
        String formatMoney = "";
        for (int i = 0; i < array.length; i++) {
            formatMoney = formatMoney.concat(array[i]);
        }
        if (!formatMoney.equals(""))
            return Long.parseLong(formatMoney);
        else return 0;
    }


}
