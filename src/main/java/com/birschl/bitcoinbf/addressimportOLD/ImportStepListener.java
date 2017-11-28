package com.birschl.bitcoinbf.addressimportOLD;

import com.google.common.hash.BloomFilter;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImportStepListener implements StepExecutionListener {

    @Autowired
    private AddressWriter addressWriter;

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        FileOutputStream fos = null;
        try {
            File file = new File("bloom-filter.dat");
            fos = FileUtils.openOutputStream(file, false);
            BloomFilter<String> filter = addressWriter.getBloomFilter();
            System.out.println("Approximately " + filter.approximateElementCount()+" addresses imported");

            filter.writeTo(fos);
            return ExitStatus.COMPLETED;
        } catch (IOException e1) {
            e1.printStackTrace();
            return ExitStatus.FAILED;
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                return ExitStatus.FAILED;
            }
        }
    }
}
