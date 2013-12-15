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
}
