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
package org.uniknow.spring.cqrs.example.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uniknow.spring.cqrs.CommandHandler;
import org.uniknow.spring.cqrs.Event;
import org.uniknow.spring.cqrs.example.GameQueryModel;
import org.uniknow.spring.cqrs.example.domain.Game;
import org.uniknow.spring.cqrs.example.domain.Move;
import org.uniknow.spring.cqrs.example.domain.State;
import org.uniknow.spring.cqrs.example.event.GameTiedEvent;
import org.uniknow.spring.cqrs.example.event.GameWonEvent;
import org.uniknow.spring.cqrs.example.event.MoveDecidedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Handler for MakeMoveCommand
 */
@Component
public class MakeMoveCommandHandler implements
    CommandHandler<MakeMoveCommand, List<? extends Event>> {

    /**
     * Repository by which instance of game can be retrieved
     */
    @Autowired
    private GameQueryModel queryModel;

    /**
     * Method that will be called when MakeMoveCommand is received.
     * 
     * @param command
     *            Command which was received
     * @return List of events
     */
    @Override
    public List<? extends Event> handle(MakeMoveCommand command) {
        Game game = queryModel.get(command.gameId);

        if (game != null) {
            if (State.CREATED == game.getState()) {
                return Arrays.asList(new MoveDecidedEvent(command.gameId,
                    command.playerEmail, command.move));
            } else if (State.WAITING == game.getState()) {
                if (game.getPlayer().equals(command.playerEmail))
                    throw new IllegalArgumentException("Player already in game");
                return Arrays.asList(
                    new MoveDecidedEvent(command.gameId, command.playerEmail,
                        command.move),
                    processMove(game, command.gameId, command.playerEmail,
                        command.move));
            } else {
                throw new IllegalStateException(game.getState().toString());
            }
        } else {
            throw new IllegalStateException("Game does not exist");
        }
    }

    /**
     * Processes move of opponent
     * 
     * @param game
     *            State of game
     * @param gameId
     *            Unique identifier for game
     * @param opponentEmail
     *            Email of opponent
     * @param opponentMove
     *            Move of opponent
     * @return Event indicating the result of the opponents move.
     */
    private Event processMove(Game game, UUID gameId, String opponentEmail,
        Move opponentMove) {
        if (game.getMove().defeats(opponentMove)) {
            // Game won by player
            return new GameWonEvent(gameId, game.getPlayer(), opponentEmail);
        } else if (opponentMove.defeats(game.getMove())) {
            // Game won by opponent
            return new GameWonEvent(gameId, opponentEmail, game.getPlayer());
        } else {
            return new GameTiedEvent(gameId);
        }
    }

}
