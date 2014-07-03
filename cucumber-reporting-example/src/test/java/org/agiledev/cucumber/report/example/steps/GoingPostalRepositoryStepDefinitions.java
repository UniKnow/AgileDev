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
package org.agiledev.cucumber.report.example.steps;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.agiledev.cucumber.report.example.Book;
import org.agiledev.cucumber.report.example.repository.DefaultFavoritesRepository;
import org.agiledev.cucumber.report.example.repository.FavoritesRepository;

import static org.junit.Assert.*;

public class GoingPostalRepositoryStepDefinitions {

    private FavoritesRepository repository = new DefaultFavoritesRepository();

    @When("^Going Postal is added to Favorites Repository$")
    public void whenGoingPostalIsAddedToFavoritesRepository() {
        Book book = new Book("9781407035406", "Terry Pratchett", "Going Postal");
        repository.putFavorite(book);
    }

    @Then("^favorite book is Going Postal$")
    public void thenFavoriteBookIsGoingPostal() {
        Book goingPostal = new Book("9781407035406", "Terry Pratchett",
            "Going Postal");
        assertEquals(goingPostal, repository.getFavorite(Book.class));
    }

}
