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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.MojoExecutionException;

public class Version {
  private static final Pattern VERSION_PARSER = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)(-((\\d+)|([a-zA-Z][a-zA-Z0-9_-]+)))?");

  private int major;
  private Integer minor;
  private Integer incremental;
  private Integer build;
  private String qualifier;

  public Version(int major, int minor, int incremental) {
    this.major = major;
    this.minor = minor;
    this.incremental = incremental;
  }

  public Version(int major, int minor, int incremental, Integer build) {
    this.major = major;
    this.minor = minor;
    this.incremental = incremental;
    this.build = build;
  }

  public Version(int major, int minor, int incremental, String qualifier) {
    this.major = major;
    this.minor = minor;
    this.incremental = incremental;
    this.qualifier = qualifier;
  }

  public int getMajor() {
    return major;
  }

  public Integer getMinor() {
    return minor;
  }

  public Integer getIncremental() {
    return incremental;
  }

  public Integer getBuild() {
    return build;
  }

  public String getQualifier() {
    return qualifier;
  }

  @Override
  public String toString() {
    StringBuilder buff = new StringBuilder();
    buff.append(this.getMajor());
    Utils.append(buff, '.', this.getMinor());
    Utils.append(buff, '.', this.getIncremental());
    Utils.append(buff, '-', this.getBuild());
    Utils.append(buff, '-', this.getQualifier());
    return buff.toString();
  }

  public static Version parseVersion(String version) throws MojoExecutionException {
    if (Utils.isNotBlank(version)) {
      Matcher matcher = VERSION_PARSER.matcher(version.trim());
      if (matcher.matches()) {
        if (matcher.groupCount() > 3) {
          if (matcher.group(6) != null)
            return new Version(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(6)));
          else
            return new Version(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), matcher.group(7));
        }
        else
          return new Version(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
      }
    }
    throw new MojoExecutionException("Unhandled version format: '" + version + "'");
  }

}