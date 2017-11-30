package com.birschl.bitcoinbf.addressimport;

import java.math.BigInteger;

class ProgressLogger {

    private static BigInteger addressCounter = BigInteger.ZERO;

    private static long ts = 0;

    static String logProgress(String address) {
        addressCounter = addressCounter.add(BigInteger.ONE);

        long now = System.currentTimeMillis();
        if (ts + 1000 < now) {
            ts = now;
            System.out.println(addressCounter + " Addresses imported");
        }


        return address;
    }
}
