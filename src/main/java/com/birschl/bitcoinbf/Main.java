package com.birschl.bitcoinbf;

import com.birschl.bitcoinbf.addressimport.AddressImporter;
import com.birschl.bitcoinbf.bloomfilter.BloomFilter;
import com.birschl.bitcoinbf.bruteforce.BruteForce;

public class Main {
    public static void main(String[] args) {

        // Import addesses from the block chain into files
        AddressImporter addressImporter = new AddressImporter("/Volumes/NAS BACKUP/blockchain/blocks", "/Users/x/address-files");
        addressImporter.startImport();

        // Build the bloom filter from the imported address files
        BloomFilter.buildFromAddressFiles("/Users/x/address-files", "filter.dat");

        // Run brute force with bloom filter loaded from file
        BruteForce bf = new BruteForce();
        bf.run("filter.dat");
    }
}
