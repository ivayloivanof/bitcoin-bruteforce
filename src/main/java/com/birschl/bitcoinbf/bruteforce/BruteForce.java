package com.birschl.bitcoinbf.bruteforce;

import com.birschl.bitcoinbf.bloomfilter.BloomFilter;

import java.util.stream.Stream;

public class BruteForce {


    public void run(String bloomFilterFile) {
        BloomFilter bloomFilter = BloomFilter.importFromFile(bloomFilterFile);

        Stream.generate(new InputKeySupplier())
                .parallel()
                .map(Calc::getAddressFromPrivateKey)
                .filter(bloomFilter)
                .map(new OnlineAddressVerifier())
                .filter(m -> m != null)
                .forEach(match -> {
                    System.out.println("MATCH FOUND! PK: "
                            + match.getPrivateKey()
                            + " ADDR: "
                            + match.getAddress().getAddress()
                            + " BALANCE: "
                            + match.getAddress().getFinalBalance());
                });
    }
}
