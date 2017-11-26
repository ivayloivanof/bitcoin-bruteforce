package com.birschl.bitcoinbf.blockchain;

import com.birschl.bitcoinbf.AddressDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AddressWriter implements ItemWriter<Set<String>> {

    private static final Logger LOG = LoggerFactory.getLogger(AddressWriter.class);

    @Autowired
    private AddressDao dao;

    @Autowired
    private TransactionReader blockReader;

    @Override
    public void write(List<? extends Set<String>> addresses) throws Exception {
        System.out.println("Overall " + blockReader.getBlockReadNumber() + " blocks read");
        List<String> addrStrings = addresses.stream()
                .flatMap(addrSet -> addrSet.stream())
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Persisting " + addrStrings.size() + " addresses");
        dao.persistAll(addrStrings);
    }
}
