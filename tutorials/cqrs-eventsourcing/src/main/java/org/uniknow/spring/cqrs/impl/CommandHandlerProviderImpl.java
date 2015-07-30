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
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.uniknow.spring.cqrs.Command;
import org.uniknow.spring.cqrs.CommandHandler;
import org.uniknow.spring.cqrs.CommandHandlerProvider;

import java.util.List;

/**
 * Provider by which CommandHandler for specific Command can be retrieved.
 * 
 * @TODO: Command might need to be processed by multiple handlers. Modify this
 *        class so that this can be supported.
 */
@Component
public class CommandHandlerProviderImpl extends
    AbstractHandlerProvider<CommandHandler<? extends Command, ?>> implements
    CommandHandlerProvider, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(CommandHandlerProviderImpl.class);

    /**
     * Loads the configured CommandHandlers within the Spring context
     * 
     * @param event
     *            the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("Initializing Command Handler Provider");
        init(CommandHandler.class);
    }

    /**
     * Returns handler for specified command
     * 
     * @param command
     *            command for which we want to retrieve the matching handler
     * @return CommandHandler which is able to process specified command
     */
    @Override
    public CommandHandler<? extends Command, ?> getHandler(Command command) {
        List<CommandHandler<? extends Command, ?>> commandHandlers = getHandlers(command
            .getClass());
        return commandHandlers.get(0);
    }
}
