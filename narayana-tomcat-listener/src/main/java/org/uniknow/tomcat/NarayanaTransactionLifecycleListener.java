/**
 * Copyright (C) 2011-2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.uniknow.tomcat;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;

import com.arjuna.ats.arjuna.coordinator.TransactionReaper;
import com.arjuna.ats.arjuna.recovery.RecoveryManager;
import com.arjuna.ats.jta.common.jtaPropertyManager;

/**
 * 
 * @author Charles Souillard
 * @see org.apache.catalina.LifecycleListener
 */
public class NarayanaTransactionLifecycleListener implements LifecycleListener {

    private static final boolean TERMINATE_NOW = false;

    RecoveryManager recoveryManager;

    public void lifecycleEvent(final LifecycleEvent event) {
        // narayana by default does not bind the UserTransaction in java:comp/UserTransaction
        // default is com.arjuna.ats.jta.common.JTAEnvironmentBean.userTransactionJNDIContext
        // tomcat binds the UserTransaction using the <Transaction> declared in conf/context.xml to java:comp/UserTransaction
        // http://tomcat.apache.org/tomcat-6.0-doc/jndi-resources-howto.html
        jtaPropertyManager.getJTAEnvironmentBean().setUserTransactionJNDIContext("java:comp/UserTransaction");

        /*
         * // for testing purpose
         * try {
         * jtaPropertyManager.getJTAEnvironmentBean().getTransactionManager().setTransactionTimeout(100);
         * System.err.println("*** TX timeout set to 100");
         * } catch (SystemException e) {
         * e.printStackTrace();
         * }
         */

        if (Lifecycle.BEFORE_START_EVENT.equals(event.getType())) {
            manageStart();
        } else if (Lifecycle.AFTER_STOP_EVENT.equals(event.getType())) {
            manageStop();
        }
    }

    private void manageStart() {
        // run the reaper here so it is in the server's classloader context
        // if we started it lazily on first tx it would run with context of the webapp using the tx.
        TransactionReaper.instantiate();

        // recovery needs the correct JNDI settings.
        // NarayanaXADataSourceWrapper sets these too as a precaution, but we may run first.
        NarayanaJndiPropertiesSetter.setJndiProperties();

        // a 'start' occurs after the Resources in GlobalNamingResources are instantiated,
        // so we can safely start the recovery Thread here.
        recoveryManager = RecoveryManager.manager();
        recoveryManager.startRecoveryManagerThread();
    }

    private void manageStop() {
        // terminates the recovery manager if required
        recoveryManager.terminate(TERMINATE_NOW);
        // terminates the reaper if required
        TransactionReaper.terminate(TERMINATE_NOW);
    }

}
