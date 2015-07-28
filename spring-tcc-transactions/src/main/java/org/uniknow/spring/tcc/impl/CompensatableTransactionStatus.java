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
package org.uniknow.spring.tcc.impl;

import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * Created by mase on 7/27/2015.
 */
public class CompensatableTransactionStatus extends DefaultTransactionStatus {

    private boolean completed = false;

    /**
     * Create a new DefaultTransactionStatus instance.
     * 
     * @param transaction
     *            underlying transaction object that can hold state for the
     *            internal transaction implementation
     * @param newTransaction
     *            if the transaction is new, else participating in an existing
     *            transaction
     * @param newSynchronization
     *            if a new transaction synchronization has been opened for the
     *            given transaction
     * @param debug
     *            should debug logging be enabled for the handling of this
     *            transaction? Caching it in here can prevent repeated calls to
     *            ask the logging system whether debug logging should be
     *            enabled.
     * @param suspendedResources
     *            a holder for resources that have been suspended for this
     *            transaction, if any
     */
    public CompensatableTransactionStatus(Object transaction,
        boolean newTransaction, boolean newSynchronization, boolean readOnly,
        boolean debug, Object suspendedResources) {
        super(transaction, newTransaction, newSynchronization, readOnly, debug,
            suspendedResources);
    }

    /**
     * Mark this transaction as completed, that is, committed or rolled back.
     */
    public void setCompleted() {
        Object transaction = getTransaction();
        if (transaction instanceof CompensatableTransaction) {
            CompensatableTransaction compensatableTransaction = (CompensatableTransaction) transaction;
            if (compensatableTransaction.getParent() == null) {
                System.out.println("Mark transaction "
                    + compensatableTransaction + " completed");
                completed = true;
            }
        }
    }

    /**
     * Returns whether the CompensatableTransaction is completed. A
     * compensatable transaction is regarded complete when the parent of the
     * nested transactions (if any) has status CONFIRMED or COMPENSATED.
     * 
     * @return
     */
    public boolean isCompleted() {
        return completed;
    }

}
