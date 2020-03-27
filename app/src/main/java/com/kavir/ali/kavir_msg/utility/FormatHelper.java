package com.kavir.ali.kavir_msg.utility;

public class FormatHelper {

    private static String[] persianNumbers = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};
    private static String[] engNumbers = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};


    public static String toPersianNumber(String text) {
        if (text == null){
            return "";
        }
        if (text.length() == 0 || text.equals("")) {
            return "";
        }
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('0' <= c && c <= '9') {
                int number = Integer.parseInt(String.valueOf(c));
                out += persianNumbers[number];
            } else if (c == '٫') {
                out += '،';
            } else {
                out += c;
            }


        }
        return out;
    }

    public static String toEngNumber(String text) {
        if (text.length() == 0) {
            return "";
        }
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('۰' <= c && c <= '۹') {
                int number = Integer.parseInt(String.valueOf(c));
                out += engNumbers[number];
            } else if (c == '،') {
                out += '٫';
            } else {
                out += c;
            }


        }
        return out;
    }
}

