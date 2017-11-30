package com.birschl.bitcoinbf.addressimport;

import com.birschl.bitcoinbf.Constants;
import org.bitcoinj.core.*;
import org.bitcoinj.script.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

class TransactionProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionProcessor.class);

    static Stream<String> process(Transaction tx) {

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
        return addresses.stream();
    }

    static Address getInputAddress(TransactionInput in) {
        try {
            if (!in.isCoinBase()) {
                return in.getFromAddress();
                //addr = in.getFromAddress().toBase58();
                // System.out.println(" <--- IN: "+addr);
            }
        } catch (ScriptException e) {
            // TODO
            // LOG.error("Error while reading transaction input address", e);
        }
        return null;
    }

    static Address getOutputAddress(TransactionOutput out) {
        try {
            Script script = out.getScriptPubKey();
            if (script.isSentToAddress() || script.isPayToScriptHash()) {
                return script.getToAddress(Constants.NETWORK_PARAMS);
            }
        } catch (ScriptException e) {
            // TODO
            //LOG.error("Error while reading transaction output address", e);
        }
        return null;
    }
}
