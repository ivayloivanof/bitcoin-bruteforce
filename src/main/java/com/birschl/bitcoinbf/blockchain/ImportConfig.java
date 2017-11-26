package com.birschl.bitcoinbf.blockchain;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.params.MainNetParams;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class ImportConfig {

    static NetworkParameters NETWORK_PARAMS = new MainNetParams();

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("${blockchain.file}")
    private String blockChainFile;

    @Bean
    public Step importAddressesStep() {
        return stepBuilderFactory.get("addressesImportStep")
                .<Transaction, Set<String>>chunk(10000)
                .reader(blockReader())
                .processor(blockProcessor())
                .writer(addressWriter())
                .build();
    }

    @Bean
    public TransactionReader blockReader() {
        return new TransactionReader(blockChainFile);
    }

    @Bean
    public TransactionProcessor blockProcessor() {
        return new TransactionProcessor();
    }

    @Bean
    public AddressWriter addressWriter() {
        return new AddressWriter();
    }

    @Bean
    public Job blockChainImportJob() {
        return jobBuilderFactory.get("blockChainImportJob")
                .start(importAddressesStep())
                .build();
    }
}
