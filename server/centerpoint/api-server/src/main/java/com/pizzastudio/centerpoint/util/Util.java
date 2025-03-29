package com.pizzastudio.centerpoint.util;

public class Util {
    public static int[] StringArrToIntArr(String[] s) {
        int[] result = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            result[i] = Integer.parseInt(s[i]);
        }
        return result;
    }


    public static double[] calculateSD(Double numArray[])
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;

        for(double num : numArray) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        double[] retvalue = new double[2];
        retvalue[0] = mean;
        retvalue[1] = Math.sqrt(standardDeviation/length);
        return retvalue;
    }


}

