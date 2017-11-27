package com.birschl.bitcoinbf.bruteforce;

import com.birschl.bitcoinbf.addressimport.ImportConfig;
import org.bitcoinj.core.ECKey;

import java.math.BigInteger;
import java.util.function.Supplier;

public class InputKeySupplier implements Supplier<String> {

    private BigInteger startPrivateKey = new BigInteger("00000000000000000000000000000000000000000000000000000000000000002", 16);   // exactly 32-bytes

    @Override
    public String get() {
        ECKey ecKey = ECKey.fromPrivate(startPrivateKey);
        String privateKeyToCheck = ecKey.getPrivateKeyAsWiF(ImportConfig.NETWORK_PARAMS);
        startPrivateKey = startPrivateKey.add(BigInteger.ONE);
        return privateKeyToCheck;
    }
}
