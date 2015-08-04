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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.uniknow.spring.cqrs.Event;
import org.uniknow.spring.cqrs.example.command.CreateGameCommand;
import org.uniknow.spring.cqrs.example.command.MakeMoveCommand;
import org.uniknow.spring.cqrs.example.domain.Move;
import org.uniknow.spring.cqrs.example.event.BaseEvent;
import org.uniknow.spring.cqrs.example.event.GameTiedEvent;
import org.uniknow.spring.cqrs.example.event.GameWonEvent;
import org.uniknow.spring.eventStore.EventStore;
import org.uniknow.spring.eventStore.EventStream;

import java.util.UUID;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/example-spring-configuration.xml")
public class GameIntegrationTest {
    // InMemoryEventStore eventStore = new InMemoryEventStore();

    @Autowired
    EventStore eventStore;

    @Autowired
    ApplicationService application;

    UUID gameId = UUID.randomUUID();
    String player1 = UUID.randomUUID().toString();
    String player2 = UUID.randomUUID().toString();

    @Test
    public void tie() throws Exception {
        application.handle(new CreateGameCommand(gameId, player1));
        application.handle(new MakeMoveCommand(gameId, player1, Move.rock));
        application.handle(new MakeMoveCommand(gameId, player2, Move.rock));
        assertEventStreamContains(gameId, new GameTiedEvent(gameId));
    }

    @Test
    public void victory() throws Exception {
        application.handle(new CreateGameCommand(gameId, player1));
        application.handle(new MakeMoveCommand(gameId, player1, Move.rock));
        application.handle(new MakeMoveCommand(gameId, player2, Move.paper));
        assertEventStreamContains(gameId, new GameWonEvent(gameId, player2,
            player1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void same_player_should_fail() throws Exception {
        application.handle(new CreateGameCommand(gameId, player1));
        application.handle(new MakeMoveCommand(gameId, player1, Move.rock));
        application.handle(new MakeMoveCommand(gameId, player1, Move.rock));
    }

    @Test(expected = IllegalStateException.class)
    public void game_not_started() throws Exception {
        application.handle(new MakeMoveCommand(gameId, player1, Move.rock));
    }

    @Test(expected = IllegalStateException.class)
    public void move_after_end_should_fail() throws Exception {
        application.handle(new CreateGameCommand(gameId, player1));
        application.handle(new MakeMoveCommand(gameId, player1, Move.rock));
        application.handle(new MakeMoveCommand(gameId, player2, Move.rock));
        application.handle(new MakeMoveCommand(gameId, "another", Move.rock));
    }

    private void assertEventStreamContains(UUID streamId, Event expectedEvent) {
        EventStream<Long, BaseEvent> eventStream = eventStore
            .loadEventStream(gameId);
        String expected = EventStringUtil.toString(expectedEvent);
        for (Event event : eventStream) {
            if (EventStringUtil.toString(event).equals(expected))
                return;
        }
        fail("Expected event did not occur: " + expected);
    }
}
