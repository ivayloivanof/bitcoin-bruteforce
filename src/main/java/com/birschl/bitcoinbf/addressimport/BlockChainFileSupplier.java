package com.birschl.bitcoinbf.addressimport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Supplies all ID's of the bitcoin-core block chain files that has not already been imported into the address-files folder.
 * It also supplies the ID of the last imported file so that it will be updated.
 */
public class BlockChainFileSupplier implements Iterable<String> {

    private final Iterator<String> fileIdsToImportOrUpdate;

    public BlockChainFileSupplier(String blockChainFolder, String addressFilesFolder) {
        try {
            List<String> fileIdsToImportOrUpdate = findFileIdsToImportOrUpdate(blockChainFolder, addressFilesFolder);
            this.fileIdsToImportOrUpdate = fileIdsToImportOrUpdate.iterator();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading addresses and block chain", e);
        }
    }

    @Override
    public Iterator<String> iterator() {
        return fileIdsToImportOrUpdate;
    }

    // TODO This should be tested
    private List<String> findFileIdsToImportOrUpdate(String blockChainFolder, String addressFilesFolder) throws IOException {
        List<String> blockChainFiles = Files
                .walk(Paths.get(blockChainFolder))
                .filter(Files::isRegularFile)
                .map(p -> p.getFileName().toString())
                .filter(f -> f.matches("blk[0-9]+\\.dat"))
                .map(f -> f.replace("blk", "").replace(".dat", ""))
                .sorted()
                .collect(Collectors.toList());
        List<String> addressFiles = Files
                .walk(Paths.get(addressFilesFolder))
                .filter(Files::isRegularFile)
                .map(p -> p.getFileName().toString())
                .filter(f -> f.matches("addr[0-9]+\\.dat"))
                .map(f -> f.replace("addr", "").replace(".dat", ""))
                .sorted()
                .collect(Collectors.toList());

        List<String> files = blockChainFiles.stream()
                .filter(f -> !addressFiles.contains(f))
                .collect(Collectors.toList());

        // Also update the latest imported address file
        if (addressFiles.size() > 0) {
            files.add(addressFiles.get(addressFiles.size()-1));
        }

        return files.stream().distinct().sorted().collect(Collectors.toList());
    }


}
