package com.birschl.bitcoinbf.bruteforce;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;

public class AddressBloomFilter {

    public void create(int size) {
        // TODO it may be more efficient to store the addresses as byte[] instead of string
        BloomFilter<String> filter = BloomFilter.create(
                Funnels.stringFunnel(Charset.forName("UTF-8")),
                size,
                0.001);


    }
}
