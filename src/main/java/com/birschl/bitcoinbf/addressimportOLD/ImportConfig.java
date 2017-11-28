package com.birschl.bitcoinbf.addressimportOLD;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.params.MainNetParams;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Set;

@Configuration
@EnableBatchProcessing
public class ImportConfig extends DefaultBatchConfigurer {

    public static NetworkParameters NETWORK_PARAMS = new MainNetParams();

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("${blockchain.file}")
    private String blockChainFile;

    @Bean
    //@StepScope TODO This should also be step scope otherwise the related beans stay in memory and will not be gc'ed
    public Step importAddressesStep() {
        return stepBuilderFactory.get("addressesImportStep")
                .<Transaction, Set<String>>chunk(100000)
                .reader(blockReader())
                .processor(blockProcessor())
                .writer(addressWriter())
                .listener(importStepListener())
                .build();
    }

    @Bean
    @StepScope
    public TransactionReader blockReader() {
        return new TransactionReader(blockChainFile);
    }

    @Bean
    @StepScope
    public TransactionProcessor blockProcessor() {
        return new TransactionProcessor();
    }

    @Bean
    @StepScope
    public AddressWriter addressWriter() {
        return new AddressWriter();
    }

    @Bean
    @StepScope
    public ImportStepListener importStepListener() {
        return new ImportStepListener();
    }

    @Bean
    public Job blockChainImportJob() {
        return jobBuilderFactory.get("blockChainImportJob")
                .start(importAddressesStep())
                .build();
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        // Prevent spring batch from using the database
    }
}
