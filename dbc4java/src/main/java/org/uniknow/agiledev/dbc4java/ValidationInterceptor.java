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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.*;
import javax.validation.executable.ExecutableValidator;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Intercepts method calls of classes which are annotated with
 * {@code @Validated}.
 * 
 * @author mase
 * @since 0.1.3
 */
@Aspect
public class ValidationInterceptor {

    private static final Logger LOGGER = Logger
        .getLogger(ValidationInterceptor.class.getName());

    /**
     * Contains instances for which invariant checks are currently in progress.
     */
    private Set<Object> invariantChecksInProgress = new HashSet<>();

    private Validator validator;

    public ValidationInterceptor() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Matches constructor parameters in class annotated with `@Validated`.
     * <p/>
     * *NOTE:* This will only work when class compiled with aspectj.
     */
    @Before("execution((@org.uniknow.agiledev.dbc4java.Validated *).new(..))")
    public void validateConstructorParameters(JoinPoint joinPoint)
        throws Throwable {
        Object instance = joinPoint.getTarget();
        if (instance != null) {
            Constructor constructor = ((ConstructorSignature) joinPoint
                .getSignature()).getConstructor();

            Set<ConstraintViolation<Object>> violations = validator
                .forExecutables().validateConstructorParameters(constructor,
                    joinPoint.getArgs());

            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(
                    new HashSet<ConstraintViolation<?>>(violations));
            }

        }
    }

    /**
     * Matches constructor in class annotated with `@Validated`.
     * <p/>
     * *NOTE:* This will only work when class compiled with aspectj.
     */
    @After("execution((@org.uniknow.agiledev.dbc4java.Validated *).new(..))")
    public void validateConstructorInvocation(JoinPoint joinPoint)
        throws Throwable {
        Object instance = joinPoint.getTarget();
        if (instance != null) {
            // Only validate constraints when object completely constructed
            if (instance.getClass().equals(
                joinPoint.getSignature().getDeclaringType())) {
                checkInvariants(instance);
            }
        }
    }

    /**
     * Verifies invariants of class. This method assures the the is only done
     * once (to prevent infinite loop).
     */
    private synchronized void checkInvariants(Object instance) {
        if (!invariantChecksInProgress.contains(instance)) {
            Set<ConstraintViolation<Object>> violations = new HashSet<>();
            try {
                invariantChecksInProgress.add(instance);

                // Validate invariants class
                violations = validator.validate(instance);

            } catch (ValidationException error) {
                if (error.getMessage().contains("HV000090")) {
                    // Error possibly caused due to post condition on getter of
                    // non class member
                    throw new ValidationException(
                        "Unable to validate post conditions for "
                            + instance.toString()
                            + ". Possibly due to post conditions on 'property' getters which are not a class member.",
                        error);
                } else {
                    throw error;
                }
            } finally {
                invariantChecksInProgress.remove(instance);
            }

            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(
                    new HashSet<ConstraintViolation<?>>(violations));
            }

        }
    }

    @Around("execution(* (@org.uniknow.agiledev.dbc4java.Validated *).*(..))"
        + "&& !execution(private * *.*(..)) "
        + "&& !execution(* *.equals(..)) " + "&& !execution(* *.hashCode(..))")
    public Object validateMethodInvocation(ProceedingJoinPoint pjp)
        throws Throwable {

        Object result;
        Set<ConstraintViolation<Object>> violations;

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Object instance = pjp.getTarget();

        ExecutableValidator executableValidator = validator.forExecutables();

        if ((instance != null) && (method != null)) {
            // Validate constraint(s) method parameters.
            Object[] arguments = pjp.getArgs();
            if ((arguments != null) && (arguments.length > 0)) {
                violations = executableValidator.validateParameters(instance,
                    method, arguments);
                if (!violations.isEmpty()) {
                    throw new ConstraintViolationException(violations);
                }
            }
        } else {
            LOGGER
                .fine("Skipped validation method parameters while method/instance is null");
        }

        result = pjp.proceed(); // Execute the method

        if (instance != null) {

            if ((method != null)
                && !signature.getReturnType().equals(Void.TYPE)) {
                // Validate constraint return value method
                violations = executableValidator.validateReturnValue(instance,
                    method, result);
                if (!violations.isEmpty()) {
                    throw new ConstraintViolationException(violations);
                }
            } else {
                LOGGER
                    .fine("Skipped validation return value while method is null");
            }

            // Validate invariants class
            checkInvariants(instance);
            // violations = validator.validate(instance);
            // if (!violations.isEmpty()) {
            // throw new ConstraintViolationException(
            // new HashSet<ConstraintViolation<?>>(violations));
            // }

        } else {
            LOGGER
                .fine("Skipped validation invariants and return value while method/instance is null");
        }
        return result;
    }

}
