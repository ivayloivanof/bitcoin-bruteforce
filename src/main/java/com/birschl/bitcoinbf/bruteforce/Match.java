package com.birschl.bitcoinbf.bruteforce;

import info.blockchain.api.blockexplorer.entity.Address;

class Match {

    private Address address;

    private String privateKey;

    public Match(Address address, String privateKey) {
        this.address = address;
        this.privateKey = privateKey;
    }

    public Address getAddress() {
        return address;
    }

    public String getPrivateKey() {
        return privateKey;
    }
}
