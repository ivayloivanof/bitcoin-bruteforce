package com.birschl.bitcoinbf.bloomfilter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

class AddressFileReader {

    static Stream<String> read(String addressFilesFolder) {
        try {
            return Files
                    .walk(Paths.get(addressFilesFolder))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().matches("addr[0-9]+\\.dat"))
                    .sorted()
                    .map(AddressFileReader::logFileName)
                    .flatMap(AddressFileReader::readAddressFile);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading address files", e);
        }
    }

    private static Stream<String> readAddressFile(Path path) {
        try {
            return Files.lines(path);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading address files", e);
        }
    }

    private static Path logFileName(Path path) {
        System.out.println("Import address file: " + path.getFileName());
        return path;
    }

}
