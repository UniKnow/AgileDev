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
package org.uniknow.agiledev.swagger;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.config.FilterFactory;
import io.swagger.core.filter.SwaggerSpecFilter;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.*;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * Swagger configuration that allows to specify at runtime the version and
 * returns only those classes for which the SwaggerDefinition version number
 * matches.
 */
public class CustomizedSwaggerConfig extends BeanConfig {

    /**
     * Returns all classes which reside within the specified resource packages
     * and are annotated with API/Path annotation and SwaggerDefinition matches
     * the specified version.
     */
    public Set<Class<?>> classes(String version) {
        this.setVersion(version);
        Set<Class<?>> classes = classes();
        return filterOnVersion(classes, version);
    }

    /**
     * Returns all classes which version matches the specified one. If the class
     * doesn't specify a version number it will be kept in the set of returned
     * classes.
     */
    private Set<Class<?>> filterOnVersion(Set<Class<?>> classes, String version) {

        Set<Class<?>> filteredClasses = new HashSet<>();
        if ((version != null) && !version.isEmpty()) {
            // Remove all classes which version doesn't match the specified
            // version.
            for (Class<?> cls : classes) {
                SwaggerDefinition swaggerDefinition = cls
                    .getAnnotation(SwaggerDefinition.class);
                if (swaggerDefinition != null) {
                    io.swagger.annotations.Info info = swaggerDefinition.info();
                    if (info != null) {
                        if (version.equals(info.version())) {
                            // Include class which version match.
                            filteredClasses.add(cls);
                        }
                    }
                } else {
                    // Include class without version specified.
                    filteredClasses.add(cls);
                }
            }
        }
        return filteredClasses;
    }
}
