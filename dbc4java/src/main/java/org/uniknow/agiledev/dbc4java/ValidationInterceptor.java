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
package org.uniknow.agiledev.dbc4java;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

/**
 * Intercepts method calls of classes which are annotated with
 * {@code @Validated}.
 * 
 * @author mase
 * @since 0.1.3
 */
@Aspect
public final class ValidationInterceptor {

    private static final Logger LOGGER = Logger
        .getLogger(ValidationInterceptor.class.getName());

    /**
     * Contains instances for which invariant checks are currently in progress.
     */
    private final Set<Object> invariantChecksInProgress = Collections
        .synchronizedSet(new TreeSet());

    private final Validator validator;
    private final ExecutableValidator executableValidator;

    ValidationInterceptor() {
        validator = Validation.byProvider(HibernateValidator.class).configure()
            .failFast(true).buildValidatorFactory().getValidator();
        executableValidator = validator.forExecutables();
    }

    /**
     * Matches constructor parameters in class annotated with `@Validated`.
     * <p/>
     * *NOTE:* This will only work when class compiled with aspectj.
     */
    @Before("execution(*.new(.., @(javax.validation.constraints.* || org.hibernate.validator.constraints.*) (*), ..))")
    public final void validateConstructorParameters(final JoinPoint joinPoint)
        throws Throwable {
        // if (joinPoint.getTarget() != null) {
        final Constructor constructor = ((ConstructorSignature) joinPoint
            .getSignature()).getConstructor();

        final Set<ConstraintViolation<Object>> violations = executableValidator
            .validateConstructorParameters(constructor, joinPoint.getArgs());

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
            // new HashSet<ConstraintViolation<?>>(violations));
        }
        // }
    }

    /**
     * Validate arguments of a method invocation annotated with constraints
     */
    @Before("execution(* *(.., @(javax.validation.constraints.* || org.hibernate.validator.constraints.*) (*), ..))")
    public final void validateMethodInvocation(final JoinPoint pjp)
        throws Throwable {

        final Method method = ((MethodSignature) pjp.getSignature())
            .getMethod();
        final Object instance = pjp.getTarget();

        if ((instance != null) && (method != null)) {
            // Validate constraint(s) method parameters.
            final Object[] arguments = pjp.getArgs();
            if ((arguments != null) && (arguments.length > 0)) {
                final Set<ConstraintViolation<Object>> violations = executableValidator
                    .validateParameters(instance, method, arguments);
                if (!violations.isEmpty()) {
                    throw new ConstraintViolationException(violations);
                }
            }
        } else {
            LOGGER
                .fine("Skipped validation method parameters while method/instance is null");
        }

    }

    /**
     * Validate method response if annotated with constraints.
     */
    @AfterReturning(
        pointcut = "execution(@(javax.validation.constraints.*) * *(..))",
        returning = "result")
    public void after(final JoinPoint pjp, final Object result) {

        // final MethodSignature signature = (MethodSignature)
        // pjp.getSignature();
        final Object instance = pjp.getTarget();

        if (instance != null) {

            final Method method = ((MethodSignature) pjp.getSignature())
                .getMethod();

            if ((method != null) && !method.getReturnType().equals(Void.TYPE)) {
                // Validate constraint return value method
                final Set<ConstraintViolation<Object>> violations = executableValidator
                    .validateReturnValue(instance, method, result);
                if (!violations.isEmpty()) {
                    throw new ConstraintViolationException(violations);
                }
            } else {
                LOGGER
                    .fine("Skipped validation return value while method is null");
            }
        }
    }

}
