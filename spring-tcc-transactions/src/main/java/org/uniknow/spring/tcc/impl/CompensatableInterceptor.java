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
package org.uniknow.spring.tcc.impl;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.uniknow.spring.tcc.api.Compensatable;
import org.uniknow.spring.tcc.api.CompensatableContext;
import org.uniknow.spring.tcc.api.CompensatableTransactionContext;
import org.uniknow.spring.tcc.api.CompensatableTransactionManager;

import java.lang.annotation.Annotation;

/**
 * Intercepts methods which are annotated with @Compensatable.
 */
@Aspect
@Component
public class CompensatableInterceptor {

    @Autowired
    CompensatableTransactionManager transactionManager;

    @Around("@annotation(compensatable)")
    public Object invoke(ProceedingJoinPoint pjp, Compensatable compensatable)
        throws Throwable {
        System.out.println("Starting compensating transaction for "
            + pjp.toString());

        // Creating transaction definition based on annotation.
        CompensatableTransactionDefinition transactionDefinition = new CompensatableTransactionDefinition(
            compensatable);
        System.out.println("Retrieve transaction for " + transactionDefinition);
        TransactionStatus status = transactionManager
            .getTransaction(transactionDefinition);

        // Extract all input parameters and persist them within transaction
        // context
        CompensatableTransactionContext transactionContext = transactionDefinition
            .getTransactionContext();
        extractInputParameters(pjp, transactionContext);

        Object result = null;
        try {
            result = pjp.proceed();

            // Check whether method is annotated with
            // CompensatableContext
            CompensatableContext compensatableContextAnnotation = AnnotationUtils
                .getAnnotation(
                    ((MethodSignature) pjp.getSignature()).getMethod(),
                    CompensatableContext.class);
            if (compensatableContextAnnotation != null) {
                transactionContext.put(compensatableContextAnnotation.value(),
                    result);
            }

            System.out.println("Invoking commit for " + pjp.toString()
                + ", status " + status + " on " + transactionManager);
            transactionManager.commit(status);
        } catch (RuntimeException error) {
            System.out
                .println("Call compensating transaction while following exception occurred "
                    + error);
            transactionManager.rollback(status);
            throw (error);
        }

        return result;
    }

    /**
     * Initializes the compensatable transaction context. During initialization
     * the parameters which are annotated with CompensatableTransactionContext
     * are added to the context.
     * 
     * @param pjp
     *            joint point from which the annotated input parameters are
     *            extracted
     * @param transactionContext
     *            context to which the annotated input parameters are added
     */
    private void extractInputParameters(ProceedingJoinPoint pjp,
        CompensatableTransactionContext transactionContext) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();

        // Filter all input arguments
        Object[] arguments = pjp.getArgs();
        Annotation[][] annotations = methodSignature.getMethod()
            .getParameterAnnotations();
        for (int i = 0; i < arguments.length; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation.annotationType() == CompensatableContext.class) {
                    // Append argument to transaction context
                    CompensatableContext contextDef = (CompensatableContext) annotation;
                    System.out.println("Adding argument " + contextDef.value()
                        + " with value " + arguments[i]
                        + " to transaction context");
                    transactionContext.put(contextDef.value(), arguments[i]);
                }
            }
        }
    }

    // /**
    // * Intercept methods which are annotated with CompensatableContext. For
    // those method the return value will be appended to the compensatable
    // context
    // */
    // @After("@annotation(compensatableContext")
    // public void AfterExecution(JoinPoint joinPoint, CompensatableContext
    // compensatableContext) {
    // System.out.println("Intercepted method annotated with CompensatableContext after")
    // }

}
