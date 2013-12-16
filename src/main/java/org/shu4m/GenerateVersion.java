/* 
 * Copyright 2013 Matthias Huebner
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.shu4m;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Generate Version class file from pom.xml
 * 
 * @author mhueb
 * 
 */
@Mojo(name = "generate-version", requiresDirectInvocation = true, defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenerateVersion extends AbstractMojo {

  private static final String GROUP_ID = "GROUP_ID";
  private static final String ARTIFACT_ID = "ARTIFACT_ID";
  private static final String VERSION = "VERSION";
  private static final String NAME = "NAME";
  private static final String DESCRIPTION = "DESCRIPTION";
  private static final String ORGANISATION = "ORGANISATION";
  private static final String BUILD_DATE = "BUILD_DATE";

  @Parameter(required = true, readonly = true, property = "project")
  private MavenProject mavenProject;

  @Parameter(required = true, readonly = true, property = "session")
  private MavenSession mavenSession;

  @Parameter(property = "sourcedir", required = true, defaultValue = "version")
  private String source;

  @Parameter(property = "packagename", required = false, defaultValue = "")
  private String packageName;

  @Parameter(property = "classname", required = true, defaultValue = "Version")
  private String className;

  @Parameter(property = "encoding", required = true, defaultValue = "UTF-8")
  private String encoding;

  public void execute() throws MojoExecutionException, MojoFailureException {
    if (packageName == null || packageName.isEmpty())
      packageName = Utils.makeJavaName(mavenProject.getGroupId()) + "." + Utils.makeJavaName(mavenProject.getArtifactId()) + ".misc";

    getLog().info("Generating " + packageName + "." + className);

    File dir = new File(mavenProject.getBuild().getDirectory() + "/generated-sources/" + source + "/" + Utils.packageToPath(packageName));
    File buildInfoFile = new File(dir, className + ".java");

    Utils.makeDir(dir);

    FileOutputStream out = null;
    OutputStreamWriter outputStreamWriter = null;
    OutputStreamWriter writer = null;

    try {
      out = new FileOutputStream(buildInfoFile);
      outputStreamWriter = new OutputStreamWriter(out, encoding);
      writer = outputStreamWriter;

      writer.write("/*\n * Generated by shu4m:generate-version\n */\npackage ");
      writer.write(packageName);
      writer.write(";\n\n/**\n * Version of ");
      writer.write(mavenProject.getGroupId());
      writer.write(":");
      writer.write(mavenProject.getArtifactId());
      writer.write("<br/>\n * <br/>\n * Generated by shu4m:generate-version\n */\npublic final class ");
      writer.write(className);
      writer.write(" {\n");
      writePropery(writer, NAME, mavenProject.getName());
      writePropery(writer, GROUP_ID, mavenProject.getGroupId());
      writePropery(writer, ARTIFACT_ID, mavenProject.getArtifactId());
      writePropery(writer, VERSION, mavenProject.getVersion());
      writePropery(writer, BUILD_DATE, Utils.formatDate(mavenSession.getStartTime()));
      writePropery(writer, DESCRIPTION, Utils.clean(mavenProject.getDescription()));
      writePropery(writer, ORGANISATION, Utils.clean(getOrganisation()));
      writer.write("\n");
      writeSingletonGetter(writer);
      writeConstructor(writer);
      writeGetter(writer, "String", "getProjectName", NAME);
      writeGetter(writer, "String", "getProjectVersion", VERSION);
      writeGetter(writer, "String", "getGroupId", GROUP_ID);
      writeGetter(writer, "String", "getArtifactId", ARTIFACT_ID);
      writeGetter(writer, "String", "getBuildDate", BUILD_DATE);
      writeGetter(writer, "String", "getDescription", DESCRIPTION);
      writeGetter(writer, "String", "getOrganisation", ORGANISATION);
      writer.write("\n}\n");
    }
    catch (IOException e) {
      throw new MojoFailureException("Failed to generate version file", e);
    }
    finally {
      Utils.close(getLog(), writer, outputStreamWriter, out);
    }
  }

  private String getOrganisation() {
    return mavenProject.getOrganization() == null ? "" : mavenProject.getOrganization().getName();
  }

  private void writeConstructor(OutputStreamWriter writer) throws IOException {
    writer.write("  private ");
    writer.write(className);
    writer.write("() {\n  }\n\n");
  }

  private void writeSingletonGetter(OutputStreamWriter writer) throws IOException {
    writer.write("  private static final ");
    writer.write(className);
    writer.write(" INSTANCE = new ");
    writer.write(className);
    writer.write("();\n\n  public static ");
    writer.write(className);
    writer.write(" get() {\n    return INSTANCE;\n  }\n\n");
  }

  private void writeGetter(OutputStreamWriter writer, String type, String name, String field) throws IOException {
    writer.write("  public ");
    writer.write(type);
    writer.write(" ");
    writer.write(name);
    writer.write("() {\n    return ");
    writer.write(field);
    writer.write(";\n  }\n\n");
  }

  private void writePropery(OutputStreamWriter writer, String name, String value) throws IOException {
    writer.write("\n  private static final String ");
    writer.write(name);
    writer.write(" = \"");
    writer.write(value);
    writer.write("\";\n");
  }
}