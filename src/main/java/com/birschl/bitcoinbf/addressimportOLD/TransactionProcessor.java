package com.birschl.bitcoinbf.addressimportOLD;

import org.bitcoinj.core.*;
import org.bitcoinj.script.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.HashSet;
import java.util.Set;

public class TransactionProcessor implements ItemProcessor<Transaction, Set<String>> {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionProcessor.class);

    @Override
    public Set<String> process(Transaction tx) throws Exception {
        Set<String> addresses = new HashSet<>();
        for (TransactionOutput out : tx.getOutputs()) {
            Address addr = getOutputAddress(out);
            if (addr != null)
                addresses.add(addr.toString());
        }
        for (TransactionInput input : tx.getInputs()) {
            Address addr = getInputAddress(input);
            if (addr != null)
                addresses.add(addr.toString());
        }
        return addresses;
    }

    private Address getInputAddress(TransactionInput in) {
        try {
            if (!in.isCoinBase()) {
                return in.getFromAddress();
                //addr = in.getFromAddress().toBase58();
                // System.out.println(" <--- IN: "+addr);
            }
        } catch (ScriptException e) {
           // LOG.error("Error while reading transaction input address", e);
        }
        return null;
    }

    private Address getOutputAddress(TransactionOutput out) {
        try {
            Script script = out.getScriptPubKey();
            if (script.isSentToAddress() || script.isPayToScriptHash()) {
                return script.getToAddress(ImportConfig.NETWORK_PARAMS);
            }
        } catch (ScriptException e) {
           // LOG.error("Error while reading transaction output address", e);
        }
        return null;
    }


}
