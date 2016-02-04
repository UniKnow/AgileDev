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
package org.uniknow.asciidoc;

import com.sun.javadoc.*;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Renderer which exports javadoc comment to asciidoc file
 */
public class ExportRenderer {

    public boolean render(RootDoc rootDoc) {
        Set<PackageDoc> packages = new HashSet<PackageDoc>();
        for (ClassDoc doc : rootDoc.classes()) {
            packages.add(doc.containingPackage());
            renderClass(doc);
        }
        for (PackageDoc doc : packages) {
            renderPackage(doc);
            // renderer.renderDoc(doc);
        }
        return true;
    }

    public void renderClass(ClassDoc doc) {
        try {
            PrintWriter writer = getWriter(doc.containingPackage(), doc.name());
            if (doc.position() != null) {
                outputText(doc.name(), doc.getRawCommentText(), writer);
            }
            for (MemberDoc member : doc.fields(false)) {
                outputText(member.name(), member.getRawCommentText(), writer);
            }
            for (MemberDoc member : doc.constructors(false)) {
                outputText(member.name(), member.getRawCommentText(), writer);
            }
            for (MemberDoc member : doc.methods(false)) {
                outputText(member.name(), member.getRawCommentText(), writer);
            }
            for (MemberDoc member : doc.enumConstants()) {
                outputText(member.name(), member.getRawCommentText(), writer);
            }
            if (doc instanceof AnnotationTypeDoc) {
                for (MemberDoc member : ((AnnotationTypeDoc) doc).elements()) {
                    outputText(member.name(), member.getRawCommentText(),
                        writer);
                }
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void renderPackage(PackageDoc doc) {
        try {
            PrintWriter writer = getWriter(doc, "package-info");
            writer.println(doc.name());
            if (doc.position() != null) {
                outputText(doc.name(), doc.getRawCommentText(), writer);
            }
            if (doc instanceof AnnotationTypeDoc) {
                for (MemberDoc member : ((AnnotationTypeDoc) doc).elements()) {
                    outputText(member.name(), member.getRawCommentText(),
                        writer);
                }
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void outputText(String tag, String comment, PrintWriter writer) {
        writer.println("// tag::" + tag + "[]");
        writer.println(cleanJavadocInput(comment));
        writer.println("// end::" + tag + "[]");
    }

    protected String cleanJavadocInput(String input) {
        return input.trim().replaceAll("\n ", "\n") // Newline space to
                                                    // accommodate javadoc
                                                    // newlines.
            .replaceAll("(?m)^( *)\\*\\\\/$", "$1*/"); // Multi-line comment end
                                                       // tag is translated into
                                                       // */.
    }

    private PrintWriter getWriter(PackageDoc packageDoc, String name)
        throws FileNotFoundException {
        File pacakgeDirectory = new File(packageDoc.name().replace('.',
            File.separatorChar));
        if (!pacakgeDirectory.exists()) {
            pacakgeDirectory.mkdirs();
        }
        File file = new File(pacakgeDirectory, name + ".adoc");
        return new PrintWriter(new OutputStreamWriter(
            new FileOutputStream(file)));
    }
}
