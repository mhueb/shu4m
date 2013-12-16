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

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

public class Utils {
  public static String packageToPath(String packageName) {
    return packageName.replace('.', '/');
  }

  public static String clean(String string) {
    return string == null ? "" : string.replace("\"", "\\\"").replace("\t", "\\t").replace("\n", "\\n").replace("\r", "\\r");
  }

  public static String formatDate(Date date) {
    return date == null ? "" : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
  }

  public static String makeJavaName(String name) {
    StringBuilder buff = new StringBuilder();
    for (int idx = 0; idx < name.length(); ++idx) {
      char currentChar = Character.toLowerCase(name.charAt(idx));
      if (currentChar == '-')
        currentChar = '_';
      if (idx > 0 && (Character.isJavaIdentifierPart(currentChar) || currentChar == '.') || idx == 0 && Character.isJavaIdentifierStart(currentChar))
        buff.append(currentChar);
    }
    return buff.toString();
  }

  public static void makeDir(File dir) {
    if (dir == null || dir.exists())
      return;
    makeDir(dir.getParentFile());
    dir.mkdir();
  }

  public static void close(Log log, Closeable... closeables) {
    for (Closeable closeable : closeables)
      try {
        if (closeable != null)
          closeable.close();
      }
      catch (IOException e) {
        log.info(e);
      }
  }

  public static void executeSetVersion(Version newVersion, MavenProject mavenProject, MavenSession mavenSession, BuildPluginManager pluginManager) throws MojoExecutionException {
    executeMojo(plugin(groupId("org.codehaus.mojo"), artifactId("versions-maven-plugin"), version("2.1")), goal("set"), configuration(element(name("updateMatchingVersions"), "true"), element(name("generateBackupPoms"), "false"), element(name("newVersion"), formatVersion(newVersion))), executionEnvironment(mavenProject, mavenSession, pluginManager));
  }

  public static String formatVersion(Version version) {
    if (version == null)
      return "";
    StringBuilder buff = new StringBuilder();
    buff.append(version.getMajor());
    append(buff, '.', version.getMinor());
    append(buff, '.', version.getIncremental());
    append(buff, '-', version.getBuild());
    append(buff, '-', version.getQualifier());
    return buff.toString();
  }

  private static void append(StringBuilder buff, char separator, Object value) {
    if (value != null)
      buff.append(separator).append(value);
  }

  public static boolean isNotBlank(String string) {
    return string != null && !string.trim().isEmpty();
  }

  private static final Pattern VERSION_PARSER = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)(-((\\d+)|([a-zA-Z][a-zA-Z0-9]+)))?");

  public static Version parseVersion(String version) throws MojoExecutionException {
    if (isNotBlank(version)) {
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
