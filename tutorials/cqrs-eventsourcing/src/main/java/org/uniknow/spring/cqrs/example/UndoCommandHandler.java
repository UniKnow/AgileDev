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
import org.uniknow.spring.cqrs.Command;
import org.uniknow.spring.cqrs.Event;
import org.uniknow.spring.cqrs.EventState;
import org.uniknow.spring.tcc.api.CompensatableTransactionContext;
import org.uniknow.spring.tcc.api.CompensationHandler;

import java.util.List;

/**
 * Created by mase on 8/3/2015.
 */
@Component
public class UndoCommandHandler implements CompensationHandler {

    /**
     * Repository by which instances of Game can be retrieved
     */
    @Autowired
    private GameQueryModel queryModel;

    /**
     * Handler which will be invoked when transaction needs to be compensated
     * 
     * @param transactionContext
     *            context containing arguments which where annotated with
     *            CompensatableTransactionContext
     */
    @Override
    public void compensate(CompensatableTransactionContext transactionContext) {
        System.out.println("intitiating compensate with context "
            + transactionContext);
        // Check whether there are events that need to be rejected
        List<Event> events = (List<Event>) transactionContext.get("events");
        if (events != null) {
            // Reject all events that occurred due to the failed command
            for (Event event : events) {
                System.out.println("Rejecting event " + event);
                event.changeState(EventState.REJECTED);
            }
        }

        // Invalidate game in query model
        System.out.println("Invalidating query model");
        queryModel.invalidate(((Command) transactionContext.get("command"))
            .aggregateId());
    }
}
