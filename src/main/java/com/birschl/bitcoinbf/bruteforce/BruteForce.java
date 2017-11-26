package com.birschl.bitcoinbf.bruteforce;

import com.birschl.bitcoinbf.AddressDao;
import org.bitcoinj.core.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BruteForce {

    @Autowired
    private AddressDao dao;

    public void run() {
        Address addr = Calc.getAddressFromPrivateKey("68nc4o8iQrymZgj9hHZwzwsmQrhpPjLeidUTqg9j5J6edntrXng");
        System.out.println("checking "+addr);
        if (dao.contains(addr.toString())) {
            System.out.println("You discovered a private key");
        }
        //System.out.println("Address:   "+addr);
        //System.out.println("Should be: 1Hg3pEHJgovrHpbS8hGUBCroFLutGucovF");
    }
}
