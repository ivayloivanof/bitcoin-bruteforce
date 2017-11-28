package com.birschl.bitcoinbf.addressimportOLD;

import com.birschl.bitcoinbf.AddressDao;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

public class AddressWriter implements ItemWriter<Set<String>> {

    private static final Logger LOG = LoggerFactory.getLogger(AddressWriter.class);

    @Autowired
    private AddressDao dao;

    @Autowired
    private TransactionReader blockReader;

    private long addressCounter=0;


    private BloomFilter<String> filter = BloomFilter.create(
            Funnels.stringFunnel(Charset.forName("UTF-8")), // TODO
            100000000, // TODO When the size is to low, then the bloom filter will not work like expected. Unfortunately the size can just be known, after the filter has been generated. Therefore you have to runn the app twice and adjust the number manually
            0.001);

    @Override
    public void write(List<? extends Set<String>> addresses) throws Exception {

        addresses.stream()
                .flatMap(addrSet -> addrSet.stream())
                .distinct()
                .forEach(a -> {
                    addressCounter++;
                    filter.put(a);
                });

        System.out.println( blockReader.getBlockReadNumber()
                + " blocks and "+ addressCounter+" addresses imported");
               // .collect(Collectors.toList());
        /*
        System.out.println("Persisting " + addrStrings.size() + " addresses");
        dao.persistAll(addrStrings);

        */
    }

    BloomFilter<String> getBloomFilter(){
        return filter;
    }
}
