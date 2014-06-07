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

import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executeMojo;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.groupId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;
import static org.twdata.maven.mojoexecutor.MojoExecutor.version;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class AbstractVersionManipulator extends AbstractMojo {

  @Parameter(required = true, readonly = true, property = "project")
  protected MavenProject mavenProject;

  @Parameter(required = true, readonly = true, property = "session")
  protected MavenSession mavenSession;

  @Component
  protected BuildPluginManager pluginManager;

  public void executeSetVersion(Version newVersion) throws MojoExecutionException {
    // @formatter:off
    executeMojo(
        plugin(
            groupId("org.codehaus.mojo"), 
            artifactId("versions-maven-plugin"), 
            version("2.1")),
        goal("set"), 
        configuration(
            element(name("updateMatchingVersions"), "true"), 
            element(name("generateBackupPoms"), "false"), 
            element(name("newVersion"), newVersion.toString())), 
        executionEnvironment(mavenProject, mavenSession, pluginManager)
    );
    // @formatter:on
  }

  protected int next(Integer value) {
    if (value == null)
      value = 0;
    return value + 1;
  }
}