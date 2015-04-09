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
package org.uniknow.agiledev.dbc4java.examples;

import org.hibernate.validator.constraints.*;
import org.uniknow.agiledev.dbc4java.Validated;

import javax.inject.Named;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Examples of appliance constraints.
 */
@Named
@Validated
public class ExampleValidationConstraint {

    private BigDecimal price = new BigDecimal(0);

    private boolean active;

    private Date eventDate;

    private Date birthday;

    private int quantity;

    private String userName;

    private String email;

    private String identifier;

    private String phoneNumber;

    private String url;

    /**
     * Method by which price is set.
     * 
     * @param price
     *            price, should be in the range of 0.00 till 99.99 and have the
     *            format xx.xx
     */
    public void setPrice(@DecimalMin("0.00") @DecimalMax("99.99") @Digits(
        integer = 2, fraction = 2) BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Example of method for which passed boolean parameter should always be
     * {@code false}.
     * 
     * @param active
     *            boolean indicating whether customer is active; should always
     *            be {@code false}.
     */
    public void setCustomerInactive(@AssertFalse boolean active) {
        this.active = active;
    }

    /**
     * Example of method for which passed boolean parameter should always be
     * {@code true}.
     * 
     * @param active
     *            boolean indicating whether customer is active; should always
     *            be {@code true}.
     */
    public void setCustomerActive(@AssertTrue boolean active) {
        this.active = active;
    }

    /**
     * Method by which status of customer is returned.
     */
    public boolean isCustomerActive() {
        return active;
    }

    /**
     * Example of method for which the passed date must be in the future.
     */
    public void setEventDate(@Future Date eventDate) {
        this.eventDate = eventDate;
    }

    public Date getEventDate() {
        return eventDate;
    }

    /**
     * Example of method for which the passed integer value must be in the range
     * 5 till 10.
     */
    public void setQuantity(@Min(5) @Max(10) int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Example of method for which the passed value may not be {@code null},
     * empty or blank string.
     */
    public void setUserName(@NotNull @NotEmpty @NotBlank String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    /**
     * Example of method for which the passed value must be {@code null}.
     */
    public void setUnusedValue(@Null Object value) {
    }

    /**
     * Example of method for which the passed date must be in the past.
     */
    public void setBirthday(@Past Date birthday) {
        this.birthday = birthday;
    }

    public Date getBirthday() {
        return birthday;
    }

    /**
     * Example of method for which the value must match a particular reqular
     * expression
     */
    public void setPhoneNumber(
        @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Example of method for which the value must match a particular regular
     * expression.
     */
    public void setEmailAddress(@Email String email) {
        this.email = email;
    }

    public String getEmailAddress() {
        return email;
    }

    /**
     * Example of method for which the size of the String must be within certain
     * boundaries
     */
    public void setIdentifier(@Size(min = 2, max = 10) String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    /**
     * Example of method for which the passed parameter must be a valid url
     */
    public void setUrl(@URL String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
