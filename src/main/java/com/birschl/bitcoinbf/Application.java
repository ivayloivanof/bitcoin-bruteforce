package com.birschl.bitcoinbf;

import com.birschl.bitcoinbf.bruteforce.BruteForce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class Application implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    private AddressDao dao;

    @Autowired
    private BruteForce bruteForce;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("blockChainImportJob")
    private Job blockChainImportJob;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.containsOption("import")) {
            dao.dropAndCreateDb(); // TODO also drop the spring batch tables
            jobLauncher.run(blockChainImportJob, new JobParameters());
         }

       // bruteForce.run();
    }
}
