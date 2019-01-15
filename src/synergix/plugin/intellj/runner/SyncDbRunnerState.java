package synergix.plugin.intellj.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.yourkit.util.Strings;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import synergix.plugin.intellj.execution.configuration.SyncDbConfiguration;
import synergix.plugin.intellj.utils.SyncDbRunnerUtil;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class SyncDbRunnerState extends CommandLineState {
    private SyncDbConfiguration syncDbConfiguration;

    public SyncDbRunnerState(@NotNull ExecutionEnvironment environment, final SyncDbConfiguration syncDbConfiguration) {
        super(environment);
        this.syncDbConfiguration = syncDbConfiguration;
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        String supermodelStableDir = this.syncDbConfiguration.getSuperModelStableDirectory();
        List<String> cmds = new ArrayList<>(Arrays.asList("cmd", "/c"));
        List<String> bodies = new ArrayList<>();
        List<String> errorMsgs = new ArrayList<>();

        Project project = this.syncDbConfiguration.getProject();
        if (this.syncDbConfiguration.getDbNames().isEmpty()) {
            errorMsgs.add("There is no database to sync.");
        }
        if (StringUtils.isEmpty(this.syncDbConfiguration.getDbCommand())) {
            errorMsgs.add("There is no command action.");
        }
        if (StringUtils.isEmpty(this.syncDbConfiguration.getDbSchema())) {
            errorMsgs.add("There is no schema to sync.");
        }
        Properties props = this.createSettingProperties();
        String settingFilePath = this.syncDbConfiguration.getSuperModelStableDirectory() + File.separator + "settings.ini";
        SyncDbRunnerUtil.extractPropertiesToFile(props, settingFilePath);

        if (SyncDbRunnerUtil.createNonGUIExportSchemeBat(supermodelStableDir)) {
            bodies.add(SyncDbRunnerUtil.NON_GUI_EXPORT_SCHEMA_FILE_NAME);
            bodies.add("cd " + this.syncDbConfiguration.getSuperModelDistDirectory());
            String command = this.syncDbConfiguration.getDbCommand();
            String schema = this.syncDbConfiguration.getDbSchema();
            String syncTemplale = "(echo %s & echo. & java -jar SuperModel.jar --" + command + " --schema=" + schema + ".xml --db=%s --includeViews)";
            for (String db : this.syncDbConfiguration.getDbNames()) {
                bodies.add(String.format(syncTemplale, db, db));
            }
        }

        GeneralCommandLine generalCommandLine = new GeneralCommandLine(cmds);

        if (!errorMsgs.isEmpty()) {
            String errorContent = String.join("<br/>", errorMsgs);
            new SyncDbErrorNotification(errorContent, NotificationType.ERROR).notify(project);
        } else {
            generalCommandLine.addParameters(this.parseToCommand(bodies));
            generalCommandLine.setCharset(Charset.forName("UTF-8"));
            generalCommandLine.setWorkDirectory(supermodelStableDir);
        }

        ProcessHandlerFactory factory = ProcessHandlerFactory.getInstance();
        OSProcessHandler processHandler = factory.createColoredProcessHandler(generalCommandLine);
        processHandler.addProcessListener(new ProcessListener() {
            @Override
            public void startNotified(@NotNull ProcessEvent event) {
            }

            @Override
            public void processTerminated(@NotNull ProcessEvent event) {
            }

            @Override
            public void processWillTerminate(@NotNull ProcessEvent event, boolean willBeDestroyed) {
            }

            @Override
            public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
                String textLine = event.getText();
                if (textLine.contains("Has errors while reading schema")) {
                    event.getProcessHandler().destroyProcess();
                }
            }
        });
        ProcessTerminatedListener.attach(processHandler);
        return processHandler;
    }

    private Properties createSettingProperties() {
        Properties prop = new Properties();
        prop.setProperty("isTesting", "N");
        prop.setProperty("postExportCommand", "");
        prop.setProperty("outputPath", this.syncDbConfiguration.getSuperModelDistDirectory());
        prop.setProperty("svn.user", this.syncDbConfiguration.getSvnUser());
        prop.setProperty("schemaPath", this.syncDbConfiguration.getProject().getBasePath() + File.separator
                + String.join(File.separator, "TH6/src/main/resources/synergix/th6/data/meta".split("/")));
        prop.setProperty("mainschemadir", "schema");
        prop.setProperty("se", "Y");
        prop.setProperty("kdiff3Path", this.syncDbConfiguration.getSuperModelStableDirectory() + File.separator
                + "Kdiff" + File.separator + "kdiff3.exe");
        prop.setProperty("iamahacker", this.syncDbConfiguration.isiAmHacker() ? "Y" : "N");
        prop.setProperty("trunkPath", "https://svn.synergixtech.com/svn/TH6/trunk");
        prop.setProperty("svn.pass", this.syncDbConfiguration.getSvnPass());
        prop.setProperty("ctrlschemadir", "ctrlschema");
        return prop;
    }

    private String parseToCommand(List<String> msg) {
        return Strings.join("", msg.toArray(new String[msg.size()]), " && ", false);

    }

    private class SyncDbErrorNotification extends Notification {
        SyncDbErrorNotification(@NotNull String content, @NotNull NotificationType type) {
            super("Synergix Error", "Sync DB Error", content, type);
        }
    }
}
