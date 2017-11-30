package com.birschl.bitcoinbf.addressimport;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

class AddressFileWriter implements Closeable {

    private final FileWriter fileWriter;

    AddressFileWriter(Path addressFile) {
        // TODO delete file if exist
        try {
            fileWriter = new FileWriter(addressFile.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Error while opening address file", e);
        }

    }

    void writeLine(String address) {
        try {
            fileWriter.write(address + System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException("Error while writing address file", e);
        }
    }

    @Override
    public void close() {
        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Error while writing address file", e);
        }
    }
}
