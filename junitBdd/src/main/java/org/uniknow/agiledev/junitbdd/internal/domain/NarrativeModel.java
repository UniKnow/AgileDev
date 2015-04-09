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
package org.uniknow.agiledev.junitbdd.internal.domain;

import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;

/**
 * Entity which contains Narrative. A narrative is a short, introductory section
 * that specifies:
 * 
 * <ul>
 * <li>who (which business or project role) is the driver or primary stakeholder
 * of the story (the actor who derives business benefit from the story)</li>
 * <li>which effect the stakeholder wants the story to have</li>
 * <li>what business value the stakeholder will derive from this story</li>
 * </ul>
 * 
 * @author mase
 */
@Validated
public class NarrativeModel {

    /**
     * Contains primary stakeholder of the story
     */
    @NotNull
    private final String as;

    /**
     * Contains description of effect the stakeholder wants the story to have
     */
    @NotNull
    private final String iWant;

    /**
     * Contains the business value the stakeholder will derive from this story
     */
    @NotNull
    private final String soThat;

    /**
     * Constructor {@code NarrativeModel}
     * 
     * @param as
     *            primary stakeholder of the story
     * @param iWant
     *            effect the stakeholder wants the story to have
     * @param soThat
     *            business value the stakeholder will derive from this story
     */
    NarrativeModel(@NotNull String as, @NotNull String iWant,
        @NotNull String soThat) {
        this.as = as;
        this.iWant = iWant;
        this.soThat = soThat;
    }

    @NotNull
    public String as() {
        return as;
    }

    @NotNull
    public String iWant() {
        return iWant;
    }

    @NotNull
    public String soThat() {
        return soThat;
    }
}
