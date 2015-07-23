/**
 * Copyright (C) 2014 uniknow. All rights reserved.
 * 
 * This Java class is subject of the following restrictions:
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: "This product includes software
 * developed by uniknow." Alternately, this acknowledgment may appear in the
 * software itself, if and wherever such third-party acknowledgments normally
 * appear.
 * 
 * 4. The name ''uniknow'' must not be used to endorse or promote products
 * derived from this software without prior written permission.
 * 
 * 5. Products derived from this software may not be called ''UniKnow'', nor may
 * ''uniknow'' appear in their name, without prior written permission of
 * uniknow.
 * 
 * THIS SOFTWARE IS PROVIDED ''AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL WWS OR ITS
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.uniknow.spring.compensatable.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.uniknow.spring.compensatable.api.CompensatableTransactionManager;
import org.uniknow.spring.compensatable.api.CompensationHandler;

import java.util.UUID;

/**
 * Implementation of Compensatable Transaction Manager
 */
@Component
public class CompensatableTransactionManagerImpl extends
    AbstractPlatformTransactionManager implements
    CompensatableTransactionManager {

    @Autowired
    private ApplicationContext context;

    @Override
    protected Object doGetTransaction() throws TransactionException {
        // Contact CompensatableTransactionCoordinator to get new (embedded)
        // transaction
        // TODO
        return new CompensatableTransaction();
    }

    @Override
    protected void doBegin(Object transaction,
        TransactionDefinition transactionDefinition)
        throws TransactionException {
        // Assure that passed transaction is of type CompensatableTransaction
        if ((transaction instanceof CompensatableTransaction)
            && (transactionDefinition instanceof CompensatableTransactionDefinition)) {
            CompensatableTransaction compensatableTransaction = (CompensatableTransaction) transaction;
            CompensatableTransactionDefinition compensatableTransactionDefinition = (CompensatableTransactionDefinition) transactionDefinition;

            // Set handler by which this transaction can be compensated
            compensatableTransaction
                .setCompensationHandler(compensatableTransactionDefinition
                    .getCompensationHandler());
        } else {
            throw new RuntimeException(
                "Passed transaction and/or transaction definition are not of the correct type");
        }
        System.out.println("Starting compensating transaction");
    }

    @Override
    protected void doCommit(DefaultTransactionStatus defaultTransactionStatus)
        throws TransactionException {
        System.out.println("Confirming compensating transaction");
    }

    @Override
    protected void doRollback(DefaultTransactionStatus transactionStatus)
        throws TransactionException {
        System.out.println("Compensating transaction");
        Object transaction = transactionStatus.getTransaction();

        if (transaction instanceof CompensatableTransaction) {
            CompensatableTransaction compensatableTransaction = (CompensatableTransaction) transaction;

            // Create instance of CompensationHandler and invoke compensation
            CompensationHandler handler = context
                .getBean(compensatableTransaction.getCompensationHandler());
            handler.compensate();
        } else {
            throw new IllegalArgumentException(
                "Passed transaction status doesn't contain transaction of type CompensatableTransaction");
        }
    }
}
