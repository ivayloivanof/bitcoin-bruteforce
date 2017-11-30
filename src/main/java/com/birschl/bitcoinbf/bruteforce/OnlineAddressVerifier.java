package com.birschl.bitcoinbf.bruteforce;

import info.blockchain.api.APIException;
import info.blockchain.api.blockexplorer.BlockExplorer;
import info.blockchain.api.blockexplorer.entity.Address;

import java.io.IOException;
import java.util.function.Function;

class OnlineAddressVerifier implements Function<String, Match> {

    private BlockExplorer blockExplorer = new BlockExplorer();

    public OnlineAddressVerifier() {
        // info.addressimportOLD.api.HttpClient.TIMEOUT_MS = 5000;
    }

    @Override
    public Match apply(String potentialMatchingPrivateKey) {
        try {
            String addressStr = Calc.getAddressFromPrivateKey(potentialMatchingPrivateKey).toString();
            Address address = blockExplorer.getAddress(addressStr.toString());
            if (address.getTotalReceived() != 0 || address.getTotalSent() != 0 || address.getTxCount() != 0) {
                return new Match(address, potentialMatchingPrivateKey);
            }
        } catch (IOException | APIException e) {
            throw new RuntimeException("Error during online validation for private key " + potentialMatchingPrivateKey, e);
        }
        return null;
    }
}
