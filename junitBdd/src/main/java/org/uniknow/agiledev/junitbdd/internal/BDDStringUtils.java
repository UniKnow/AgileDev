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
package org.uniknow.agiledev.junitbdd.internal;

import java.util.*;

public class BDDStringUtils {

    public static String decamelise(String testName) {
        String nocamel = charAtToUpper(0, testName);
        for (int i = 1; i < testName.length(); i++) {
            char nextChar = testName.charAt(i);
            if (isUpper(nextChar)) {
                nocamel += spaceAndToLower(nextChar);
            } else {
                nocamel += nextChar;
            }
        }
        return nocamel;
    }

    private static String spaceAndToLower(char character) {
        return " " + (character + "").toLowerCase();
    }

    private static boolean isUpper(char nextChar) {
        return nextChar >= 'A' && nextChar <= 'Z';
    }

    private static String charAtToUpper(int at, String text) {
        return (text.charAt(at) + "").toUpperCase();
    }

    public static String camelise(String name) {
        String camelisedName = "";
        StringTokenizer tokenizer = new StringTokenizer(name, " ");
        while (tokenizer.hasMoreTokens()) {
            String nextToken = tokenizer.nextToken();
            camelisedName += charAtToUpper(0, nextToken)
                + nextToken.substring(1);
        }
        return camelisedName;
    }

    public static String removeSpecialCharacters(String name) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == ' ' || isBetween(c, '0', '9') || isBetween(c, 'A', 'Z')
                || c == '_' || isBetween(c, 'a', 'z'))
                sb.append(c);
        }
        return sb.toString();
    }

    private static boolean isBetween(char c, int min, int max) {
        return c >= min && c <= max;
    }

}
