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
package org.uniknow.agiledev.tutorial.rest.impl.jaxrs;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.uniknow.agiledev.tutorial.rest.api.BlogService;
import org.uniknow.agiledev.tutorial.rest.api.domain.MyPost;
import org.uniknow.agiledev.tutorial.rest.api.jaxrs.V1.BlogRestService;
import org.uniknow.agiledev.tutorial.rest.api.jaxrs.V1.Post;

import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of V1 blog rest service
 */
@Validated
@Component
public class BlogRestServiceV1Impl implements BlogRestService {

    private BlogService service;
    private Mapper mapper;

    @Autowired
    public BlogRestServiceV1Impl(@NotNull BlogService service,
        @NotNull Mapper mapper) {
        System.out.println("Initiating BlogServiceV1Impl");
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<Post> getPosts() {
        List<MyPost> posts = service.getPosts();
        List<Post> dtoPosts = new ArrayList<Post>(posts.size());
        for (MyPost myPost : posts) {
            dtoPosts.add(mapper.map(myPost, Post.class));
        }
        return dtoPosts;
    }

    @Override
    public Post getPost(@PathParam("id") int id) {
        MyPost post = service.getPost(id);
        if (post == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return mapper.map(post, Post.class);
    }

    @Override
    public Response addPost(Post post) {
        int id = service.addPost(mapper.map(post, MyPost.class));
        return Response.created(
            UriBuilder.fromPath(Integer.toString(id)).build()).build();
    }

    @Override
    public Response deletePost(@PathParam("id") int id) {
        boolean success = service.deletePost(id);
        if (!success) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return Response.ok().build();
    }
}
