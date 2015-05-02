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

import org.hibernate.validator.group.GroupSequenceProvider;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptions;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.provider.MetaDataProvider;
import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
import org.hibernate.validator.internal.metadata.raw.ConstrainedField;
import org.hibernate.validator.internal.metadata.raw.ConstrainedParameter;
import org.hibernate.validator.internal.metadata.raw.ConstrainedType;
import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap;
import org.hibernate.validator.internal.util.ReflectionHelper;
import org.hibernate.validator.internal.util.classhierarchy.ClassHierarchyHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredConstructors;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredFields;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethods;
import org.hibernate.validator.internal.util.privilegedactions.GetMethods;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import javax.validation.GroupSequence;
import javax.validation.ParameterNameProvider;
import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashMap;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;
import static org.hibernate.validator.internal.util.CollectionHelper.partition;
import static org.hibernate.validator.internal.util.ConcurrentReferenceHashMap.ReferenceType.SOFT;

/**
 * Specialization of default annotation provider. This annotation provider also
 * includes static methods
 * 
 * @author mase
 * @since 0.1.9
 */
public class DbcStaticAnnotationMetaDataProvider implements MetaDataProvider {

    private static final Log log = LoggerFactory.make();

    /**
     * The default initial capacity for this cache.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 16;

    private final ConstraintHelper constraintHelper;
    private final ConcurrentReferenceHashMap<Class<?>, BeanConfiguration<?>> configuredBeans;

    // private final AnnotationProcessingOptions annotationProcessingOptions;
    private final ParameterNameProvider parameterNameProvider;

    public DbcStaticAnnotationMetaDataProvider(
        ConstraintHelper constraintHelper,
        ParameterNameProvider parameterNameProvider) {
        this.constraintHelper = constraintHelper;
        this.parameterNameProvider = parameterNameProvider;
        // this.annotationProcessingOptions = annotationProcessingOptions;
        configuredBeans = new ConcurrentReferenceHashMap<Class<?>, BeanConfiguration<?>>(
            DEFAULT_INITIAL_CAPACITY, SOFT, SOFT);
    }

    /**
     * Returns the annotation processing options as configured by this provider.
     * 
     * @return The annotation processing options as configured by this provider.
     */
    @Override
    public AnnotationProcessingOptions getAnnotationProcessingOptions() {
        return new AnnotationProcessingOptionsImpl();
    }

    /**
     * Returns a list with the configurations for all types contained in the
     * given type's hierarchy (including implemented interfaces) starting at the
     * specified type.
     * 
     * @param beanClass
     *            The type of interest.
     * 
     * @return A set with the configurations for the complete hierarchy of the
     *         given type. May be empty, but never {@code null}.
     */
    @Override
    public <T> List<BeanConfiguration<? super T>> getBeanConfigurationForHierarchy(
        Class<T> beanClass) {
        List<BeanConfiguration<? super T>> configurations = newArrayList();

        for (Class<? super T> hierarchyClass : ClassHierarchyHelper
            .getHierarchy(beanClass)) {
            BeanConfiguration<? super T> configuration = getBeanConfiguration(hierarchyClass);
            if (configuration != null) {
                configurations.add(configuration);
            }
        }

        return configurations;
    }

    private <T> BeanConfiguration<T> getBeanConfiguration(Class<T> beanClass) {
        @SuppressWarnings("unchecked")
        BeanConfiguration<T> configuration = (BeanConfiguration<T>) configuredBeans
            .get(beanClass);

        if (configuration != null) {
            return configuration;
        }

        configuration = retrieveBeanConfiguration(beanClass);
        configuredBeans.put(beanClass, configuration);

        return configuration;
    }

    /**
     * Retrieves constraint related meta data from the annotations of the given
     * type.
     * 
     * @param beanClass
     *            The bean class for which to retrieve the meta data
     * 
     * @return Constraint related meta data from the annotations of the given
     *         type.
     */
    private <T> BeanConfiguration<T> retrieveBeanConfiguration(
        Class<T> beanClass) {
        Set<ConstrainedElement> constrainedElements = new HashSet<>();

        constrainedElements.addAll(getMethodMetaData(beanClass));

        return new BeanConfiguration<T>(ConfigurationSource.ANNOTATION,
            beanClass, constrainedElements, getDefaultGroupSequence(beanClass),
            getDefaultGroupSequenceProvider(beanClass));
    }

    /**
     * Returns order in which is which the groups need to be evaluated.
     * 
     * @param beanClass
     *            The bean class for which to retrieve the order in which the
     *            groups need to be evaluated.
     * 
     * @return list of classes defining the order in which the groups need to be
     *         evaluated, or null if no group sequence is defined.
     */
    private List<Class<?>> getDefaultGroupSequence(Class<?> beanClass) {
        GroupSequence groupSequenceAnnotation = beanClass
            .getAnnotation(GroupSequence.class);
        return groupSequenceAnnotation != null ? Arrays
            .asList(groupSequenceAnnotation.value()) : null;
    }

    private <T> DefaultGroupSequenceProvider<? super T> getDefaultGroupSequenceProvider(
        Class<T> beanClass) {
        GroupSequenceProvider groupSequenceProviderAnnotation = beanClass
            .getAnnotation(GroupSequenceProvider.class);

        if (groupSequenceProviderAnnotation != null) {
            @SuppressWarnings("unchecked")
            Class<? extends DefaultGroupSequenceProvider<? super T>> providerClass = (Class<? extends DefaultGroupSequenceProvider<? super T>>) groupSequenceProviderAnnotation
                .value();
            return newGroupSequenceProviderClassInstance(beanClass,
                providerClass);
        }

        return null;
    }

    private <T> DefaultGroupSequenceProvider<? super T> newGroupSequenceProviderClassInstance(
        Class<T> beanClass,
        Class<? extends DefaultGroupSequenceProvider<? super T>> providerClass) {
        Method[] providerMethods = run(GetMethods.action(providerClass));
        for (Method method : providerMethods) {
            Class<?>[] paramTypes = method.getParameterTypes();
            if ("getValidationGroups".equals(method.getName())
                && !method.isBridge() && paramTypes.length == 1
                && paramTypes[0].isAssignableFrom(beanClass)) {

                return run(NewInstance.action(providerClass,
                    "the default group sequence provider"));
            }
        }

        throw log.getWrongDefaultGroupSequenceProviderTypeException(beanClass
            .getName());
    }

    private Set<ConstrainedExecutable> getMethodMetaData(Class<?> clazz) {
        List<ExecutableElement> declaredMethods = ExecutableElement
            .forMethods(run(GetDeclaredMethods.action(clazz)));

        return getMetaData(declaredMethods);
    }

    private Set<ConstrainedExecutable> getMetaData(
        List<ExecutableElement> executableElements) {
        Set<ConstrainedExecutable> executableMetaData = newHashSet();

        for (ExecutableElement executable : executableElements) {
            // HV-172; ignoring synthetic methods (inserted by the compiler), as
            // they can't have any constraints
            // anyway and possibly hide the actual method with the same
            // signature in the built meta model
            Member member = executable.getMember();
            if (member.isSynthetic()) {
                continue;
            }

            executableMetaData.add(findExecutableMetaData(executable));
        }

        return executableMetaData;
    }

    /**
     * Finds all constraint annotations defined for the given method or
     * constructor.
     * 
     * @param executable
     *            The executable element to check for constraints annotations.
     * 
     * @return A meta data object describing the constraints specified for the
     *         given element.
     */
    private ConstrainedExecutable findExecutableMetaData(
        ExecutableElement executable) {
        List<ConstrainedParameter> parameterConstraints = getParameterMetaData(executable);

        AccessibleObject member = executable.getAccessibleObject();

        Map<ConstraintDescriptorImpl.ConstraintType, List<ConstraintDescriptorImpl<?>>> executableConstraints = partition(
            findConstraints(executable.getMember(), executable.getElementType()),
            byType());

        Set<MetaConstraint<?>> crossParameterConstraints = Collections
            .emptySet();

        Set<MetaConstraint<?>> returnValueConstraints;
        Map<Class<?>, Class<?>> groupConversions;
        boolean isCascading;
        boolean requiresUnwrapping = false;

        requiresUnwrapping = executable.getAccessibleObject()
            .isAnnotationPresent(UnwrapValidatedValue.class);

        returnValueConstraints = convertToMetaConstraints(
            executableConstraints
                .get(ConstraintDescriptorImpl.ConstraintType.GENERIC),
            executable);
        groupConversions = getGroupConversions(
            member.getAnnotation(ConvertGroup.class),
            member.getAnnotation(ConvertGroup.List.class));
        isCascading = executable.getAccessibleObject().isAnnotationPresent(
            Valid.class);

        return new ConstrainedExecutable(ConfigurationSource.ANNOTATION,
            ConstraintLocation.forReturnValue(executable),
            parameterConstraints, crossParameterConstraints,
            returnValueConstraints, groupConversions, isCascading,
            requiresUnwrapping);
    }

    private Set<MetaConstraint<?>> convertToMetaConstraints(
        List<ConstraintDescriptorImpl<?>> constraintsDescriptors,
        ExecutableElement executable) {
        if (constraintsDescriptors == null) {
            return Collections.emptySet();
        }

        Set<MetaConstraint<?>> constraints = newHashSet();

        for (ConstraintDescriptorImpl<?> constraintDescriptor : constraintsDescriptors) {
            constraints
                .add(constraintDescriptor.getConstraintType() == ConstraintDescriptorImpl.ConstraintType.GENERIC ? createReturnValueMetaConstraint(
                    executable, constraintDescriptor)
                    : createCrossParameterMetaConstraint(executable,
                        constraintDescriptor));
        }

        return constraints;
    }

    /**
     * Retrieves constraint related meta data for the parameters of the given
     * executable.
     * 
     * @param executable
     *            The executable of interest.
     * 
     * @return A list with parameter meta data for the given executable.
     */
    private List<ConstrainedParameter> getParameterMetaData(
        ExecutableElement executable) {
        List<ConstrainedParameter> metaData = newArrayList();

        List<String> parameterNames = executable
            .getParameterNames(parameterNameProvider);
        int i = 0;
        for (Annotation[] parameterAnnotations : executable
            .getParameterAnnotations()) {
            boolean parameterIsCascading = false;
            String parameterName = parameterNames.get(i);
            Set<MetaConstraint<?>> parameterConstraints = newHashSet();
            ConvertGroup groupConversion = null;
            ConvertGroup.List groupConversionList = null;
            boolean requiresUnwrapping = false;

            for (Annotation parameterAnnotation : parameterAnnotations) {
                // 1. mark parameter as cascading if this annotation is the
                // @Valid annotation
                if (parameterAnnotation.annotationType().equals(Valid.class)) {
                    parameterIsCascading = true;
                }
                // 2. determine group conversions
                else if (parameterAnnotation.annotationType().equals(
                    ConvertGroup.class)) {
                    groupConversion = (ConvertGroup) parameterAnnotation;
                } else if (parameterAnnotation.annotationType().equals(
                    ConvertGroup.List.class)) {
                    groupConversionList = (ConvertGroup.List) parameterAnnotation;
                }
                // 3. unwrapping required?
                else if (parameterAnnotation.annotationType().equals(
                    UnwrapValidatedValue.class)) {
                    requiresUnwrapping = true;
                }

                // 4. collect constraints if this annotation is a constraint
                // annotation
                List<ConstraintDescriptorImpl<?>> constraints = findConstraintAnnotations(
                    executable.getMember(), parameterAnnotation,
                    ElementType.PARAMETER);
                for (ConstraintDescriptorImpl<?> constraintDescriptorImpl : constraints) {
                    parameterConstraints.add(createParameterMetaConstraint(
                        executable, i, constraintDescriptorImpl));
                }
            }

            metaData.add(new ConstrainedParameter(
                ConfigurationSource.ANNOTATION, ConstraintLocation
                    .forParameter(executable, i), ReflectionHelper.typeOf(
                    executable, i), i, parameterName, parameterConstraints,
                getGroupConversions(groupConversion, groupConversionList),
                parameterIsCascading, requiresUnwrapping));
            i++;
        }

        return metaData;
    }

    /**
     * Finds all constraint annotations defined for the given member and returns
     * them in a list of constraint descriptors.
     * 
     * @param member
     *            The member to check for constraints annotations.
     * @param type
     *            The element type the constraint/annotation is placed on.
     * 
     * @return A list of constraint descriptors for all constraint specified for
     *         the given member.
     */
    private List<ConstraintDescriptorImpl<?>> findConstraints(Member member,
        ElementType type) {
        List<ConstraintDescriptorImpl<?>> metaData = newArrayList();
        for (Annotation annotation : ((AccessibleObject) member)
            .getDeclaredAnnotations()) {
            metaData
                .addAll(findConstraintAnnotations(member, annotation, type));
        }

        return metaData;
    }

    /**
     * Examines the given annotation to see whether it is a single- or
     * multi-valued constraint annotation.
     * 
     * @param annotation
     *            The annotation to examine
     * @param type
     *            the element type on which the annotation/constraint is placed
     *            on
     * 
     * @return A list of constraint descriptors or the empty list in case
     *         <code>annotation</code> is neither a single nor multi-valued
     *         annotation.
     */
    private <A extends Annotation> List<ConstraintDescriptorImpl<?>> findConstraintAnnotations(
        Member member, A annotation, ElementType type) {
        List<ConstraintDescriptorImpl<?>> constraintDescriptors = newArrayList();

        List<Annotation> constraints = newArrayList();
        Class<? extends Annotation> annotationType = annotation
            .annotationType();
        if (constraintHelper.isConstraintAnnotation(annotationType)) {
            constraints.add(annotation);
        } else if (constraintHelper.isMultiValueConstraint(annotationType)) {
            constraints.addAll(constraintHelper
                .getConstraintsFromMultiValueConstraint(annotation));
        }

        for (Annotation constraint : constraints) {
            final ConstraintDescriptorImpl<?> constraintDescriptor = buildConstraintDescriptor(
                member, constraint, type);
            constraintDescriptors.add(constraintDescriptor);
        }
        return constraintDescriptors;
    }

    private Map<Class<?>, Class<?>> getGroupConversions(
        ConvertGroup groupConversion, ConvertGroup.List groupConversionList) {
        Map<Class<?>, Class<?>> groupConversions = newHashMap();

        if (groupConversion != null) {
            groupConversions.put(groupConversion.from(), groupConversion.to());
        }

        if (groupConversionList != null) {
            for (ConvertGroup conversion : groupConversionList.value()) {
                if (groupConversions.containsKey(conversion.from())) {
                    throw log
                        .getMultipleGroupConversionsForSameSourceException(
                            conversion.from(), CollectionHelper
                                .<Class<?>> asSet(
                                    groupConversions.get(conversion.from()),
                                    conversion.to()));
                }

                groupConversions.put(conversion.from(), conversion.to());
            }
        }

        return groupConversions;
    }

    private CollectionHelper.Partitioner<ConstraintDescriptorImpl.ConstraintType, ConstraintDescriptorImpl<?>> byType() {
        return new CollectionHelper.Partitioner<ConstraintDescriptorImpl.ConstraintType, ConstraintDescriptorImpl<?>>() {

            @Override
            public ConstraintDescriptorImpl.ConstraintType getPartition(
                ConstraintDescriptorImpl<?> v) {
                return v.getConstraintType();
            }
        };
    }

    private <A extends Annotation> MetaConstraint<A> createParameterMetaConstraint(
        ExecutableElement member, int parameterIndex,
        ConstraintDescriptorImpl<A> descriptor) {
        return new MetaConstraint<A>(descriptor,
            ConstraintLocation.forParameter(member, parameterIndex));
    }

    private <A extends Annotation> MetaConstraint<A> createReturnValueMetaConstraint(
        ExecutableElement member, ConstraintDescriptorImpl<A> descriptor) {
        return new MetaConstraint<A>(descriptor,
            ConstraintLocation.forReturnValue(member));
    }

    private <A extends Annotation> MetaConstraint<A> createCrossParameterMetaConstraint(
        ExecutableElement member, ConstraintDescriptorImpl<A> descriptor) {
        return new MetaConstraint<A>(descriptor,
            ConstraintLocation.forCrossParameter(member));
    }

    private <A extends Annotation> ConstraintDescriptorImpl<A> buildConstraintDescriptor(
        Member member, A annotation, ElementType type) {
        return new ConstraintDescriptorImpl<A>(constraintHelper, member,
            annotation, type);
    }

    /**
     * Runs the given privileged action, using a privileged block if required.
     * <p>
     * <b>NOTE:</b> This must never be changed into a publicly available method
     * to avoid execution of arbitrary privileged actions within HV's protection
     * domain.
     */
    private <T> T run(PrivilegedAction<T> action) {
        return System.getSecurityManager() != null ? AccessController
            .doPrivileged(action) : action.run();
    }
}
