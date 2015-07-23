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

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;
import javax.transaction.UserTransaction;

import com.arjuna.ats.jta.common.jtaPropertyManager;

/**
 * 
 * @author Charles Souillard
 * @see javax.naming.spi.ObjectFactory
 * 
 */
public class NarayanaUserTransactionObjectFactory implements ObjectFactory {

    /**
     * @see javax.naming.spi.ObjectFactory.getObjectInstance(Object, Name, Context, Hashtable<?, ?>)
     * @see com.arjuna.ats.jta.common.JTAEnvironmentBean.getUserTransaction()
     */
    public Object getObjectInstance(final Object obj, final Name name, final Context nameCtx, final Hashtable<?, ?> environment) throws Exception {
        final UserTransaction userTransaction = jtaPropertyManager.getJTAEnvironmentBean().getUserTransaction();
        if (userTransaction == null) {
            throw new Exception(
                    "There is no pre-instantiated instance of UserTransaction set and classloading or instantiation have failed. unable to return an instance of UserTransaction.");
        }
        return userTransaction;
    }
}
