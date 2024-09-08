package com.clush.todolist.utility;

import java.text.DecimalFormat;

public class StatisticsFormatUtils {

	private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public static double format(double value) {
        return Double.parseDouble(decimalFormat.format(value));
    }
}
