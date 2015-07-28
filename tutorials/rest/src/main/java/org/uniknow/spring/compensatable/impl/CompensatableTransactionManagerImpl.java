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
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.uniknow.spring.compensatable.api.Compensatable;
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

    /**
     * Check if the given transaction object indicates an existing transaction
     * (that is, a transaction which has already started).
     * <p>
     * The result will be evaluated according to the specified propagation
     * behavior for the new transaction. An existing transaction might get
     * suspended (in case of PROPAGATION_REQUIRES_NEW), or the new transaction
     * might participate in the existing one (in case of PROPAGATION_REQUIRED).
     * <p>
     * The default implementation returns {@code false}, assuming that
     * participating in existing transactions is generally not supported.
     * Subclasses are of course encouraged to provide such support.
     * 
     * @param transaction
     *            transaction object returned by doGetTransaction
     * @return if there is an existing transaction
     * @throws TransactionException
     *             in case of system errors
     * @see #doGetTransaction
     */
    @Override
    protected boolean isExistingTransaction(Object transaction) {
        if (transaction instanceof CompensatableTransaction) {
            return ((CompensatableTransaction) transaction).getParent() != null;
        } else {
            throw new RuntimeException("Passed transaction is not of type "
                + CompensatableTransaction.class.getSimpleName());
        }
    }

    /**
     * The doGetTransaction implementation will usually look for an existing
     * transaction and store corresponding state in the returned transaction
     * object.
     * 
     * @return
     * @throws TransactionException
     */
    @Override
    protected Object doGetTransaction() throws TransactionException {
        return new CompensatableTransaction(context,
            CompensatableTransactionCoordinator.getCurrentTransaction());
    }

    @Override
    protected void doBegin(Object transaction,
        TransactionDefinition transactionDefinition)
        throws TransactionException {
        System.out.println("Begin compensating transaction " + transaction
            + ", " + transactionDefinition);
        // Assure that passed transaction is of type CompensatableTransaction
        if (transaction instanceof CompensatableTransaction) {

            CompensatableTransaction compensatableTransaction = (CompensatableTransaction) transaction;
            CompensatableTransactionCoordinator
                .setCurrentTransaction(compensatableTransaction);
        } else {
            throw new RuntimeException(
                "Passed transaction and/or transaction definition are not of the correct type");
        }
    }

    @Override
    protected void doCommit(DefaultTransactionStatus transactionStatus)
        throws TransactionException {
        Object transaction = transactionStatus.getTransaction();
        if (transaction instanceof CompensatableTransaction) {
            CompensatableTransaction compensatableTransaction = (CompensatableTransaction) transaction;
            System.out.println("Committing " + compensatableTransaction);
            switch (compensatableTransaction.getState()) {
            case TRY:
                // Only invoke confirmation on root of nested transactions
                System.out.println("Trying to confirm "
                    + compensatableTransaction);
                if (compensatableTransaction.getParent() == null) {
                    compensatableTransaction.confirm();
                } else {
                    throw new CompensatableTransactionException(
                        "Attempting to confirm nested transaction");
                }
                break;
            default:
                throw new CompensatableTransactionException(
                    "Can't commit transaction when state is "
                        + compensatableTransaction.getState());
            }
        } else {
            throw new RuntimeException(
                "Passed transaction status doesn't contain transaction of type CompensatableTransaction");
        }
    }

    @Override
    protected void doSetRollbackOnly(DefaultTransactionStatus status)
        throws TransactionException {

    }

    @Override
    protected void doRollback(DefaultTransactionStatus transactionStatus)
        throws TransactionException {
        System.out.println("Compensating transaction");
        Object transaction = transactionStatus.getTransaction();

        if (transaction instanceof CompensatableTransaction) {
            CompensatableTransaction compensatableTransaction = (CompensatableTransaction) transaction;

            switch (compensatableTransaction.getState()) {
            case TRY:
                // Only invoke rollback on root of nested transactions
                if (compensatableTransaction.getParent() == null) {
                    compensatableTransaction.compensate();
                } else {
                    throw new CompensatableTransactionException(
                        "Attempting to compensate on nested transaction");
                }
                break;
            default:
                throw new CompensatableTransactionException(
                    "Can't rollback transaction when state is "
                        + compensatableTransaction.getState());
            }
        } else {
            throw new RuntimeException(
                "Passed transaction status doesn't contain transaction of type CompensatableTransaction");
        }
    }

    /**
     * Cleanup resources after transaction completion.
     * <p>
     * Called after {@code doCommit} and {@code doRollback} execution, on any
     * outcome. The default implementation does nothing.
     * <p>
     * Should not throw any exceptions but just issue warnings on errors.
     * 
     * @param transaction
     *            transaction object returned by {@code doGetTransaction}
     */
    protected void doCleanupAfterCompletion(Object transaction) {
        CompensatableTransactionCoordinator.setCurrentTransaction(null);
    }

    /**
     * Suspend the current transaction.
     * 
     * @param transaction
     *            transaction object returned by {@code doGetTransaction}
     * @return suspended CompensatableTransaction.
     * @throws TransactionException
     *             in case of system errors
     * @see #doResume
     */
    protected Object doSuspend(Object transaction) throws TransactionException {
        System.out.println("Suspending " + transaction);
        if (transaction instanceof CompensatableTransaction) {
            CompensatableTransactionCoordinator.setCurrentTransaction(null);
            return transaction;
        } else {
            throw new RuntimeException(
                "Passed transaction status doesn't contain transaction of type CompensatableTransaction");
        }
    }

    /**
     * Resume the resources of the current transaction. Transaction
     * synchronization will be resumed afterwards.
     * <p>
     * The default implementation throws a
     * TransactionSuspensionNotSupportedException, assuming that transaction
     * suspension is generally not supported.
     * 
     * @param transaction
     *            transaction object returned by {@code doGetTransaction}
     * @param suspendedTransaction
     *            suspended CompensatableTransaction
     * @throws org.springframework.transaction.TransactionSuspensionNotSupportedException
     *             if resuming is not supported by the transaction manager
     *             implementation
     * @throws TransactionException
     *             in case of system errors
     * @see #doSuspend
     */
    protected void doResume(Object transaction, Object suspendedTransaction)
        throws TransactionException {
        System.out.println("Resuming " + transaction);
        if (suspendedTransaction instanceof CompensatableTransaction) {
            CompensatableTransactionCoordinator
                .setCurrentTransaction((CompensatableTransaction) suspendedTransaction);
        }
    }

    /**
     * Create a TransactionStatus instance for the given arguments.
     */
    @Override
    protected DefaultTransactionStatus newTransactionStatus(
        TransactionDefinition definition, Object transaction,
        boolean newTransaction, boolean newSynchronization, boolean debug,
        Object suspendedResources) {

        System.out.println("new transaction status with definition "
            + definition);
        boolean actualNewSynchronization = newSynchronization
            && !TransactionSynchronizationManager.isSynchronizationActive();

        if (transaction instanceof CompensatableTransaction) {
            CompensatableTransaction compensatableTransaction = (CompensatableTransaction) transaction;
            compensatableTransaction
                .setDefinition((CompensatableTransactionDefinition) definition);
            return new CompensatableTransactionStatus(compensatableTransaction,
                newTransaction, definition.isReadOnly(),
                actualNewSynchronization, debug, suspendedResources);
        } else {
            throw new RuntimeException(
                "Transaction must by of type CompensatableTransaction");
        }
    }

}
