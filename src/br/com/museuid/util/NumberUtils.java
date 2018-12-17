package br.com.museuid.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class NumberUtils {
    public static String formatValue(float value) {
        String arr[] = {"", "K", "M", "B", "T", "P", "E"};
        int index = 0;
        while ((value / 1000) >= 1) {
            value = value / 1000;
            index++;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        return String.format("%s %s", decimalFormat.format(value), arr[index]);
    }

    public static String convertVNDMoneyToDecimaFormatString(int money){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat(); // Set your desired format here.
        df.setDecimalFormatSymbols(symbols);
        return df.format(money);
    }
}
