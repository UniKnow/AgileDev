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
package com.tomtom.support.mysql;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.mysql.management.MysqldResource;
import com.mysql.management.MysqldResourceI;

/**
 * Manages the life cycle of the embedded MySQL database. This manager uses the
 * MySQL Connector/MXJ and Mysql Connector/J JDBC driver to manage the embedded
 * MySQL DB instance.
 * <p/>
 * See http://dev.mysql.com/doc/refman/5.1/en/connector-mxj.html for more
 * details
 * 
 * 
 * @since October 2014.
 * @author a.stoisavljevic
 * 
 *         As from Spring 3.1.3 {@link SimpleJdbcTemplate} and
 *         {@link SimpleJdbcTestUtils} are deprecated we should use
 *         {@link JdbcTemplate} and {@link JdbcTestUtils}. Therefore latest
 *         change is to amend those deprecated Spring Framework classes for
 *         newer once
 * 
 * @see line 131 and line 137
 */
public class EmbeddedMysqlManager {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String USERNAME = "sa";

    private static final int DEFAULT_AUTOMATIC_MYSQL_PORT_NUMBER = 13306;

    private MysqldResource mysqldResource;
    private String baseDatabaseDir = System.getProperty("java.io.tmpdir")
        + File.separator + "db-tests";
    private String databaseName = "test_db_" + System.nanoTime();
    private String username = USERNAME;
    private String password = null;
    private int port = -1;
    private List<String> sqlScripts = new ArrayList<String>();
    private DriverManagerDataSource datasource;

    /**
     * Contains additional options for embedded mysql database
     */
    private Map<String, String> databaseOptions = new HashMap<String, String>();

    public Map<String, String> getDatabaseOptions() {
        return databaseOptions;
    }

    public void setDatabaseOptions(Map<String, String> databaseOptions) {
        if (databaseOptions != null) {
            this.databaseOptions = databaseOptions;
        }
    }

    private MysqldResource getMysqldResource() {
        if (mysqldResource == null) {
            File databaseDir = new File(new File(baseDatabaseDir), databaseName);
            mysqldResource = new MysqldResource(databaseDir);
        }
        return mysqldResource;
    }

    public synchronized void startDatabase() {
        MysqldResource resource;

        if (port < 0) {
            log.info("no port specified, choosing one automatically");
            port = DEFAULT_AUTOMATIC_MYSQL_PORT_NUMBER;
            while (!available(port)) {
                log.debug("Trying next port");
                ++port;
            }
            // setting password the same as the port number makes it harder
            // to accidentally use the wrong DB in tests
            if (password == null) {
                log.info("Password will be equal to port number");
                password = Integer.toString(port);
            }

            // separate out multiple versions of the database running
            // simultaneously
            // baseDatabaseDir += File.separator + password;
        }

        if (log.isDebugEnabled()) {
            log.debug("=============== Starting Embedded MySQL using these parameters ===============");
            log.debug("baseDatabaseDir : " + baseDatabaseDir);
            log.debug("databaseName : " + databaseName);
            log.debug("host : localhost (hardcoded)");
            log.debug("port : " + port);
            log.debug("username : " + username);
            log.debug("password : " + password);
            log.debug("=============================================================================");
        }

        resource = getMysqldResource();

        Map<String, String> database_options = new HashMap<String, String>();
        database_options.putAll(getDatabaseOptions());
        database_options.put(MysqldResourceI.PORT, Integer.toString(port));
        database_options.put(MysqldResourceI.INITIALIZE_USER, "true");
        database_options.put(MysqldResourceI.INITIALIZE_USER_NAME, username);
        database_options.put(MysqldResourceI.INITIALIZE_PASSWORD, password);

        resource.start("embedded-mysqld-thread-" + System.currentTimeMillis(),
            database_options);

        if (!resource.isRunning()) {
            throw new RuntimeException("MySQL did not start.");
        }

        log.info("MySQL started successfully @ " + System.currentTimeMillis());

        try {
            if (!sqlScripts.isEmpty()) {
                // SimpleJdbcTemplate simpleJdbcTemp = new
                // SimpleJdbcTemplate(getDatasource());
                JdbcTemplate jdbcTemplate = new JdbcTemplate(getDatasource());
                log.info("Executing scripts...");

                for (String script : sqlScripts) {
                    log.info("Executing script [" + script + "]");
                    // SimpleJdbcTestUtils.executeSqlScript(simpleJdbcTemp, new
                    // ClassPathResource(script), false);
                    JdbcTestUtils.executeSqlScript(jdbcTemplate,
                        new ClassPathResource(script), false);
                }
            } else {
                log.info("No scripts to load...");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if ((resource != null) && (resource.isRunning())) {
                shutdownDatabase();
            }
        }
    }

    public synchronized void shutdownDatabase() {
        log.info("Shutdown MySQL instance");
        MysqldResource resource = getMysqldResource();
        resource.shutdown();
        if (resource.isRunning() == false) {
            log.info(">>>>>>>>>> DELETING MYSQL BASE DIR ["
                + resource.getBaseDir() + "] <<<<<<<<<<");
            try {
                FileUtils.forceDelete(resource.getBaseDir());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            mysqldResource = null;
        }
    }

    /**
     * Assures MySQL resources are properly shutdown
     * 
     * @throws Throwable
     */
    // protected final void finalize() throws Throwable {
    // log.info("Shutdown any running MySQL instance.");
    // try {
    // // MysqldResource resource = getMysqldResource();
    // // if ((resource != null) && (resource.isRunning())) {
    // // shutdownDatabase();
    // // FileUtils.forceDelete(re.getBaseDir());
    // // mysqldResource = null;
    // // }
    // shutdownDatabase();
    // } finally {
    // super.finalize();
    // }
    // }

    /**
     * @return a {@link DataSource} for the embedded DB managed by this manager
     */
    public synchronized DataSource getDatasource() {
        if (!mysqldResource.isRunning()) {
            log.error("MySQL instance not found... Terminating");
            throw new RuntimeException(
                "Cannot get Datasource, MySQL instance not started.");
        }

        if (datasource == null) {
            datasource = new DriverManagerDataSource();
            datasource.setDriverClassName("com.mysql.jdbc.Driver");

            datasource.setUrl(getUrl());
            datasource.setUsername(username);
            datasource.setPassword(password);
        }
        return datasource;
    }

    /**
     * @return the database JDBC URL.
     */
    public final synchronized String getUrl() {
        return "jdbc:mysql://localhost:"
            + port
            + "/"
            + databaseName
            + "?"
            + "createDatabaseIfNotExist=true&sessionVariables=FOREIGN_KEY_CHECKS=0";
    }

    /**
     * @return the baseDatabaseDir
     */
    public final synchronized String getBaseDatabaseDir() {
        return baseDatabaseDir;
    }

    /**
     * @param baseDatabaseDir
     *            the baseDatabaseDir to set
     */
    public final synchronized void setBaseDatabaseDir(String baseDatabaseDir) {
        this.baseDatabaseDir = baseDatabaseDir;
    }

    /**
     * @return the databaseName
     */
    public final synchronized String getDatabaseName() {
        return databaseName;
    }

    /**
     * @param databaseName
     *            the databaseName to set
     */
    public final synchronized void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * @return the port
     */
    public final synchronized int getPort() {
        return port;
    }

    /**
     * @param port
     *            the port to set
     */
    public final synchronized void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the username to connect with.
     */
    public final synchronized String getUsername() {
        return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public final synchronized void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password to connect with.
     */
    public final synchronized String getPassword() {
        return password;
    }

    /**
     * Set password to connect with
     * 
     * @param password
     */
    public final synchronized void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the sqlScripts
     */
    public final synchronized List<String> getSqlScripts() {
        return sqlScripts;
    }

    /**
     * @param sqlScripts
     *            the sqlScripts to set
     */
    public final synchronized void setSqlScripts(List<String> sqlScripts) {
        this.sqlScripts = sqlScripts;
    }

    /**
     * check availability of port, where to run MySQL service
     * 
     * @param port
     * @return true if port is available
     */
    private boolean available(int port) {
        log.debug("Testing port " + port);
        Socket s = null;
        try {
            s = new Socket("localhost", port);
            // If the code makes it this far something is using the port and has
            // responded.
            log.debug("Port " + port + " is not available");
            return false;
        } catch (IOException e) {
            log.debug("Port " + port + " is available");
            return true;
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException ioEx) {
                    // we did our best
                    log.error("ERROR! We tried our best: {}", ioEx.getMessage());
                }
            }
        }
    }
}
