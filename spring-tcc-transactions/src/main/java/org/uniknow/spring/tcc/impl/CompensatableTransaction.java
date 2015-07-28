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

import org.springframework.context.ApplicationContext;
import org.uniknow.spring.tcc.api.CompensationHandler;
import org.uniknow.spring.tcc.api.ConfirmationHandler;

import java.util.List;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by mase on 7/23/2015.
 */
public final class CompensatableTransaction {

    /*
     * Contains context in which the transaction is executed
     */
    private final ApplicationContext context;

    /*
     * Contains initial definition of transaction
     */
    private CompensatableTransactionDefinition definition;

    /*
     * Contains unique identifier of transaction
     */
    private final UUID id = UUID.randomUUID();

    /*
     * Contains the state of this transaction
     */
    private CompensatableTransactionState state = CompensatableTransactionState.TRY;

    /*
     * Contains the handler that will be called when compensating this
     * transaction
     */
    private Class<? extends CompensationHandler> compensationHandler;

    /*
     * Contains the handler that will be called when confirming this transaction
     */
    private Class<? extends ConfirmationHandler> confirmationHandler;

    /*
     * In case of a nested transaction, parent contains the transaction in which
     * this transaction is nested
     */
    private final CompensatableTransaction parent;

    /*
     * In case of a nested transaction, children contains the transactions that
     * are nested within this transaction.
     */
    private final List<CompensatableTransaction> children = new Vector<CompensatableTransaction>();

    CompensatableTransaction(ApplicationContext context,
        CompensatableTransaction parent) {
        System.out.println("Invoking transaction with parent " + parent);
        this.context = context;
        this.parent = parent;
        if (parent != null) {
            // Append nested transaction
            this.parent.addChild(this);
        }
    }

    /**
     * Add nested {@link org.uniknow.spring.tcc.impl.CompensatableTransaction}.
     * 
     * @param transaction
     *            Nested transaction that has to be added
     */
    void addChild(CompensatableTransaction transaction) {
        children.add(transaction);
    }

    /**
     * Returns the nested
     * {@link org.uniknow.spring.tcc.impl.CompensatableTransaction}s.
     * 
     * @return list of nested
     *         {@link org.uniknow.spring.tcc.impl.CompensatableTransaction} s.
     */
    List<CompensatableTransaction> getChildren() {
        return children;
    }

    /**
     * Returns in case of nested transaction the
     * {@link org.uniknow.spring.tcc.impl.CompensatableTransaction} in which
     * this transaction is nested.
     * 
     * @return {@link org.uniknow.spring.tcc.impl.CompensatableTransaction} in
     *         which this transaction is nested, otherwise null;
     */
    CompensatableTransaction getParent() {
        return parent;
    }

    /**
     * Set initial defintion of transaction
     */
    public void setDefinition(CompensatableTransactionDefinition definition) {
        this.definition = definition;
    }

    /**
     * Returns the current status of the transaction
     */
    CompensatableTransactionState getState() {
        return state;
    }

    /**
     * Set the current status of the transaction
     */
    private void setState(CompensatableTransactionState state) {
        System.out.println("Changing state of transaction from " + this.state
            + " to " + state);
        this.state = state;
    }

    public void confirm() {
        // Initiate compensate on nested transactions
        for (CompensatableTransaction nested : getChildren()) {
            nested.confirm();
        }

        // Create instance of ConfirmationHandler and invoke
        // confirmation
        System.out.println("Confirming " + this);
        ConfirmationHandler handler = context.getBean(definition
            .getConfirmationHandler());
        handler.confirm();

        setState(CompensatableTransactionState.CONFIRMED);
    }

    public void compensate() {
        // Initiate compensate on nested transactions
        for (CompensatableTransaction nested : getChildren()) {
            nested.compensate();
        }

        // Create instance of CompensationHandler and invoke
        // compensation
        System.out.println("Compensating " + this);
        CompensationHandler handler = context.getBean(definition
            .getCompensationHandler());
        handler.compensate();

        setState(CompensatableTransactionState.COMPENSATED);
    }

    public String toString() {
        return getClass().getSimpleName() + "[" + id + ", " + state + ", "
            + children + "]";
    }
}
