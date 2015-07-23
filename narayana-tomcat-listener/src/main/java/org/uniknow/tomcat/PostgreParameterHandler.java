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
 * This class is an implementation of org.bonitasoft.engine.tomcat.narayana.ParameterHandler that relies on default behaviour implemented by
 * org.bonitasoft.engine.tomcat.narayana.DefaultParameterHandler
 * It only defines different ways to manage Postgre specific parameters that are not String
 * 
 * @author Charles Souillard
 * @see org.bonitasoft.tomcat.narayana.ParameterHandler
 * @see org.bonitasoft.tomcat.narayana.DefaultParameterHandler
 */
public class PostgreParameterHandler extends DefaultParameterHandler {

    /**
     * @see org.bonitasoft.engine.tomcat.narayana.ParameterHandler.handlerParameter(XADataSource, String, String)
     */
    @Override
    public void handlerParameter(final XADataSource xaDataSource, final String parameterName, final String parameterValue) throws Exception {
        if ("setPortNumber".equals(parameterName)) {
            final Method method = xaDataSource.getClass().getMethod(parameterName, new Class[] { int.class });
            method.invoke(xaDataSource, Integer.parseInt(parameterValue));
        } else {
            super.handlerParameter(xaDataSource, parameterName, parameterValue);
        }
    }
}
