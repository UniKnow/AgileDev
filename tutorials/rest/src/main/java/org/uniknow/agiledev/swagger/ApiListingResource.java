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
package org.uniknow.agiledev.swagger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.config.FilterFactory;
import io.swagger.config.Scanner;
import io.swagger.config.ScannerFactory;
import io.swagger.config.SwaggerConfig;
import io.swagger.core.filter.SpecFilter;
import io.swagger.core.filter.SwaggerSpecFilter;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.JaxrsScanner;
import io.swagger.jaxrs.config.ReaderConfigUtils;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import io.swagger.models.Swagger;
import io.swagger.util.Yaml;
import org.apache.http.HttpStatus;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;
import java.util.*;

/**
 * Customization Swagger Api listing resource. Attempt to create swagger json
 * file containing API specs for different versions using the accept/provide
 * header technology,
 */
@Path("/")
public class ApiListingResource {
    static boolean initialized = false;
    Logger LOGGER = LoggerFactory
        .getLogger(io.swagger.jaxrs.listing.ApiListingResource.class);
    @Context
    ServletContext context;

    /**
     * Returns swagger instance for specified version. If version is null
     * 
     * @param app
     * @param sc
     * @return
     */
    protected synchronized Swagger scan(Application app, ServletConfig sc,
        @NotNull String version) {
        Swagger swagger = null;
        Scanner scanner = ScannerFactory.getScanner();
        LOGGER.debug("using scanner " + scanner);

        if (scanner != null) {
            // If scanner instance of
            SwaggerSerializers.setPrettyPrint(scanner.getPrettyPrint());
            swagger = (Swagger) context.getAttribute("swagger");

            Set<Class<?>> classes = new HashSet<Class<?>>();
            if (scanner instanceof JaxrsScanner) {
                JaxrsScanner jaxrsScanner = (JaxrsScanner) scanner;
                classes = jaxrsScanner.classesFromContext(app, sc);
            } else if (scanner instanceof CustomizedSwaggerConfig) {
                classes = ((CustomizedSwaggerConfig) scanner).classes(version);
            } else {
                classes = scanner.classes();
            }
            System.out.println("Processing classes " + classes);
            if (classes != null) {
                Reader reader = new Reader(swagger,
                    ReaderConfigUtils.getReaderConfig(context));
                swagger = reader.read(classes);
                if (scanner instanceof SwaggerConfig) {
                    swagger = ((SwaggerConfig) scanner).configure(swagger);
                } else {
                    SwaggerConfig configurator = (SwaggerConfig) context
                        .getAttribute("reader");
                    if (configurator != null) {
                        LOGGER
                            .debug("configuring swagger with " + configurator);
                        configurator.configure(swagger);
                    } else {
                        LOGGER.debug("no configurator");
                    }
                }
                context.setAttribute("swagger", swagger);
            }
        }
        initialized = true;
        return swagger;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/swagger.json")
    @ApiOperation(value = "The swagger definition in JSON", hidden = true)
    public Response getListingJson(@Context Application app,
        @Context ServletConfig sc, @Context HttpHeaders headers,
        @Context UriInfo uriInfo, @QueryParam("version") String version) {

        if (version == null) {
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        } else {
            Swagger swagger = (Swagger) context.getAttribute("swagger");
            swagger = scan(app, sc, version);
            if (swagger != null) {
                SwaggerSpecFilter filterImpl = FilterFactory.getFilter();
                if (filterImpl != null) {
                    SpecFilter f = new SpecFilter();
                    swagger = f.filter(swagger, filterImpl,
                        getQueryParams(uriInfo.getQueryParameters()),
                        getCookies(headers), getHeaders(headers));
                }
                return Response.ok().entity(swagger).build();
            } else {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            }
        }
    }

    @GET
    @Produces("application/yaml")
    @Path("/swagger.yaml")
    @ApiOperation(value = "The swagger definition in YAML", hidden = true)
    public Response getListingYaml(@Context Application app,
        @Context ServletConfig sc, @Context HttpHeaders headers,
        @Context UriInfo uriInfo, @QueryParam("version") String version) {

        if (version == null) {
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        } else {
            Swagger swagger = (Swagger) context.getAttribute("swagger");
            swagger = scan(app, sc, version);
            try {
                if (swagger != null) {
                    SwaggerSpecFilter filterImpl = FilterFactory.getFilter();
                    LOGGER.debug("using filter " + filterImpl);
                    if (filterImpl != null) {
                        SpecFilter f = new SpecFilter();
                        swagger = f.filter(swagger, filterImpl,
                            getQueryParams(uriInfo.getQueryParameters()),
                            getCookies(headers), getHeaders(headers));
                    }

                    String yaml = Yaml.mapper().writeValueAsString(swagger);
                    String[] parts = yaml.split("\n");
                    StringBuilder b = new StringBuilder();
                    for (String part : parts) {
                        int pos = part.indexOf("!<");
                        int endPos = part.indexOf(">");
                        b.append(part);
                        b.append("\n");
                    }
                    return Response.ok().entity(b.toString())
                        .type("application/yaml").build();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Response.status(404).build();
        }
    }

    protected Map<String, List<String>> getQueryParams(
        MultivaluedMap<String, String> params) {
        Map<String, List<String>> output = new HashMap<String, List<String>>();
        if (params != null) {
            for (String key : params.keySet()) {
                List<String> values = params.get(key);
                output.put(key, values);
            }
        }
        return output;
    }

    protected Map<String, String> getCookies(HttpHeaders headers) {
        Map<String, String> output = new HashMap<String, String>();
        if (headers != null) {
            for (String key : headers.getCookies().keySet()) {
                Cookie cookie = headers.getCookies().get(key);
                output.put(key, cookie.getValue());
            }
        }
        return output;
    }

    protected Map<String, List<String>> getHeaders(HttpHeaders headers) {
        Map<String, List<String>> output = new HashMap<String, List<String>>();
        if (headers != null) {
            for (String key : headers.getRequestHeaders().keySet()) {
                List<String> values = headers.getRequestHeaders().get(key);
                output.put(key, values);
            }
        }
        return output;
    }
}
