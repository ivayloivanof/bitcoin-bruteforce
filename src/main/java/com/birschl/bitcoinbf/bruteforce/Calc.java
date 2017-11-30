package com.birschl.bitcoinbf.bruteforce;

import org.bitcoinj.core.Base58;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;

import java.math.BigInteger;

public class Calc {

    static NetworkParameters np = new MainNetParams();

    public static String getAddressFromPrivateKey(String privateKeyBase58) {
        DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(np, privateKeyBase58);
        ECKey key = dumpedPrivateKey.getKey();
        return key.toAddress(np).toString();
    }

    //TODO verify if this is really working in any cases !!!!!
    public static String getAddressFromPrivateKey(BigInteger privateKey) {
        return getAddressFromPrivateKey(Base58.encode(privateKey.toByteArray()));
    }

}
