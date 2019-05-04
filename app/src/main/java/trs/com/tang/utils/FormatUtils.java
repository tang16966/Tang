package trs.com.tang.utils;

import java.text.DecimalFormat;

public class FormatUtils {
    private static DecimalFormat decimalFormat;

    public static String getdecimal(float number){
        if (decimalFormat == null){
            decimalFormat = new DecimalFormat("0.00");
        }
        return decimalFormat.format(number);
    }
}
