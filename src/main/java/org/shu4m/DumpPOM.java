/* 
 * Copyright 2013 SHU4M
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
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "dumppom", requiresDirectInvocation = true, defaultPhase = LifecyclePhase.NONE)
public class DumpPOM extends AbstractMojo {

  @Parameter(required = true, readonly = true, property = "prefix", defaultValue = "pom_")
  private String prefix;

  @Parameter(required = true, readonly = true, property = "filename", defaultValue = "pom")
  private String filename;

  @Parameter(required = true, readonly = true, property = "project")
  private MavenProject mavenProject;

  @Parameter(required = true, readonly = true, property = "session")
  private MavenSession mavenSession;

  @Component
  private BuildPluginManager pluginManager;

  public void execute() throws MojoExecutionException, MojoFailureException {
    getLog().info("dump project info into property file");

    Properties properties = new Properties();
    properties.setProperty(prefix + "groupid", mavenProject.getGroupId());
    properties.setProperty(prefix + "artifactid", mavenProject.getArtifactId());
    properties.setProperty(prefix + "version", mavenProject.getVersion());
    properties.setProperty(prefix + "name", mavenProject.getName());
    if (Utils.isNotBlank(mavenProject.getDescription()))
      properties.setProperty(prefix + "description", mavenProject.getDescription());

    File file = new File(mavenProject.getBuild().getDirectory(), filename + ".properties");
    Utils.makeDir(file.getParentFile());

    FileWriter writer = null;
    try {
      writer = new FileWriter(file);
      properties.store(writer, "generated by maven plugin shu4m:dump-pom");
    }
    catch (IOException e) {
      throw new MojoFailureException("Failed writing to " + file.getPath(), e);
    }
    finally {
      Utils.close(getLog(), writer);
    }
  }
}