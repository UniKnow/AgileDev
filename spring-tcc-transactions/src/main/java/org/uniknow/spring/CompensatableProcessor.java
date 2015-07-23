package org.uniknow.spring;

import org.jboss.narayana.compensations.api.Compensatable;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Element;
import java.util.Set;

/**
 * Processes annotations specific to tcc
 */
public class CompensatableProcessor  extends AbstractProcessor {

    /**
     * {@inheritDoc}
     *
     * @param annotations
     * @param roundEnv
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Compensatable.class)) {
            System.out.println("Encountered " + Compensatable.class + " annotation at " + element);
        }
        return true;
    }
}
