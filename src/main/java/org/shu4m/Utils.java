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

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.maven.plugin.logging.Log;

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

  public static void append(StringBuilder buff, char separator, Object value) {
    if (value != null)
      buff.append(separator).append(value);
  }

  public static boolean isNotBlank(String string) {
    return string != null && !string.trim().isEmpty();
  }

  public static <T> T getReplaceValue(T value, T replace) {
    if (replace != null)
      return replace;
    else
      return value;
  }

}
