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
package org.uniknow.spring.cqrs.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.uniknow.spring.cqrs.Handler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by mase on 7/30/2015.
 * 
 * @param <H>
 *            Type of Handler
 */
class AbstractHandlerProvider<H extends Handler> {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(AbstractHandlerProvider.class);

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    /**
     * Contains mapping of type that is processed by handler to handler.
     */
    private final Map<Class, List<String>> handlers = new HashMap<>();

    /**
     * Initializes the map of handlers
     * 
     * @param handlerType
     *            Type of handlers that should be registered
     */
    protected void init(Class handlerType) {
        handlers.clear();

        // Traverse all handler beans and register them
        String[] handlerNames = beanFactory.getBeanNamesForType(handlerType);
        for (String handlerName : handlerNames) {
            System.out.println("Processing " + handlerName);
            BeanDefinition handlerDefintion = beanFactory
                .getBeanDefinition(handlerName);
            try {
                // Determine type that is processed by handler
                Class handlerClass = Class.forName(handlerDefintion
                    .getBeanClassName());
                Class acceptedType = getAcceptedType(handlerClass);

                // Register handler for command
                System.out.println("Registering  handler " + handlerName
                    + " for type " + acceptedType);
                if (!handlers.containsKey(acceptedType)) {
                    handlers.put(acceptedType, new Vector<String>());
                }
                List<String> registeredHandlers = handlers.get(acceptedType);
                registeredHandlers.add(handlerName);
                handlers.put(acceptedType, registeredHandlers);

            } catch (ClassNotFoundException error) {

            }
        }

    }

    /**
     * Returns the type that is processed by the specified handler
     * 
     * @param handlerType
     * @return
     */
    private Class getAcceptedType(Class handlerType) {
        // Get CommandHandler interface
        Type[] interfaces = handlerType.getGenericInterfaces();
        for (Type type : interfaces) {
            if (type instanceof ParameterizedType) {
                // Determine type that is handled by handler
                ParameterizedType parametrizedInterface = (ParameterizedType) type;
                // if
                // (handlerType.isInstance(parametrizedInterface.getRawType()))
                // {
                return (Class) parametrizedInterface.getActualTypeArguments()[0];
                // }
            }
        }
        throw new RuntimeException(
            "Unable to determine type that is processed by " + handlerType);
    }

    /**
     * Returns handler for specified type
     * 
     * @param type
     *            type for which we want to retrieve the matching handler
     * @return Handlers which are able to process specified type
     */
    List<H> getHandlers(Class type) {
        // Get instances of Handlers that processes the specified type
        if (handlers.containsKey(type)) {
            List<String> handlerNames = handlers.get(type);
            List<H> handlersOfType = new Vector();
            for (String handlerName : handlerNames) {
                handlersOfType.add((H) beanFactory.getBean(handlerName));
            }
            return handlersOfType;
        } else {
            throw new RuntimeException("No handler found for " + type);
        }
    }

}
