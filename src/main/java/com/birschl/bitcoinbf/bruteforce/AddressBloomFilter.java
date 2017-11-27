package com.birschl.bitcoinbf.bruteforce;

import com.google.common.hash.Funnels;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.function.Predicate;


public class AddressBloomFilter implements Predicate<String> {

    private static final Logger LOG = LoggerFactory.getLogger(AddressBloomFilter.class);

    private com.google.common.hash.BloomFilter<String> bloomFilter = null;

    public AddressBloomFilter(String bloomFilterFile) {
        try {
            System.out.println("Loading bloom filter from file");
            FileInputStream is = FileUtils.openInputStream(new File(bloomFilterFile));
            bloomFilter = com.google.common.hash.BloomFilter.readFrom(is, Funnels.stringFunnel(Charset.forName("UTF-8")));
            System.out.println("Bloom filter with approximately " + bloomFilter.approximateElementCount() + " addresses loaded");

        } catch (IOException e) {
            throw new RuntimeException("Error while reading bloom filter file", e);
        }
    }

    @Override
    public boolean test(String privateKeyToCheck) {
        String addressOfPrivKey = Calc.getAddressFromPrivateKey(privateKeyToCheck).toString();
        boolean res = bloomFilter.mightContain(addressOfPrivKey);
        if (res) {
            System.out.println("Potential match found! ADDR: " + addressOfPrivKey + " PRIV: " + privateKeyToCheck);
            return true;
        }
        return false;
    }


}
