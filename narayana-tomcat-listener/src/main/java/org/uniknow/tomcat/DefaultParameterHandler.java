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

import java.lang.reflect.Method;

import javax.sql.XADataSource;

/**
 * 
 * @author Charles Souillard
 * @see org.bonitasoft.tomcat.narayana.ParameterHandler
 * 
 */
public class DefaultParameterHandler implements ParameterHandler {

    /**
     * This implementation assumes that the given datasource has a corresponding setter with the given parameterName with a String parameter
     * 
     * @see org.bonitasoft.engine.tomcat.narayana.ParameterHandler.handlerParameter(XADataSource, String, String)
     */
    public void handlerParameter(final XADataSource xaDataSource, final String parameterName, final String parameterValue) throws Exception {
        // assuming method takes a String argument
        final Method method = xaDataSource.getClass().getMethod(parameterName, new Class[] { java.lang.String.class });
        method.invoke(xaDataSource, parameterValue);
    }
}
