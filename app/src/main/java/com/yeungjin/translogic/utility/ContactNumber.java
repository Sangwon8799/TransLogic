package com.yeungjin.translogic.utility;

public class ContactNumber {
    private ContactNumber() { }

    public static String parse(String number) {
        StringBuilder formatted = new StringBuilder();

        if (number.startsWith("02")) {
            formatted.append(number.substring(0, 2)).append("-");
            formatted.append(number.substring(2, 6)).append("-");
            formatted.append(number.substring(6));
        } else {
            switch (number.length()) {
                case 11:
                    formatted.append(number.substring(0, 3)).append("-");
                    formatted.append(number.substring(3, 7)).append("-");
                    formatted.append(number.substring(7));
                    break;
                case 10:
                    formatted.append(number.substring(0, 3)).append("-");
                    formatted.append(number.substring(3, 6)).append("-");
                    formatted.append(number.substring(6));
                    break;
            }
        }

        return formatted.toString();
    }
}
