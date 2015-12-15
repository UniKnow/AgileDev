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
package org.uniknow.agiledev.support.mysql.maven.plugin;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.uniknow.agiledev.support.mysql.EmbeddedMysqlManager;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Abstract MOJO containting instance of EmbeddedMysqlManager by which instance
 * of mysql server can be started and stopped
 */
public abstract class AbstractEmbeddedMysqlMojo extends AbstractMojo {

    /**
     * Location at which MySQL data will be persisted
     * 
     * @parameter default-value="${project.build.directory}/mysql-data"
     * @required
     */
    public File baseDir;

    /**
     * Database name to use
     * 
     * @parameter default-value="test_db"
     */
    String dbname;

    private static EmbeddedMysqlManager instance = null;

    private static final EmbeddedMysqlManager getInstance() {
        if (instance == null) {
            instance = new EmbeddedMysqlManager();
        }

        return instance;
    }

    // /**
    // * Called by the garbage collector on an object when garbage collection
    // * determines that there are no more references to the object.
    // * A subclass overrides the {@code finalize} method to dispose of
    // * system resources or to perform other cleanup.
    // * <p/>
    // * The general contract of {@code finalize} is that it is invoked
    // * if and when the Java<font size="-2"><sup>TM</sup></font> virtual
    // * machine has determined that there is no longer any
    // * means by which this object can be accessed by any thread that has
    // * not yet died, except as a result of an action taken by the
    // * finalization of some other object or class which is ready to be
    // * finalized. The {@code finalize} method may take any action, including
    // * making this object available again to other threads; the usual purpose
    // * of {@code finalize}, however, is to perform cleanup actions before
    // * the object is irrevocably discarded. For example, the finalize method
    // * for an object that represents an input/output connection might perform
    // * explicit I/O transactions to break the connection before the object is
    // * permanently discarded.
    // * <p/>
    // * The {@code finalize} method of class {@code Object} performs no
    // * special action; it simply returns normally. Subclasses of
    // * {@code Object} may override this definition.
    // * <p/>
    // * The Java programming language does not guarantee which thread will
    // * invoke the {@code finalize} method for any given object. It is
    // * guaranteed, however, that the thread that invokes finalize will not
    // * be holding any user-visible synchronization locks when finalize is
    // * invoked. If an uncaught exception is thrown by the finalize method,
    // * the exception is ignored and finalization of that object terminates.
    // * <p/>
    // * After the {@code finalize} method has been invoked for an object, no
    // * further action is taken until the Java virtual machine has again
    // * determined that there is no longer any means by which this object can
    // * be accessed by any thread that has not yet died, including possible
    // * actions by other objects or classes which are ready to be finalized,
    // * at which point the object may be discarded.
    // * <p/>
    // * The {@code finalize} method is never invoked more than once by a Java
    // * virtual machine for any given object.
    // * <p/>
    // * Any exception thrown by the {@code finalize} method causes
    // * the finalization of this object to be halted, but is otherwise
    // * ignored.
    // *
    // * @throws Throwable the {@code Exception} raised by this method
    // */
    // @Override
    // protected void finalize() throws Throwable {
    // getLog().info("Finalize Maven embedded mysql");
    // // TODO: We should make sure database is cleaned up properly. If we do it
    // in
    // // stopDatabase();
    // super.finalize();
    // }

    /**
     * Starts embedded MySql server
     * 
     * @param baseDir
     *            Directory containig MySql data
     * @param port
     *            Port at which database will be running
     * @param dbname
     *            name of database
     * @param user
     *            User that will be used to access dabase
     * @throws MojoExecutionException
     */
    public final void startDatabase(File baseDir, int port, String dbname,
        String user, String password, Map<String, String> options)
        throws MojoExecutionException {
        getLog().info(
            "Starting database " + dbname + " at port " + port + " for user "
                + user);
        EmbeddedMysqlManager manager = getInstance();
        if (!baseDir.exists()) {
            try {
                FileUtils.forceMkdir(baseDir);

            } catch (IOException e) {
                throw new MojoExecutionException(
                    "Can't create target directory "
                        + baseDir.getAbsolutePath(), e);
            }
        }

        manager.setBaseDatabaseDir(baseDir.getAbsolutePath());
        manager.setDatabaseName(dbname);
        manager.setPort(port);
        manager.setUsername(user);
        manager.setPassword(password);
        manager.setDatabaseOptions(options);
        manager.startDatabase();
    }

    public final void stopDatabase() {
        EmbeddedMysqlManager manager = getInstance();
        if (manager != null) {
            getLog().info("Stop database " + baseDir.getAbsolutePath());
            manager.setBaseDatabaseDir(baseDir.getAbsolutePath());
            manager.setDatabaseName(dbname);
            manager.shutdownDatabase();
            instance = null;
        }
    }

}
