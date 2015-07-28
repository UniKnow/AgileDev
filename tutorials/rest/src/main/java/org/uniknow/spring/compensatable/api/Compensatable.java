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
package org.uniknow.spring.compensatable.api;

import org.springframework.transaction.annotation.Propagation;
import org.uniknow.spring.compensatable.impl.DefaultCompensationHandler;
import org.uniknow.spring.compensatable.impl.DefaultConfirmationHandler;

import java.lang.annotation.*;

/**
 * <p>
 * The Compensatable annotation provides the application the ability to
 * declarative control compensation transaction boundaries on managed beans at
 * method level.
 * </p>
 * <p>
 * This support is provided via AOP that conduct the necessary suspending.
 * <p>
 * The propagation element of the annotation indicates whether a bean method is
 * to be executed within a transaction context. Propagation.REQUIRED is the
 * default.
 * </p>
 * <p>
 * By default checked exceptions do not result in the transactional interceptor
 * marking the transaction for rollback and instances of RuntimeException and
 * its subclasses do. This default behavior can be modified by specifying
 * exceptions that result in the interceptor marking the transaction for
 * rollback and/or exceptions that do not result in rollback.
 * </p>
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Compensatable {

    public Propagation value() default Propagation.REQUIRED;

    /**
     * By setting the compensate handler we specify the class that should be
     * invoked to compensate the rolled back action.
     */
    public Class<? extends CompensationHandler> compensateHandler() default DefaultCompensationHandler.class;

    /**
     * By setting the confirmation handler we specify the class that should be
     * invoked to confirm the committed action.
     */
    public Class<? extends ConfirmationHandler> confirmationHandler() default DefaultConfirmationHandler.class;

    /**
     * The cancelOn element can be set to indicate exceptions that must cause
     * the interceptor to mark the transaction for compensation. Conversely, the
     * dontCancelOn element can be set to indicate exceptions that must not
     * cause the interceptor to mark the transaction for compensation. When a
     * class is specified for either of these elements, the designated behavior
     * applies to subclasses of that class as well. If both elements are
     * specified, dontCancelOn takes precedence.
     * 
     * @return Class[] of Exceptions
     */
    public Class[] cancelOn() default {};

    /**
     * The dontCancelOn element can be set to indicate exceptions that must not
     * cause the interceptor to mark the transaction for compensation.
     * Conversely, the cancelOn element can be set to indicate exceptions that
     * must cause the interceptor to mark the transaction for compensation. When
     * a class is specified for either of these elements, the designated
     * behavior applies to subclasses of that class as well. If both elements
     * are specified, dontCancelOn takes precedence.
     * 
     * @return Class[] of Exceptions
     */
    public Class[] dontCancelOn() default {};

    // /**
    // * The distributed element states whether a distributed or local
    // transaction should be begun,
    // * under circumstances where this annotation causes a new transaction to
    // begin.
    // *
    // * @return Class[] of Exceptions
    // */
    // @Nonbinding boolean distributed() default false;

}
