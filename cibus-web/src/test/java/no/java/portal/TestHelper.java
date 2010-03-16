package no.java.portal;

import java.io.File;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
public abstract class TestHelper {

    public static File getBaseDir(Class aClass) {
        String basedir = System.getProperty("basedir");
        if (basedir != null) {
            return new File(basedir);
        } else {
            File file = new File(aClass.getProtectionDomain().getCodeSource().getLocation().getPath());
            if (!file.exists()) {
                throw new RuntimeException("Unable to find basedir");
            }
            while (!new File(file, "pom.xml").exists()) {
                file = file.getParentFile();
                if (file == null) {
                    throw new RuntimeException("Unable to find basedir");
                }
            }
            return file;
        }
    }

    public static String getWebAppDir(Class aClass) {
        File dir = new File(getBaseDir(aClass), "src/main/webapp");
        if (!dir.exists()) {
            throw new RuntimeException("Unable to find web application directory");
        }
        return dir.toString();
    }
    
}
