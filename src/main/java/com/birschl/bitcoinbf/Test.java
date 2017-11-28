package com.birschl.bitcoinbf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Test {


    public static void main(String[] args) throws IOException {
        List<Path> blockChainFiles = Files
                .walk(Paths.get("/Volumes/NAS BACKUP/blockchain/blocks"))
                .filter(Files::isRegularFile)
                .filter(f -> f.getFileName().toString().matches("blk[0-9]+\\.dat"))
                .filter(f -> {
                    System.out.println(f);
                    return true;
                })
                .collect(Collectors.toList());
    }
}
