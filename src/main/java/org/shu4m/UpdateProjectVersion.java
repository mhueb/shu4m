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
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "update-projectversion", requiresDirectInvocation = true, defaultPhase = LifecyclePhase.NONE)
public class UpdateProjectVersion extends AbstractMojo {

  @Parameter(required = false, property = "incremental")
  private Integer incremental;

  @Parameter(required = false, property = "incincremental")
  private Boolean incIncrement;

  @Parameter(required = false, property = "buildnumber")
  private Integer buildNumber;

  @Parameter(required = false, property = "incbuildnumber")
  private Boolean incBuildNumber;

  @Parameter(required = false, property = "qualifier")
  private String qualifier;

  @Parameter(required = true, readonly = true, property = "project")
  private MavenProject mavenProject;

  @Parameter(required = true, readonly = true, property = "session")
  private MavenSession mavenSession;

  @Component
  private BuildPluginManager pluginManager;

  public void execute() throws MojoExecutionException, MojoFailureException {
    getLog().info("increment version");
    if (buildNumber != null && qualifier != null)
      throw new MojoFailureException("Use of properties 'buildnumber' and 'qualifier' together are not allowed");
    Version version = Version.parseVersion(mavenProject.getVersion());
    Version newVersion = adaptVersion(version);
    executeSetVersion(newVersion);
  }

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

  private Version adaptVersion(Version version) {
    int incrementalEx = Utils.getReplaceValue(version.getIncremental(), incremental);
    Integer buildNumberEx = Utils.getReplaceValue(version.getBuild(), buildNumber);
    String qualifierEx = Utils.getReplaceValue(version.getQualifier(), qualifier);

    if (incIncrement && incremental == null)
      ++incrementalEx;

    if (incBuildNumber && buildNumber == null && buildNumberEx != null)
      ++buildNumberEx;

    if (qualifierEx != null)
      return new Version(version.getMajor(), version.getMinor(), incrementalEx, qualifierEx);
    else
      return new Version(version.getMajor(), version.getMinor(), incrementalEx, buildNumberEx);
  }

}