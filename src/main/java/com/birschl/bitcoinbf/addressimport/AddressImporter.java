package com.birschl.bitcoinbf.addressimport;

import com.birschl.bitcoinbf.Constants;
import org.bitcoinj.core.Context;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.utils.BlockFileLoader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.StreamSupport;

public class AddressImporter {


    private static String blockChainFolder;

    private static String addressesFolder;

    public AddressImporter(String blockChainFolder, String addressesFolder) {
        this.blockChainFolder = blockChainFolder;
        this.addressesFolder = addressesFolder;
    }

    public void startImport() {

        for (String fileId : new BlockChainFileSupplier(blockChainFolder, addressesFolder)) {
            Path blockFile = Paths.get(blockChainFolder, "blk" + fileId + ".dat");
            Path addressFile = Paths.get(addressesFolder, "addr" + fileId + ".dat");

            System.out.println("Extracting addresses from " + blockFile.getFileName() + " to " + addressFile.getFileName());

            Context.getOrCreate(MainNetParams.get());
            BlockFileLoader blockFileLoader = new BlockFileLoader(Constants.NETWORK_PARAMS, Arrays.asList(blockFile.toFile()));

            AddressFileWriter addressFileWriter = new AddressFileWriter(addressFile);

            StreamSupport
                    .stream(blockFileLoader.spliterator(), false)
                    .flatMap(block -> block.getTransactions().stream())
                    .flatMap(TransactionProcessor::process)
                    .map(ProgressLogger::logProgress)
                    .distinct()
                    .forEach(addressFileWriter::writeLine);

            addressFileWriter.close();

            System.out.println("Import finished " + addressFile.getFileName());

            // TODO Check bloom filter. In case iterate over all address files and remove duplicates???


        }
    }


}
