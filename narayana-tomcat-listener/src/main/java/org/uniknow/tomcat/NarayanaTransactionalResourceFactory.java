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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import javax.sql.XADataSource;

/**
 * This class builds a XaDataSource that wraps all subsequent calls to the final declared XaDataSource to register the connection inside Narayana Transaction
 * Manager
 * 
 * @author Charles Souillard
 * @see javax.naming.spi.ObjectFactory
 */
public class NarayanaTransactionalResourceFactory implements ObjectFactory {

    private static final List<String> IGNORED_PARAMETERS = new ArrayList<String>();

    private static final String PARAMETER_PREFIX = "xads.";

    private static final String XA_DS_CLASS_NAME_PARAMETER = PARAMETER_PREFIX + "class";

    private static final String PARAMETER_HANDLER_CLASS_NAME_PARAMETER = PARAMETER_PREFIX + "param.handler.class";

    private static final String AJURNA_URL = PARAMETER_PREFIX + "arjuna.url";

    static {
        // parameters that a default Tomcat resource may have
        IGNORED_PARAMETERS.add("factory");
        IGNORED_PARAMETERS.add("scope");
        IGNORED_PARAMETERS.add("auth");
        IGNORED_PARAMETERS.add("type");
        IGNORED_PARAMETERS.add("name");
        IGNORED_PARAMETERS.add("description");

        // parameters that must not be transfered to the datasource instance
        IGNORED_PARAMETERS.add(XA_DS_CLASS_NAME_PARAMETER);
        IGNORED_PARAMETERS.add(PARAMETER_HANDLER_CLASS_NAME_PARAMETER);
        IGNORED_PARAMETERS.add(AJURNA_URL);
    }

    /**
     * @see javax.naming.spi.ObjectFactory
     */
    @Override
    public Object getObjectInstance(final Object obj, final Name name, final Context nameCtx, final Hashtable<?, ?> environment) throws Exception {
        // convert the Addresses into easier to handle String key/values
        final Reference ref = (Reference) obj;
        final Enumeration<RefAddr> refAddrs = ref.getAll();
        final HashMap<String, String> resourceParameters = new HashMap<String, String>();
        while (refAddrs.hasMoreElements()) {
            final RefAddr addr = refAddrs.nextElement();
            final String refAddrType = addr.getType();
            final String refAddrContent = (String) addr.getContent();
            resourceParameters.put(refAddrType, refAddrContent);
        }

        // build a new instance of the XADataSource based on the supplied class name
        final String xaDsClassName = resourceParameters.get(XA_DS_CLASS_NAME_PARAMETER);
        if (xaDsClassName == null) {
            throw new NamingException("No value specified for " + XA_DS_CLASS_NAME_PARAMETER);
        }
        final XADataSource xaDataSource = instantiateClass(XADataSource.class, xaDsClassName);

        // build a new instance of the ParameterHandler based on the supplied class name
        final String parameterHandlerClassName = resourceParameters.get(PARAMETER_HANDLER_CLASS_NAME_PARAMETER);
        if (parameterHandlerClassName == null) {
            throw new NamingException("No value specified for " + parameterHandlerClassName);
        }
        final ParameterHandler parameterHandler = instantiateClass(ParameterHandler.class, parameterHandlerClassName);

        // configure the datasource with supplied parameters (the one that starts with the appropriate prefix)
        for (final String key : resourceParameters.keySet()) {
            if (IGNORED_PARAMETERS.contains(key)) {
                continue;
            }

            if (!key.startsWith(PARAMETER_PREFIX)) {
                System.err.println("Resource parameter '" + key + "' is ignored because it is unexpected by " + this.getClass().getName());
                continue;
            }
            parameterHandler.handlerParameter(xaDataSource, key.substring(PARAMETER_PREFIX.length()), resourceParameters.get(key));
        }

        // get arjunaURL
        final String arjunaURL = resourceParameters.get(AJURNA_URL);
        if (arjunaURL == null) {
            throw new NamingException("No value specified for " + AJURNA_URL);
        }

        // return a wrapper around the built & configured xaDataSource that will intercept all subsequent calls to this xaDataSource to make returned
        // connections bound the transaction manager
        return new NarayanaXADataSourceWrapper(arjunaURL, xaDataSource);
    }

    /**
     * Load the given class name and return an instance of this loaded class
     */
    @SuppressWarnings("unchecked")
    protected <T> T instantiateClass(final Class<T> clazze, final String className) throws NamingException {
        try {
            return ((Class<? extends T>) Thread.currentThread().getContextClassLoader().loadClass(className)).newInstance();
        } catch (final ClassNotFoundException e) {
            final NamingException ex = new NamingException("Unable to load " + className);
            ex.initCause(e);
            throw ex;
        } catch (InstantiationException e) {
            final NamingException ex = new NamingException("Unable to instantiate " + className);
            ex.initCause(e);
            throw ex;
        } catch (IllegalAccessException e) {
            final NamingException ex = new NamingException("Unable to access " + className);
            ex.initCause(e);
            throw ex;
        }
    }
}
