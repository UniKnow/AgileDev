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
package org.uniknow.agiledev.tutorial.rest.impl;

import org.jboss.narayana.compensations.api.TxCompensate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.uniknow.agiledev.tutorial.rest.api.BlogService;
import org.uniknow.agiledev.tutorial.rest.api.domain.MyPost;
import org.uniknow.spring.compensatable.api.Compensatable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BlogServiceImpl implements BlogService {

    private static final Random ID_GENERATOR = new Random();
    private Map<Integer, MyPost> posts;

    public BlogServiceImpl() {
        System.out.println("Initiating BlogServiceImpl");
        posts = new ConcurrentHashMap<Integer, MyPost>();
        posts.put(100, getHelloWorldPost(100));
    }

    @Override
    public List<MyPost> getPosts() {
        return new ArrayList<MyPost>(posts.values());
    }

    @Override
    public MyPost getPost(int id) {
        return posts.get(id);
    }

    @Override
    @Compensatable
    @TxCompensate(RevertPost.class)
    public int addPost(MyPost post) {
        int id = post.getId();
        if (id == -1) {
            id = ID_GENERATOR.nextInt(1000);
            while (posts.containsKey(id)) {
                id = ID_GENERATOR.nextInt(1000);
            }
        }

        System.out.println("adding post");
        if (posts.containsKey(id)) {
            throw new RuntimeException("Blog with id " + id + " already exist.");
        } else {
            post.setId(id);
            posts.put(id, post);
            return id;
        }
    }

    @Override
    public boolean deletePost(int id) {
        return posts.remove(id) != null;
    }

    private MyPost getHelloWorldPost(int id) {
        MyPost post = new MyPost();
        post.setId(id);
        post.setDatePublished(new Date());
        post.setTags(Arrays.asList("hello-world", "example"));
        post.setCategories(Arrays.asList("Java", "Tutorials"));
        post.setTitle("Hello world!");
        post.setContent("This is my first post.");
        return post;
    }
}
