package helperClasses;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Class from
// https://stackoverflow.com/questions/18004150/desktop-api-is-not-supported-on-the-current-platform

/* Class to help in opening the folder in which image is in */
public class DesktopAPI {

  public void open(File file) {
    if (!openSystemSpecific(file.getPath())) {
      openDESKTOP(file);
    }
  }

  public enum EnumOS {
    linux, macos, solaris, unknown, windows;

    public boolean isLinux() {
      return this == linux || this == solaris;
    }

    public boolean isMac() {
      return this == macos;
    }

    public boolean isWindows() {
      return this == windows;
    }
  }

  private EnumOS getOs() {
    String s = System.getProperty("os.name").toLowerCase();
    if (s.contains("win")) {
      return EnumOS.windows;
    }
    if (s.contains("mac")) {
      return EnumOS.macos;
    }
    if (s.contains("solaris")) {
      return EnumOS.solaris;
    }
    if (s.contains("sunos")) {
      return EnumOS.solaris;
    }
    if (s.contains("linux")) {
      return EnumOS.linux;
    }
    if (s.contains("unix")) {
      return EnumOS.linux;
    } else {
      return EnumOS.unknown;
    }
  }

  private String[] prepareCommand(String command, String file) {
    List<String> parts = new ArrayList<>();
    parts.add(command);
    for (String s : "%s".split(" ")) {
      s = String.format(s, file); // put in the filename thing

      parts.add(s.trim());
    }
    return parts.toArray(new String[parts.size()]);
  }

  private boolean runCommand(String command, String file) {
    String[] parts = prepareCommand(command, file);
    try {
      Process p = Runtime.getRuntime().exec(parts);
      if (p == null) {
        return false;
      }
      try {
        int retval = p.exitValue();
        if (retval == 0) {
          return false;
        } else {
          return false;
        }
      } catch (IllegalThreadStateException itse) {
        return true;
      }
    } catch (IOException e) {
      return false;
    }
  }

  private void openDESKTOP(File file) {
    try {
      if (!Desktop.isDesktopSupported()) {
        return;
      }
      if (!Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
        return;
      }
      Desktop.getDesktop().open(file);

    } catch (Throwable ignored) {
    }
  }

  private boolean openSystemSpecific(String what) {
    EnumOS os = getOs();
    if (os.isLinux()) {
      if (runCommand("kde-open", what)) {
        return true;
      }
      if (runCommand("gnome-open", what)) {
        return true;
      }
      if (runCommand("xdg-open", what)) {
        return true;
      }
    }
    if (os.isMac()) {
      if (runCommand("open", what)) {
        return true;
      }
    }
    if (os.isWindows()) {
      if (runCommand("explorer", what)) {
        return true;
      }
    }
    return false;
  }
}