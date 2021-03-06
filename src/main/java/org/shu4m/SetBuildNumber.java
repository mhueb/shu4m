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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "set-buildnumber", requiresDirectInvocation = true, defaultPhase = LifecyclePhase.NONE)
public class SetBuildNumber extends AbstractVersionManipulator {

  @Parameter(required = true, property = "buildnumber")
  private Integer buildNumber;

  public void execute() throws MojoExecutionException, MojoFailureException {
    getLog().info("Set build number");
    Version version = Version.parseVersion(mavenProject.getVersion());
    Version newVersion = new Version(version.getMajor(), version.getMinor(), version.getIncremental(), buildNumber);
    executeSetVersion(newVersion);
  }
}