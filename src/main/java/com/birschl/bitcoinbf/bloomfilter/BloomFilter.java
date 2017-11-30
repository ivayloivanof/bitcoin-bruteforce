package com.birschl.bitcoinbf.bloomfilter;

import com.google.common.hash.Funnels;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.function.Predicate;

public class BloomFilter implements Predicate<String> {

    private static final Logger LOG = LoggerFactory.getLogger(BloomFilter.class);

    private com.google.common.hash.BloomFilter filter;

    private BloomFilter() {
    }

    public static BloomFilter importFromFile(String bloomFilterFile) {
        try {
            BloomFilter f = new BloomFilter();
            f.fromFile(bloomFilterFile);
            return f;
        } catch (IOException e) {
            throw new RuntimeException("Error while reading bloom filter file", e);
        }
    }

    public static BloomFilter buildFromAddressFiles(String addressFilesFolder, String bloomFilterFile) {
        try {
            BloomFilter f = new BloomFilter();
            f.fromAddressFiles(addressFilesFolder, bloomFilterFile);
            return f;
        } catch (IOException e) {
            throw new RuntimeException("Error while creating bloom filter", e);
        }
    }

    private void fromFile(String bloomFilterFile) throws IOException {
        System.out.println("Loading bloom filter from file " + bloomFilterFile);
        FileInputStream is = FileUtils.openInputStream(new File(bloomFilterFile));
        filter = com.google.common.hash.BloomFilter.readFrom(is, Funnels.stringFunnel(Charset.forName("UTF-8")));
        System.out.println("Bloom filter with approximately " + filter.approximateElementCount() + " addresses loaded");
    }

    private void fromAddressFiles(String addressFilesFolder, String bloomFilterFile) throws IOException {
        System.out.println("Counting addresses in folder: " + addressFilesFolder);
        long numAddresses = AddressFileReader.read(addressFilesFolder).count();
        System.out.println("Found " + numAddresses + " addresses. Building bloom filter.");
        if (numAddresses > Integer.MAX_VALUE)
            throw new RuntimeException("Address count exceeds bloom filter max");
        this.filter = initBloomFilter((int) numAddresses);
        AddressFileReader
                .read(addressFilesFolder)
                .forEach(filter::put);
        System.out.println("Bloom filter created. It contains approximately " + filter.approximateElementCount() + " addresses");
        writeFilterToFile(bloomFilterFile);
    }

    private void writeFilterToFile(String bloomFilterFile) throws IOException {
        System.out.println("Writing bloom filter to file " + bloomFilterFile);
        File file = new File(bloomFilterFile);
        FileOutputStream fos = FileUtils.openOutputStream(file, false);
        filter.writeTo(fos);
        fos.close();
    }


    private com.google.common.hash.BloomFilter initBloomFilter(int expectedInsertions) {
        return com.google.common.hash.BloomFilter.create(
                Funnels.stringFunnel(Charset.forName("UTF-8")), // TODO
                expectedInsertions,
                0.001);
    }

    @Override
    public boolean test(String address) {
        return filter.mightContain(address);
    }

}
