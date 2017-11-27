package com.birschl.bitcoinbf.addressimport;

import org.bitcoinj.core.Context;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.utils.BlockFileLoader;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TransactionReader implements ItemReader<Transaction> {

    private BlockFileLoader blockFileLoader;

    private Iterator<Transaction> txIterator;

    private int blockCounter = 0;

    public TransactionReader(String blockChainFile){
        Context.getOrCreate(MainNetParams.get());
        List<File> blockChainFiles = new ArrayList<>();
        blockChainFiles.add(new File(blockChainFile));
        blockFileLoader = new BlockFileLoader(ImportConfig.NETWORK_PARAMS, blockChainFiles);
    }


    @Override
    public Transaction read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
        if (txIterator != null && txIterator.hasNext()) {
            return txIterator.next();
        }

        while (blockFileLoader.hasNext()) {
            blockCounter++;
            txIterator = blockFileLoader.next().getTransactions().iterator();
            if (txIterator.hasNext()) {
                return txIterator.next();
            }
        }

        return null;
    }

    public int getBlockReadNumber() {
        return blockCounter;
    }

}
