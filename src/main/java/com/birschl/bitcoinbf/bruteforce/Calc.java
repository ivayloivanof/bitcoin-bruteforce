package com.birschl.bitcoinbf.bruteforce;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;

public class Calc {

    static NetworkParameters np = new MainNetParams();

    public static Address getAddressFromPrivateKey(String privateKeyBase58) {
        DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(np, privateKeyBase58);
        ECKey key = dumpedPrivateKey.getKey();
        return key.toAddress(np);
    }


}
