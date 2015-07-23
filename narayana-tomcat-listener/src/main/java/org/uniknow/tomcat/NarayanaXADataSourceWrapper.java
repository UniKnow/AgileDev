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

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.sql.XADataSource;

import com.arjuna.ats.jdbc.TransactionalDriver;

/**
 * 
 * @author Charles Souillard
 * 
 */
public class NarayanaXADataSourceWrapper implements XADataSource, DataSource {

    private final XADataSource xaDS;

    private final TransactionalDriver txDriver = new TransactionalDriver();

    private final String ajurnaURL;

    /**
     * Create a wrapper around the provided XADataSource implementation,
     * which should be registered in tomcat's global JNDI with the specified jndiName.
     * Note: the registration is not done here, it has to be done (<Transaction> tag in context.xml offers this).
     * 
     * @param jndiName
     *            should be the fully qualifed JNDI name of the XADataSource, in
     *            tomcat's global JNDI, not a webapp specific JNDI context.
     * @param xaDS
     */
    public NarayanaXADataSourceWrapper(final String ajurnaURL, final XADataSource xaDS) {
        this.xaDS = xaDS;
        this.ajurnaURL = ajurnaURL;
        // System.err.println("ARJUNA URL: ******* " + url + " *****");
    }

    /**
     * @see javax.sql.DataSource.getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(null, null);
    }

    /**
     * @see javax.sql.DataSource.getConnection(String, String)
     */
    @Override
    public Connection getConnection(final String username, final String password) throws SQLException {
        final Properties properties = new Properties();

        if (username != null) {
            properties.setProperty(TransactionalDriver.userName, username);
        }

        if (password != null) {
            properties.setProperty(TransactionalDriver.password, password);
        }
        // Warning: ensure the tx lifecycle listener is configured too in tomcat or there will be a
        // possible race here, as recovery needs these properties too and may start first
        NarayanaJndiPropertiesSetter.setJndiProperties();

        // try {
        // printJNDI("java:comp/env");
        // } catch (NamingException e) {
        // e.printStackTrace();
        // }

        return txDriver.connect(ajurnaURL, properties);
    }

    private void printJNDI(final String name) throws NamingException {
        final ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        System.err.println("Lising JNDI content for '" + name + "' from classloader: " + currentClassLoader);
        final Context ctx = new InitialContext();
        final NamingEnumeration<NameClassPair> list = ctx.list(name);
        while (list.hasMore()) {
            NameClassPair nc = list.next();
            System.out.println(nc);
        }
    }

    /**
     * @see javax.sql.XADataSource.getXAConnection()
     */
    @Override
    public XAConnection getXAConnection() throws SQLException {
        return xaDS.getXAConnection();
    }

    /**
     * @see javax.sql.XADataSource.getXAConnection(String, String)
     */
    @Override
    public XAConnection getXAConnection(final String user, final String password) throws SQLException {
        return xaDS.getXAConnection(user, password);
    }

    /**
     * @see java.sql.Wrapper.isWrapperFor(Class<?>)
     */
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface.isAssignableFrom(XADataSource.class);
    }

    /**
     * @see java.sql.Wrapper.unwrap(Class<T>)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (isWrapperFor(iface)) {
            return (T) xaDS;
        } else {
            throw new SQLException("Not a wrapper for " + iface.getCanonicalName());
        }
    }

    /**
     * @see javax.sql.CommonDataSource.getLogWriter()
     */
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return xaDS.getLogWriter();
    }

    /**
     * @see javax.sql.CommonDataSource.setLogWriter(PrintWriter)
     */
    @Override
    public void setLogWriter(final PrintWriter out) throws SQLException {
        xaDS.setLogWriter(out);
    }

    /**
     * @see javax.sql.CommonDataSource.setLoginTimeout(int)
     */
    @Override
    public void setLoginTimeout(final int seconds) throws SQLException {
        xaDS.setLoginTimeout(seconds);
    }

    /**
     * @see javax.sql.CommonDataSource.getLoginTimeout()
     */
    @Override
    public int getLoginTimeout() throws SQLException {
        return xaDS.getLoginTimeout();
    }

    /**
     * @see javax.sql.CommonDataSource.getParentLogger()
     */
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}
