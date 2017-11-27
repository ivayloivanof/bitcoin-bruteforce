package com.birschl.bitcoinbf.bruteforce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class BruteForce {


    @Value("${bloomfilter.file}")
    private String bloomFilterFile;

    public void run(){

        Stream.generate(new InputKeySupplier())
                .parallel()
                .filter(new AddressBloomFilter(bloomFilterFile))
                .map(new OnlineAddressVerifier())
                .filter(m -> m!=null)
                .forEach(match-> {
                    System.out.println("MATCH FOUND! PK: "
                            +match.getPrivateKey()
                            +" ADDR: "
                            + match.getAddress().getAddress()
                            +" BALANCE: "
                            +match.getAddress().getFinalBalance());
                });
    }
}
