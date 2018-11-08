package synergix.plugin.intellj.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class RunnerUtil {
    public final static String NON_GUI_EXPORT_SCHEMA_FILE_NAME = "runNonGUI.bat";
    public final static String INTELLIJ_BATCH_RUN = "intelljBatchRun.bat";

    public static boolean createNonGUIExportSchemeBat(String supermodelStableDirectory) {
        List<String> lines = Arrays.asList(
                "REM open GUI mode",
                "SET JAVA_PATH=\"%JAVA_HOME%\\bin\"",
                "SET JAVA_CMD=%JAVA_PATH%\\java.exe",
                "%JAVA_CMD% -jar ExportSchemaApp.jar");
        return createFile(supermodelStableDirectory + File.separator + NON_GUI_EXPORT_SCHEMA_FILE_NAME, lines);
    }

    public static boolean createFile(String path, List<String> lines) {
        try {
            Path file = Paths.get(path);
            Files.write(file, lines, Charset.forName("UTF-8"));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
