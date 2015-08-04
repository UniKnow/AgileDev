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
package org.uniknow.spring.cqrs.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uniknow.spring.cqrs.EventHandler;
import org.uniknow.spring.cqrs.EventHandlerProvider;
import org.uniknow.spring.cqrs.example.domain.Game;
import org.uniknow.spring.cqrs.example.event.BaseEvent;
import org.uniknow.spring.eventStore.Event;
import org.uniknow.spring.eventStore.EventStore;
import org.uniknow.spring.eventStore.EventStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by mase on 7/30/2015.
 */
@Component
public class GameQueryModel {

    /*
     * Provider by which event handlers can be retrieved
     */
    @Autowired
    private EventHandlerProvider eventHandlerProvider;

    /*
     * Contains store in which events for games are persisted
     */
    @Autowired
    private EventStore<Long, BaseEvent> eventStore;

    private Map<UUID, Game> queryModel = new HashMap<>();

    /**
     * Returns Game that matches the specified identifier
     */
    public Game get(UUID gameID) {
        // Check whether game already exist within query model
        if (!queryModel.containsKey(gameID)) {
            // Attempt to restore game from event store
            EventStream<Long, BaseEvent> eventStream = eventStore
                .loadEventStream(gameID);
            for (BaseEvent event : eventStream) {
                // TODO: Ignore rejected events (should we do this here or
                // filter out in iterator?)
                for (EventHandler eventHandler : eventHandlerProvider
                    .getHandler(event)) {
                    eventHandler.handle(event);
                }
            }
        }
        return queryModel.get(gameID);
    }

    /**
     * Persists game
     */
    public void put(Game game) {
        queryModel.put(game.getGameID(), game);
    }

    /**
     * Invalidate cached Game. This will happen when transaction is rolled back.
     */
    public void invalidate(UUID gameID) {
        queryModel.remove(gameID);
    }
}
