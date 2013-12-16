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

  @Parameter(required = false, property = "major")
  private Integer major;

  @Parameter(required = false, property = "minor")
  private Integer minor;

  @Parameter(required = false, property = "incremental")
  private Integer incremental;

  @Parameter(required = false, property = "buildnumber")
  private Integer buildnumber;

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
    Version version = Utils.parseVersion(mavenProject.getVersion());
    Version newVersion = create(version);
    Utils.executeSetVersion(newVersion, mavenProject, mavenSession, pluginManager);
  }

  private Version create(Version version) {
    if (qualifier != null) {
      String qualifierEx = qualifier;
      if (buildnumber != null)
        qualifierEx = qualifierEx + buildnumber;
      return new Version(get(version.getMajor(), major), get(version.getMinor(), minor), get(version.getIncremental() + 1, incremental), qualifierEx);
    }
    else
      return new Version(get(version.getMajor(), major), get(version.getMinor(), minor), get(version.getIncremental() + 1, incremental), buildnumber);
  }

  private int get(Integer value, Integer replace) {
    if (replace != null)
      return replace;
    else
      return value;
  }
}