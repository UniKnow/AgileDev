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
        for (ClassDoc doc : rootDoc.classes()) {
            renderClass(doc);
        }
        for (PackageDoc doc : rootDoc.specifiedPackages()) {
            renderPackage(doc);
        }
        return true;
    }

    public void renderClass(ClassDoc doc) {
        try {
            PrintWriter writer = getWriter(doc.containingPackage(), doc.name());

            if (doc.position() != null) {
                writer.println("//tag::class-description[]");
                outputText(doc.name(), doc.getRawCommentText(), writer);
                writer.println("//end::class-description[]");
            }

            if (doc.fields().length > 0) {
                writer.println("//tag::field-descriptions[]");
                for (MemberDoc member : doc.fields(false)) {
                    outputText(member.name(), member.getRawCommentText(),
                        writer);
                }
                writer.println("//end::field-descriptions[]");
            }

            if (doc.constructors(false).length > 0) {
                writer.println("//tag::constructor-descriptions[]");
                for (MemberDoc member : doc.constructors(false)) {
                    outputText(member.name(), member.getRawCommentText(),
                        writer);
                }
                writer.println("//end::constructor-descriptions[]");
            }

            if (doc.methods(false).length > 0) {
                writer.println("//tag::method-descriptions[]");
                for (MethodDoc member : doc.methods(false)) {
                    outputText(member, writer);
                }
                writer.println("//end::method-descriptions[]");
            }

            if (doc.enumConstants().length > 0) {
                writer.println("//tag::enum-descriptions[]");
                writer.println("." + doc.name());
                writer
                    .println("[caption=\"Enums values(s) \",width=\"100%\",options=\"header\"]");
                writer.println("|===");
                writer.println("|Name|Description");
                for (FieldDoc member : doc.enumConstants()) {
                    outputTextEnum(member, writer);
                }
                writer.println("|===");
                writer.println("//end::enum-descriptions[]");
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
            writer.println("//tag::package-description[]");
            if (doc.position() != null) {
                outputText(doc.name(), doc.getRawCommentText(), writer);
            }
            if (doc instanceof AnnotationTypeDoc) {
                for (MemberDoc member : ((AnnotationTypeDoc) doc).elements()) {
                    outputText(member.name(), member.getRawCommentText(),
                        writer);
                }
            }
            writer.println("//end::package-description[]");
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate output for Enum
     * 
     * @param member
     *            enumeration for which we want to generate AsciiDoc
     * @param writer
     *            Writer that will be used to generate output.
     */
    private void outputTextEnum(FieldDoc member, PrintWriter writer) {

        writer.println("|");
        writer.println("// tag::enum-name[]");
        writer.println(member.name());
        writer.println("// tag::enum-name[]");
        writer.println("|");
        writer.println(member.commentText());
    }

    /**
     * Generate output for Method
     * 
     * @param method
     *            Method for which we want to create asciidoc
     * @param writer
     *            Writer that will be used to generate output
     */
    private void outputText(MethodDoc method, PrintWriter writer) {
        writer.println("// tag::method-description[]");
        writer.println("// tag::method-name[]");
        writer.println("." + method.name());
        writer.println("// end::method-name[]");
        writer.println("[caption=\"Method \"]");
        writer.println("==========================");
        if (!method.commentText().trim().isEmpty()) {
            writer.println("// tag::method-comment[]");
            writer.println(parseMethodComment(method.commentText()) + " +");
            writer.println("// end::method-comment[]");
        }

        if (method.parameters().length > 0) {
            writer.println("// tag::method-parameters[]");
            writer.println(".method " + method.name());
            writer
                .println("[caption=\"Parameter(s) \",width=\"100%\",options=\"header\"]");
            writer.println("|===");
            writer.println("|Name|Type");
            for (Parameter parameter : method.parameters()) {
                writer.println("|" + parameter.name() + "|"
                    + parameter.typeName() + "|");
            }
            writer.println("|===");
            writer.println("// end::method-parameters[]");
        }

        writer.println("_Return:_ ");
        writer.println("// tag::method-return[]");
        writer.println(method.returnType() + " +");
        writer.println("// end::method-return[]");
        writer.println("==========================");
        writer.println("// end::method-description[]");
    }

    private void outputText(String tag, String comment, PrintWriter writer) {
        writer.println("// tag::" + tag + "[]");
        writer.println(cleanJavadocInput(comment));
        writer.println("// end::" + tag + "[]");
    }

    private String parseMethodComment(String input) {
        String cleanedText = cleanJavadocInput(input);

        // Replace all @param entries with _Parameter_
        cleanedText = cleanedText.replaceAll("@param", "_Parameter:_ ");
        cleanedText = cleanedText.replaceAll("@return", "_Return:_ ");

        return cleanedText;
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
        File file = new File(pacakgeDirectory, name + ".ad");
        return new PrintWriter(new OutputStreamWriter(
            new FileOutputStream(file)));
    }
}
