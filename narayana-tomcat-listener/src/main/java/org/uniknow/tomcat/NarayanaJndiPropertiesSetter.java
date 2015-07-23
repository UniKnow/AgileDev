package org.uniknow.tomcat;

import java.util.Hashtable;

import javax.naming.Context;

import com.arjuna.ats.jdbc.common.jdbcPropertyManager;

public class NarayanaJndiPropertiesSetter {

    public static void setJndiProperties() {
        final Hashtable<String, Object> jndiProps = new Hashtable<String, Object>(2);
        jndiProps.put("Context.INITIAL_CONTEXT_FACTORY", System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
        jndiProps.put("Context.URL_PKG_PREFIXES", System.getProperty(Context.URL_PKG_PREFIXES));

        jdbcPropertyManager.getJDBCEnvironmentBean().setJndiProperties(jndiProps);
    }
}
