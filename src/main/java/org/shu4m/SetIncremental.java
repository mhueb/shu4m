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

@Mojo(name = "set-incremental", requiresDirectInvocation = true, defaultPhase = LifecyclePhase.NONE)
public class SetIncremental extends AbstractVersionManipulator {

  @Parameter(required = true, property = "incremental")
  private Integer incremental;

  @Parameter(required = false, property = "qualifier")
  private String qualifier;

  public void execute() throws MojoExecutionException, MojoFailureException {
    if (qualifier != null)
      getLog().info("set incremental to " + incremental + " and qualifier to " + qualifier);
    else
      getLog().info("set incremental to " + incremental);
    Version version = Version.parseVersion(mavenProject.getVersion());
    Version newVersion = Version.toVersion(version.getMajor(), version.getMinor(), incremental, qualifier != null ? null : version.getBuild(), qualifier != null ? qualifier : version.getQualifier());
    executeSetVersion(newVersion);
  }
}
