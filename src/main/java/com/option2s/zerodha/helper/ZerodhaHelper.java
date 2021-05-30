package com.option2s.zerodha.helper;

public class ZerodhaHelper {

    public static void main(String[] args) throws Exception {

        double value = 31625.5;

        System.out.println(getNearest100(value));
        System.out.println(getNearest50(value));

    }

    public static int getNearest100(double value) {
        return (int) Math.round(value/100.0) * 100;
    }

    public static int getNearest50(double value) {
        return (int) Math.round(value/50.0) * 50;
    }

}
