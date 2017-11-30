package com.birschl.bitcoinbf.bruteforce;

import com.birschl.bitcoinbf.Constants;
import org.bitcoinj.core.ECKey;

import java.math.BigInteger;
import java.util.function.Supplier;

class InputKeySupplier implements Supplier<String> {

    private BigInteger startPrivateKey = new BigInteger("00000000000000000000000000000000000000000000000000000000000000002", 16);

    private final static Object lock = new Object();

    private long startTs = 0;

    private BigInteger keyCount = new BigInteger("0");

    private long lastLogTs = 0;

    @Override
    public String get() {
        synchronized (lock) {
            ECKey ecKey = ECKey.fromPrivate(startPrivateKey);
            String privateKeyToCheck = ecKey.getPrivateKeyAsWiF(Constants.NETWORK_PARAMS);
            startPrivateKey = startPrivateKey.add(BigInteger.ONE);
            keyCount = keyCount.add(BigInteger.ONE);
            logKeyRate();
            return privateKeyToCheck;
        }
    }

    // TODO clean that up
    private void logKeyRate() {
        long now = System.currentTimeMillis();

        if (startTs == 0)
            startTs = now;
        if (startTs <= 1000)
            return;

        if (lastLogTs + 10000 < now) {
            lastLogTs = now;
            long runningSince = (now - startTs) / 1000;
            if (runningSince == 0)
                return;
            BigInteger keysPerSecond = keyCount.divide(BigInteger.valueOf(runningSince));
            System.out.println(keysPerSecond + " Keys/s - Latest key base: " + startPrivateKey);
        }
    }
}
