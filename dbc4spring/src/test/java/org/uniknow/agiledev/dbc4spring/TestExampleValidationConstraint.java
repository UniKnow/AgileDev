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
package org.uniknow.agiledev.dbc4spring;

import org.aspectj.lang.annotation.Before;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Verifies constraints which are applied in {@code ExampleValidationConstraint}
 * .
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/service-spring-config.xml")
public class TestExampleValidationConstraint {

    @Inject
    private ExampleValidationConstraint example;

    /**
     * Verify that price can't be below 0
     */
    @Test(expected = ValidationException.class)
    public void testPriceBelowMin() {
        example.setPrice(new BigDecimal("-0.01"));
    }

    /**
     * Verifies that price can't be above 100.00
     */
    @Test(expected = ValidationException.class)
    public void testPriceAboveMax() {
        example.setPrice(new BigDecimal("100.00"));
    }

    /**
     * Verifies that format of price other than xx.xx causes exception
     */
    @Test(expected = ValidationException.class)
    public void testSetPriceInvalidFormat() {
        example.setPrice(new BigDecimal("0.001"));
    }

    /**
     * Verifies that price is properly set
     */
    @Test
    public void testSetPrice() {
        // Random randomizer = new Random();
        // float price = randomizer.nextInt(9999) / 100;
        BigDecimal price = new BigDecimal("64.11");

        example.setPrice(price);
        assertEquals(price, example.getPrice());
    }

    /**
     * Verifies exception is thrown when method for which boolean always must be
     * {@code false} is set to {@code true}.
     */
    @Test(expected = ValidationException.class)
    public void testSetCustomerInactiveWithInvalidValue() {
        example.setCustomerInactive(true);
    }

    /**
     * Verifies status of customer is properly set.
     */
    @Test
    public void testSetCustomerInactive() {
        example.setCustomerInactive(false);
        assertFalse(example.isCustomerActive());
    }

    /**
     * Verifies exception is thrown when method for which boolean always must be
     * {@code true} is set to {@code false}.
     */
    @Test(expected = ValidationException.class)
    public void testSetCustomerActiveWithInvalidValue() {
        example.setCustomerActive(false);
    }

    /**
     * Verifies status of customer is properly set.
     */
    @Test
    public void testSetCustomerActive() {
        example.setCustomerActive(true);
        assertTrue(example.isCustomerActive());
    }

    /**
     * Verifies exception is thrown when speficied date is not within the future
     */
    @Test(expected = ValidationException.class)
    public void testSetEventDatePast() {
        Date eventDate = new Date(System.currentTimeMillis() - 1000);
        example.setEventDate(eventDate);
    }

    /**
     * Verifies event date is properly set
     */
    @Test
    public void testSetEventDate() {
        Date eventDate = new Date(System.currentTimeMillis() + 1000);
        example.setEventDate(eventDate);
    }

    /**
     * Verifies exception is thrown when passed integer value is below minimum.
     */
    @Test(expected = ValidationException.class)
    public void testSetQuantityBelowMinimum() {
        example.setQuantity(4);
    }

    /**
     * Verifies exception is thrown when passed integer value is above maximum.
     */
    @Test(expected = ValidationException.class)
    public void testSetQuantityAboveMaximum() {
        example.setQuantity(11);
    }

    /**
     * Verifies quantity is properly set
     */
    @Test
    public void testSetQuantity() {
        example.setQuantity(7);
        assertEquals(7, example.getQuantity());
    }

    /**
     * Verifies exception is thrown when passed value is {@code null}.
     */
    @Test(expected = ValidationException.class)
    public void testSetUserNameNull() {
        example.setUserName(null);
    }

    /**
     * Verifies exception is thrown when passed value is empty string
     */
    @Test(expected = ValidationException.class)
    public void testSetUserNameEmptyString() {
        example.setUserName("");
    }

    /**
     * Verifies exception is thrown when passed value is blank string
     */
    @Test(expected = ValidationException.class)
    public void testSetUserNameBlankString() {
        example.setUserName("    ");
    }

    /**
     * Verifies user name is properly set.
     */
    @Test
    public void testSetUserName() {
        example.setUserName("TEST");
        assertEquals("TEST", example.getUserName());
    }

    /**
     * Verifies exception is thrown when passed value is not {@code null}.
     */
    @Test(expected = ValidationException.class)
    public void testSetUnusedValueNotNull() {
        example.setUnusedValue(new Object());
    }

    /**
     * Verifies unused value is properly set
     */
    @Test
    public void testSetUnusedValue() {
        example.setUnusedValue(null);
    }

    /**
     * Verifies exception is thrown when passed value is in the future.
     */
    @Test(expected = ValidationException.class)
    public void testSetBirthdayFuture() {
        Date birthday = new Date(System.currentTimeMillis() + 1000);
        example.setBirthday(birthday);
    }

    /**
     * Verifies birthday is properly set
     */
    @Test
    public void testSetBirthday() {
        Date birthday = new Date(System.currentTimeMillis() - 1000);
        example.setBirthday(birthday);
        assertEquals(birthday, example.getBirthday());
    }

    /**
     * Verifies exception is thrown when passed value doesn't comply to email
     * format
     */
    @Test(expected = ValidationException.class)
    public void testSetEmailAddressInvalidValue() {
        example.setEmailAddress("TEST");
    }

    /**
     * Verifies email address is properly set.
     */
    @Test
    public void testSetEmailAddress() {
        String emailAddress = "test@test.com";

        example.setEmailAddress(emailAddress);
        assertEquals(emailAddress, example.getEmailAddress());
    }

    /**
     * Verifies exception is thrown when passed String is not within the
     * specified boundaries
     */
    @Test(expected = ValidationException.class)
    public void testSetIdentifierBelowMinLength() {
        example.setIdentifier("0");
    }

    /**
     * Verifies exception is thrown when passed String is not within the
     * specified boundaries
     */
    @Test(expected = ValidationException.class)
    public void testSetIdentifierAboveMaxLength() {
        example.setIdentifier("01234567890");
    }

    /**
     * Set identifier
     */
    @Test
    public void testSetIdentifier() {
        String identifier = "012345";

        example.setIdentifier(identifier);
        assertEquals(identifier, example.getIdentifier());
    }

    /**
     * Verifies exception is thrown when passed String doesn't comply to the
     * international phone number pattern.
     */
    @Test(expected = ValidationException.class)
    public void testSetPhoneNumberInvalid() {
        example.setPhoneNumber("234-567890");
    }

    /**
     * Set international phone number
     */
    @Test
    public void testSetPhoneNumber() {
        String phoneNumber = "+31 0123456789";

        example.setPhoneNumber(phoneNumber);
        assertEquals(phoneNumber, example.getPhoneNumber());
    }

    /**
     * Verifies exception is thrown when passed String is not a valid url
     */
    @Test(expected = ValidationException.class)
    public void testSetUrlInvalidValue() {
        example.setUrl("TEST");
    }

    /**
     * Set url
     */
    @Test
    public void testSetUrl() {
        String url = "http://www.google.com";

        example.setUrl(url);
        assertEquals(url, example.getUrl());
    }
}
