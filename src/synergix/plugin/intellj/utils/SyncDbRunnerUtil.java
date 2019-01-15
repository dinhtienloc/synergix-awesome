package synergix.plugin.intellj.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class SyncDbRunnerUtil {
    public final static String NON_GUI_EXPORT_SCHEMA_FILE_NAME = "runNonGUI.bat";
    public final static String INTELLIJ_BATCH_RUN = "intelljBatchRun.bat";

    public static boolean createNonGUIExportSchemeBat(String supermodelStableDirectory) {
        List<String> lines = Arrays.asList(
                "REM open GUI mode",
                "SET JAVA_PATH=\"%JAVA_HOME%\\bin\"",
                "SET JAVA_CMD=%JAVA_PATH%\\java.exe",
                "%JAVA_CMD% -jar ExportSchemaApp.jar");
        return writeFile(supermodelStableDirectory + File.separator + NON_GUI_EXPORT_SCHEMA_FILE_NAME, lines);
    }

    public static boolean extractPropertiesToFile(Properties properties, String settingPath) {
        Path path = createFile(settingPath);
        try (OutputStream output = new FileOutputStream(path.toFile())) {
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    public static boolean writeFile(String path, List<String> lines) {
        try {
            Path file = createFile(path);
            Files.write(file, lines, Charset.forName("UTF-8"));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static Path createFile(String path) {
        try {
            Path file = Paths.get(path);
            if (!Files.exists(file)) {
                if (file.getParent() != null) {
                    Files.createDirectories(file.getParent());
                }
                Files.createFile(file);
            }
            return file;
        } catch (IOException e) {
            return null;
        }
    }
}
