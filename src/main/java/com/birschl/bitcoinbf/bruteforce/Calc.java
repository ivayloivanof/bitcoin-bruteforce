package com.birschl.bitcoinbf.bruteforce;

import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;

import java.math.BigInteger;

public class Calc {

    static NetworkParameters np = new MainNetParams();

    public static Address getAddressFromPrivateKey(String privateKeyBase58) {
        DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(np, privateKeyBase58);
        ECKey key = dumpedPrivateKey.getKey();
        return key.toAddress(np);
    }

    //TODO verify if this is really working in any cases !!!!!
    public static Address getAddressFromPrivateKey(BigInteger privateKey) {
        return getAddressFromPrivateKey(Base58.encode(privateKey.toByteArray()));
    }

}
