package org.uniknow.spring.tcc;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * Transaction Manager for Try Cancel/Confirm transactions. Since there are resources (REST services, LDAP storage, etc) that are not supporting transactions this class and its collaborators aim to provide compensating transactions instead. Should a transaction need to be rolled back, this transaction manager will try to restore the original state using information recorded prior to each operation. The operation where the original state is restored is called a compensating operation.
 */
public class TccTransactionManager {
//        extends AbstractPlatformTransactionManager {
//
//    /**
//     * Return a transaction object for the current transaction state.
//     * <p>This object will be passed into the other template
//     * methods (e.g. doBegin and doCommit), either directly or as part of a
//     * DefaultTransactionStatus instance.
//     * <p>The returned object should contain information about any existing
//     * transaction, that is, a transaction that has already started before the
//     * current {@code getTransaction} call on the transaction manager.
//     * Consequently, a {@code doGetTransaction} implementation will usually
//     * look for an existing transaction and store corresponding state in the
//     * returned transaction object.
//     * @return the current transaction object
//     * @throws org.springframework.transaction.CannotCreateTransactionException
//     * if transaction support is not available
//     * @throws TransactionException in case of lookup or system errors
//     * @see #doBegin
//     * @see #doCommit
//     * @see #doRollback
//     * @see DefaultTransactionStatus#getTransaction
//     */
//    @Override
//    protected Object doGetTransaction() throws TransactionException {
//        return null;
//    }
//
//    /**
//     * Begin a new transaction with semantics according to the given transaction
//     * definition.
//     * <p>This method gets called when the transaction manager has decided to actually
//     * start a new transaction. Either there wasn't any transaction before, or the
//     * previous transaction has been suspended.
//     * <p>A special scenario is a nested transaction without savepoint: If
//     * {@code useSavepointForNestedTransaction()} returns "false", this method
//     * will be called to start a nested transaction when necessary. In such a context,
//     * there will be an active transaction: The implementation of this method has
//     * to detect this and start an appropriate nested transaction.
//     * @param transaction transaction object returned by {@code doGetTransaction}
//     * @param definition TransactionDefinition instance, describing propagation
//     * behavior, isolation level, read-only flag, timeout, and transaction name
//     * @throws TransactionException in case of creation or system errors
//     */
//    @Override
//    protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
//
//    }
//
//    /**
//     * Perform an actual commit of the given transaction.
//     * <p>An implementation does not need to check the "new transaction" flag
//     * or the rollback-only flag; this will already have been handled before.
//     * Usually, a straight commit will be performed on the transaction object
//     * contained in the passed-in status.
//     * @param status the status representation of the transaction
//     * @throws TransactionException in case of commit or system errors
//     * @see DefaultTransactionStatus#getTransaction
//     */
//    @Override
//    protected void doCommit(DefaultTransactionStatus status) throws TransactionException {
//
//    }
//
//    /**
//     * Perform an actual rollback of the given transaction.
//     * <p>An implementation does not need to check the "new transaction" flag;
//     * this will already have been handled before. Usually, a straight rollback
//     * will be performed on the transaction object contained in the passed-in status.
//     * @param status the status representation of the transaction
//     * @throws TransactionException in case of system errors
//     * @see DefaultTransactionStatus#getTransaction
//     */
//    @Override
//    protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
//
//    }
}
