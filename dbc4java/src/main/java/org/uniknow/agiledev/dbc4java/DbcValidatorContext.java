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

import org.hibernate.validator.HibernateValidatorContext;
import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;

import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;

public class DbcValidatorContext implements HibernateValidatorContext {

    private final DbcValidatorFactory validatorFactory;

    private MessageInterpolator messageInterpolator;
    private TraversableResolver traversableResolver;
    private ConstraintValidatorFactory constraintValidatorFactory;
    private ParameterNameProvider parameterNameProvider;

    private boolean failFast;
    private final List<ValidatedValueUnwrapper<?>> validatedValueHandlers;

    public DbcValidatorContext(DbcValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
        this.messageInterpolator = validatorFactory.getMessageInterpolator();
        this.traversableResolver = validatorFactory.getTraversableResolver();
        this.constraintValidatorFactory = validatorFactory
            .getConstraintValidatorFactory();
        this.parameterNameProvider = validatorFactory
            .getParameterNameProvider();
        this.failFast = validatorFactory.isFailFast();
        this.validatedValueHandlers = new ArrayList<ValidatedValueUnwrapper<?>>(
            validatorFactory.getValidatedValueHandlers());
    }

    @Override
    public HibernateValidatorContext messageInterpolator(
        MessageInterpolator messageInterpolator) {
        if (messageInterpolator == null) {
            this.messageInterpolator = validatorFactory
                .getMessageInterpolator();
        } else {
            this.messageInterpolator = messageInterpolator;
        }
        return this;
    }

    @Override
    public HibernateValidatorContext traversableResolver(
        TraversableResolver traversableResolver) {
        if (traversableResolver == null) {
            this.traversableResolver = validatorFactory
                .getTraversableResolver();
        } else {
            this.traversableResolver = traversableResolver;
        }
        return this;
    }

    @Override
    public HibernateValidatorContext constraintValidatorFactory(
        ConstraintValidatorFactory factory) {
        if (factory == null) {
            this.constraintValidatorFactory = validatorFactory
                .getConstraintValidatorFactory();
        } else {
            this.constraintValidatorFactory = factory;
        }
        return this;
    }

    @Override
    public HibernateValidatorContext parameterNameProvider(
        ParameterNameProvider parameterNameProvider) {
        if (parameterNameProvider == null) {
            this.parameterNameProvider = validatorFactory
                .getParameterNameProvider();
        } else {
            this.parameterNameProvider = parameterNameProvider;
        }
        return this;
    }

    @Override
    public HibernateValidatorContext failFast(boolean failFast) {
        this.failFast = failFast;
        return this;
    }

    @Override
    public HibernateValidatorContext addValidationValueHandler(
        ValidatedValueUnwrapper<?> handler) {
        this.validatedValueHandlers.add(handler);
        return this;
    }

    @Override
    public Validator getValidator() {
        return validatorFactory.createValidator(constraintValidatorFactory,
            messageInterpolator, traversableResolver, parameterNameProvider,
            failFast, validatedValueHandlers);
    }
}
