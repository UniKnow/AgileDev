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
package org.uniknow.spring.eventStore;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Iterator for EventStream. This iterator will filter out all Events with
 * status REJECTED
 */
class EventStreamIterator<E extends Event> implements Iterator<E> {

    private final Iterator<E> iterator;

    EventStreamIterator(List<E> events) {
        if (events == null) {
            throw new IllegalArgumentException("Events may not be null");
        } else {
            // Create filtered list of events
            List<E> filteredEvents = new Vector();
            for (E event : events) {
                // TODO: Check whether we should clone event so that changes
                // done on Event don't affect this stream
                // Ignore events which are rejected.
                if (event.getState() == EventState.OK) {
                    filteredEvents.add(event);
                } else {
                    System.out.println("Ignoring event " + event);
                }
            }
            iterator = filteredEvents.iterator();
        }
    }

    /**
     * Returns {@code true} if the iteration has more elements. (In other words,
     * returns {@code true} if {@link #next} would return an element rather than
     * throwing an exception.)
     * 
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * Returns the next element in the iteration.
     * 
     * @return the next element in the iteration
     * @throws NoSuchElementException
     *             if the iteration has no more elements
     */
    @Override
    public E next() {
        return iterator.next();
    }

    /**
     * Removes from the underlying collection the last element returned by this
     * iterator (optional operation). This method can be called only once per
     * call to {@link #next}. The behavior of an iterator is unspecified if the
     * underlying collection is modified while the iteration is in progress in
     * any way other than by calling this method.
     * 
     * @throws UnsupportedOperationException
     *             if the {@code remove} operation is not supported by this
     *             iterator
     * @throws IllegalStateException
     *             if the {@code next} method has not yet been called, or the
     *             {@code remove} method has already been called after the last
     *             call to the {@code next} method
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
